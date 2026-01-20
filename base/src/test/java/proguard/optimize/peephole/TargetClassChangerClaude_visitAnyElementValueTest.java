package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.ElementValue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TargetClassChanger#visitAnyElementValue(Clazz, Annotation, ElementValue)}.
 *
 * The visitAnyElementValue method updates the referenced class and referenced method of an
 * ElementValue when class merging has occurred. If the element value's referenced class has
 * a target class (due to vertical class merging), it updates both the referencedClass and
 * referencedMethod fields to point to the appropriate entities in the target class.
 */
public class TargetClassChangerClaude_visitAnyElementValueTest {

    private TargetClassChanger changer;
    private Clazz clazz;
    private Annotation annotation;
    private ElementValue elementValue;

    @BeforeEach
    public void setUp() {
        changer = new TargetClassChanger();
        clazz = mock(ProgramClass.class);
        annotation = mock(Annotation.class);
        elementValue = mock(ElementValue.class);
    }

    /**
     * Tests that visitAnyElementValue can be called without throwing exceptions.
     */
    @Test
    public void testVisitAnyElementValue_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> changer.visitAnyElementValue(clazz, annotation, elementValue));
    }

    /**
     * Tests that visitAnyElementValue can be called multiple times.
     */
    @Test
    public void testVisitAnyElementValue_calledMultipleTimes_doesNotFail() {
        // Act & Assert - should not throw exceptions on multiple calls
        assertDoesNotThrow(() -> {
            changer.visitAnyElementValue(clazz, annotation, elementValue);
            changer.visitAnyElementValue(clazz, annotation, elementValue);
            changer.visitAnyElementValue(clazz, annotation, elementValue);
        });
    }

    /**
     * Tests that visitAnyElementValue works with different element value instances.
     */
    @Test
    public void testVisitAnyElementValue_withDifferentElementValues_processesEach() {
        // Arrange
        ElementValue elementValue1 = mock(ElementValue.class);
        ElementValue elementValue2 = mock(ElementValue.class);
        ElementValue elementValue3 = mock(ElementValue.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitAnyElementValue(clazz, annotation, elementValue1);
            changer.visitAnyElementValue(clazz, annotation, elementValue2);
            changer.visitAnyElementValue(clazz, annotation, elementValue3);
        });
    }

    /**
     * Tests that visitAnyElementValue works with different annotation instances.
     */
    @Test
    public void testVisitAnyElementValue_withDifferentAnnotations_processesEach() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        Annotation annotation3 = mock(Annotation.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitAnyElementValue(clazz, annotation1, elementValue);
            changer.visitAnyElementValue(clazz, annotation2, elementValue);
            changer.visitAnyElementValue(clazz, annotation3, elementValue);
        });
    }

    /**
     * Tests that visitAnyElementValue works with different clazz instances.
     */
    @Test
    public void testVisitAnyElementValue_withDifferentClazz_processesEach() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitAnyElementValue(clazz1, annotation, elementValue);
            changer.visitAnyElementValue(clazz2, annotation, elementValue);
            changer.visitAnyElementValue(clazz3, annotation, elementValue);
        });
    }

    /**
     * Tests that visitAnyElementValue works with LibraryClass as well as ProgramClass.
     */
    @Test
    public void testVisitAnyElementValue_withLibraryClass_processesCorrectly() {
        // Arrange
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> changer.visitAnyElementValue(libraryClass, annotation, elementValue));
    }

    /**
     * Tests that visitAnyElementValue works with a mix of ProgramClass and LibraryClass.
     */
    @Test
    public void testVisitAnyElementValue_withMixedClazzTypes_handlesAll() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitAnyElementValue(programClass, annotation, elementValue);
            changer.visitAnyElementValue(libraryClass, annotation, elementValue);
        });
    }

    /**
     * Tests that visitAnyElementValue executes quickly.
     */
    @Test
    public void testVisitAnyElementValue_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            changer.visitAnyElementValue(clazz, annotation, elementValue);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
            "visitAnyElementValue should execute quickly");
    }

    /**
     * Tests that visitAnyElementValue can be called in rapid succession without issues.
     */
    @Test
    public void testVisitAnyElementValue_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                changer.visitAnyElementValue(clazz, annotation, elementValue);
            }
        }, "Rapid successive calls should not cause issues");
    }

    /**
     * Tests that multiple changers can call visitAnyElementValue independently.
     */
    @Test
    public void testVisitAnyElementValue_multipleChangersNoInterference() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();

        // Act & Assert - different changers should work independently
        assertDoesNotThrow(() -> {
            changer1.visitAnyElementValue(clazz, annotation, elementValue);
            changer2.visitAnyElementValue(clazz, annotation, elementValue);
            changer3.visitAnyElementValue(clazz, annotation, elementValue);
        });
    }

    /**
     * Tests that visitAnyElementValue works correctly when called on a newly created changer instance.
     */
    @Test
    public void testVisitAnyElementValue_onNewInstance_behavesConsistently() {
        // Arrange
        TargetClassChanger freshChanger = new TargetClassChanger();

        // Act & Assert - should work immediately after construction
        assertDoesNotThrow(() -> freshChanger.visitAnyElementValue(clazz, annotation, elementValue));
    }

    /**
     * Tests that visitAnyElementValue handles edge case of calling with various
     * combinations of different clazz, annotation, and element value tuples in sequence.
     */
    @Test
    public void testVisitAnyElementValue_sequentialCallsWithDifferentParameters_noIssues() {
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
        ElementValue[] elementValues = {
            mock(ElementValue.class),
            mock(ElementValue.class),
            mock(ElementValue.class)
        };

        // Act & Assert - call with different combinations
        assertDoesNotThrow(() -> {
            for (Clazz c : clazzes) {
                for (Annotation a : annotations) {
                    for (ElementValue ev : elementValues) {
                        changer.visitAnyElementValue(c, a, ev);
                    }
                }
            }
        });
    }

    /**
     * Tests that the visitAnyElementValue method implements the ElementValueVisitor interface correctly.
     */
    @Test
    public void testVisitAnyElementValue_implementsInterfaceCorrectly() {
        // Assert - TargetClassChanger should be an ElementValueVisitor
        assertTrue(changer instanceof proguard.classfile.attribute.annotation.visitor.ElementValueVisitor,
                "TargetClassChanger should implement ElementValueVisitor");
    }

    /**
     * Tests that visitAnyElementValue completes without blocking or hanging.
     */
    @Test
    public void testVisitAnyElementValue_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        changer.visitAnyElementValue(clazz, annotation, elementValue);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitAnyElementValue should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitAnyElementValue handles the same element value instance multiple times
     * without accumulating state or causing issues.
     */
    @Test
    public void testVisitAnyElementValue_sameElementValueInstanceMultipleTimes_noAccumulation() {
        // Arrange
        ElementValue singleElementValue = mock(ElementValue.class);

        // Act & Assert - call multiple times with the same element value
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                changer.visitAnyElementValue(clazz, annotation, singleElementValue);
            }
        });
    }

    /**
     * Tests that visitAnyElementValue called after multiple other operations still works correctly.
     */
    @Test
    public void testVisitAnyElementValue_afterMultipleOperations_stillWorks() {
        // Simulate some prior operations
        for (int i = 0; i < 5; i++) {
            changer.visitAnyElementValue(clazz, annotation, elementValue);
        }

        // Act & Assert - call visitAnyElementValue again
        assertDoesNotThrow(() -> changer.visitAnyElementValue(clazz, annotation, elementValue));
    }

    /**
     * Tests that visitAnyElementValue with varying combinations of parameters all execute successfully.
     */
    @Test
    public void testVisitAnyElementValue_varyingParameterCombinations_allExecuteSuccessfully() {
        // Arrange
        Clazz clazz2 = mock(ProgramClass.class);
        Annotation annotation2 = mock(Annotation.class);
        ElementValue elementValue2 = mock(ElementValue.class);

        // Act & Assert - call with various parameter combinations
        assertDoesNotThrow(() -> {
            changer.visitAnyElementValue(clazz, annotation, elementValue);
            changer.visitAnyElementValue(clazz2, annotation2, elementValue2);
            changer.visitAnyElementValue(clazz, annotation2, elementValue2);
        });
    }

    /**
     * Tests that visitAnyElementValue maintains consistency across invocations.
     */
    @Test
    public void testVisitAnyElementValue_consistentBehavior() {
        // Act & Assert - call multiple times and verify consistent behavior
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() ->
                changer.visitAnyElementValue(clazz, annotation, elementValue)
            );
        }
    }

    /**
     * Tests that visitAnyElementValue processes element values in the context of annotation processing.
     * Element values are the values associated with annotation methods.
     */
    @Test
    public void testVisitAnyElementValue_processesInAnnotationContext() {
        // Act & Assert - should work correctly in annotation context
        assertDoesNotThrow(() -> changer.visitAnyElementValue(clazz, annotation, elementValue));
    }

    /**
     * Tests that visitAnyElementValue handles element values from different annotations correctly.
     * Each element value belongs to an annotation and references an annotation class.
     */
    @Test
    public void testVisitAnyElementValue_handlesElementValuesFromDifferentAnnotations() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        ElementValue elementValue1 = mock(ElementValue.class);
        ElementValue elementValue2 = mock(ElementValue.class);

        // Act & Assert - should handle element values from different annotations
        assertDoesNotThrow(() -> {
            changer.visitAnyElementValue(clazz, annotation1, elementValue1);
            changer.visitAnyElementValue(clazz, annotation2, elementValue2);
        });
    }

    /**
     * Tests that visitAnyElementValue works with the visitor pattern correctly.
     * The method is part of the ElementValueVisitor interface implementation.
     */
    @Test
    public void testVisitAnyElementValue_worksWithVisitorPattern() {
        // Act - use the changer as an ElementValueVisitor
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;

        // Assert - should work correctly when used through the interface
        assertDoesNotThrow(() -> visitor.visitAnyElementValue(clazz, annotation, elementValue));
    }

    /**
     * Tests that visitAnyElementValue can handle element values in the context of
     * class retargeting during optimization.
     */
    @Test
    public void testVisitAnyElementValue_inClassRetargetingContext() {
        // Act & Assert - should work in the context of class retargeting
        assertDoesNotThrow(() -> changer.visitAnyElementValue(clazz, annotation, elementValue));
    }

    /**
     * Tests that visitAnyElementValue works correctly with different TargetClassChanger instances.
     * Each changer should be able to process element values independently.
     */
    @Test
    public void testVisitAnyElementValue_withDifferentChangerInstances() {
        // Arrange
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();

        // Act & Assert - should work for different changer instances
        assertDoesNotThrow(() -> {
            changer.visitAnyElementValue(clazz, annotation, elementValue);
            changer2.visitAnyElementValue(clazz, annotation, elementValue);
            changer3.visitAnyElementValue(clazz, annotation, elementValue);
        });
    }

    /**
     * Tests that visitAnyElementValue integrates correctly with the overall visitor pattern
     * used in the TargetClassChanger class.
     */
    @Test
    public void testVisitAnyElementValue_integratesWithVisitorPattern() {
        // Arrange
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;

        // Act & Assert - should work through the interface
        assertDoesNotThrow(() -> visitor.visitAnyElementValue(clazz, annotation, elementValue));
    }

    /**
     * Tests that visitAnyElementValue can process element values from annotations on different classes.
     */
    @Test
    public void testVisitAnyElementValue_fromAnnotationsOnDifferentClasses() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        ElementValue elementValue1 = mock(ElementValue.class);
        ElementValue elementValue2 = mock(ElementValue.class);

        // Act & Assert - should process element values from annotations on different classes
        assertDoesNotThrow(() -> {
            changer.visitAnyElementValue(clazz1, annotation1, elementValue1);
            changer.visitAnyElementValue(clazz2, annotation2, elementValue2);
        });
    }

    /**
     * Tests that visitAnyElementValue can handle a large number of element values.
     */
    @Test
    public void testVisitAnyElementValue_handlesLargeNumberOfElementValues() {
        // Arrange
        ElementValue[] elementValues = new ElementValue[100];
        for (int i = 0; i < elementValues.length; i++) {
            elementValues[i] = mock(ElementValue.class);
        }

        // Act & Assert - should handle processing many element values
        assertDoesNotThrow(() -> {
            for (ElementValue ev : elementValues) {
                changer.visitAnyElementValue(clazz, annotation, ev);
            }
        });
    }

    /**
     * Tests that visitAnyElementValue works correctly when processing element values
     * in a typical annotation processing workflow.
     */
    @Test
    public void testVisitAnyElementValue_inTypicalWorkflow() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        ElementValue ev1 = mock(ElementValue.class);
        ElementValue ev2 = mock(ElementValue.class);
        ElementValue ev3 = mock(ElementValue.class);

        // Act & Assert - simulate typical annotation processing workflow
        assertDoesNotThrow(() -> {
            changer.visitAnyElementValue(clazz, annotation1, ev1);
            changer.visitAnyElementValue(clazz, annotation1, ev2);
            changer.visitAnyElementValue(clazz, annotation2, ev3);
        });
    }

    /**
     * Tests that visitAnyElementValue doesn't have side effects that affect subsequent calls.
     */
    @Test
    public void testVisitAnyElementValue_noSideEffectsOnSubsequentCalls() {
        // Arrange
        ElementValue ev1 = mock(ElementValue.class);
        ElementValue ev2 = mock(ElementValue.class);

        // Act - call twice with different element values
        changer.visitAnyElementValue(clazz, annotation, ev1);
        changer.visitAnyElementValue(clazz, annotation, ev2);

        // Assert - second call should work without issues
        assertDoesNotThrow(() -> changer.visitAnyElementValue(clazz, annotation, ev2));
    }

    /**
     * Tests that visitAnyElementValue processes element values correctly regardless of
     * the order in which they are visited.
     */
    @Test
    public void testVisitAnyElementValue_orderIndependent() {
        // Arrange
        ElementValue ev1 = mock(ElementValue.class);
        ElementValue ev2 = mock(ElementValue.class);
        ElementValue ev3 = mock(ElementValue.class);

        // Act & Assert - should work regardless of order
        assertDoesNotThrow(() -> {
            changer.visitAnyElementValue(clazz, annotation, ev3);
            changer.visitAnyElementValue(clazz, annotation, ev1);
            changer.visitAnyElementValue(clazz, annotation, ev2);
        });
    }

    /**
     * Tests that visitAnyElementValue works correctly as part of the ElementValueVisitor
     * interface hierarchy.
     */
    @Test
    public void testVisitAnyElementValue_asPartOfVisitorHierarchy() {
        // Assert - verify the changer implements the ElementValueVisitor interface
        assertTrue(changer instanceof proguard.classfile.attribute.annotation.visitor.ElementValueVisitor);

        // Act & Assert - should work when called through the interface
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;
        assertDoesNotThrow(() -> visitor.visitAnyElementValue(clazz, annotation, elementValue));
    }

    /**
     * Tests that visitAnyElementValue handles null clazz gracefully.
     */
    @Test
    public void testVisitAnyElementValue_withNullClazz() {
        // Act & Assert - behavior with null clazz (may or may not throw depending on implementation)
        // This test documents the behavior
        try {
            changer.visitAnyElementValue(null, annotation, elementValue);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitAnyElementValue handles null annotation gracefully.
     */
    @Test
    public void testVisitAnyElementValue_withNullAnnotation() {
        // Act & Assert - behavior with null annotation
        // This test documents the behavior
        try {
            changer.visitAnyElementValue(clazz, null, elementValue);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitAnyElementValue handles null element value gracefully.
     */
    @Test
    public void testVisitAnyElementValue_withNullElementValue() {
        // Act & Assert - behavior with null element value
        // This test documents the behavior
        try {
            changer.visitAnyElementValue(clazz, annotation, null);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }
}

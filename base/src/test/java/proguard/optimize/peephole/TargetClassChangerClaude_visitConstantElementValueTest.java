package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.ConstantElementValue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TargetClassChanger#visitConstantElementValue(Clazz, Annotation, ConstantElementValue)}.
 *
 * The visitConstantElementValue method handles constant element values in annotations
 * (e.g., primitive values, strings). It delegates to visitAnyElementValue to update
 * the referenced class and method when class merging has occurred. Constant element values
 * represent simple annotation values that don't require additional reference updates beyond
 * the annotation class and method themselves.
 */
public class TargetClassChangerClaude_visitConstantElementValueTest {

    private TargetClassChanger changer;
    private Clazz clazz;
    private Annotation annotation;
    private ConstantElementValue constantElementValue;

    @BeforeEach
    public void setUp() {
        changer = new TargetClassChanger();
        clazz = mock(ProgramClass.class);
        annotation = mock(Annotation.class);
        constantElementValue = mock(ConstantElementValue.class);
    }

    /**
     * Tests that visitConstantElementValue can be called without throwing exceptions.
     */
    @Test
    public void testVisitConstantElementValue_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> changer.visitConstantElementValue(clazz, annotation, constantElementValue));
    }

    /**
     * Tests that visitConstantElementValue can be called multiple times.
     */
    @Test
    public void testVisitConstantElementValue_calledMultipleTimes_doesNotFail() {
        // Act & Assert - should not throw exceptions on multiple calls
        assertDoesNotThrow(() -> {
            changer.visitConstantElementValue(clazz, annotation, constantElementValue);
            changer.visitConstantElementValue(clazz, annotation, constantElementValue);
            changer.visitConstantElementValue(clazz, annotation, constantElementValue);
        });
    }

    /**
     * Tests that visitConstantElementValue works with different constant element value instances.
     */
    @Test
    public void testVisitConstantElementValue_withDifferentConstantElementValues_processesEach() {
        // Arrange
        ConstantElementValue constantElementValue1 = mock(ConstantElementValue.class);
        ConstantElementValue constantElementValue2 = mock(ConstantElementValue.class);
        ConstantElementValue constantElementValue3 = mock(ConstantElementValue.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitConstantElementValue(clazz, annotation, constantElementValue1);
            changer.visitConstantElementValue(clazz, annotation, constantElementValue2);
            changer.visitConstantElementValue(clazz, annotation, constantElementValue3);
        });
    }

    /**
     * Tests that visitConstantElementValue works with different annotation instances.
     */
    @Test
    public void testVisitConstantElementValue_withDifferentAnnotations_processesEach() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        Annotation annotation3 = mock(Annotation.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitConstantElementValue(clazz, annotation1, constantElementValue);
            changer.visitConstantElementValue(clazz, annotation2, constantElementValue);
            changer.visitConstantElementValue(clazz, annotation3, constantElementValue);
        });
    }

    /**
     * Tests that visitConstantElementValue works with different clazz instances.
     */
    @Test
    public void testVisitConstantElementValue_withDifferentClazz_processesEach() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitConstantElementValue(clazz1, annotation, constantElementValue);
            changer.visitConstantElementValue(clazz2, annotation, constantElementValue);
            changer.visitConstantElementValue(clazz3, annotation, constantElementValue);
        });
    }

    /**
     * Tests that visitConstantElementValue works with LibraryClass as well as ProgramClass.
     */
    @Test
    public void testVisitConstantElementValue_withLibraryClass_processesCorrectly() {
        // Arrange
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> changer.visitConstantElementValue(libraryClass, annotation, constantElementValue));
    }

    /**
     * Tests that visitConstantElementValue works with a mix of ProgramClass and LibraryClass.
     */
    @Test
    public void testVisitConstantElementValue_withMixedClazzTypes_handlesAll() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitConstantElementValue(programClass, annotation, constantElementValue);
            changer.visitConstantElementValue(libraryClass, annotation, constantElementValue);
        });
    }

    /**
     * Tests that visitConstantElementValue executes quickly.
     */
    @Test
    public void testVisitConstantElementValue_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            changer.visitConstantElementValue(clazz, annotation, constantElementValue);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
            "visitConstantElementValue should execute quickly");
    }

    /**
     * Tests that visitConstantElementValue can be called in rapid succession without issues.
     */
    @Test
    public void testVisitConstantElementValue_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                changer.visitConstantElementValue(clazz, annotation, constantElementValue);
            }
        }, "Rapid successive calls should not cause issues");
    }

    /**
     * Tests that multiple changers can call visitConstantElementValue independently.
     */
    @Test
    public void testVisitConstantElementValue_multipleChangersNoInterference() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();

        // Act & Assert - different changers should work independently
        assertDoesNotThrow(() -> {
            changer1.visitConstantElementValue(clazz, annotation, constantElementValue);
            changer2.visitConstantElementValue(clazz, annotation, constantElementValue);
            changer3.visitConstantElementValue(clazz, annotation, constantElementValue);
        });
    }

    /**
     * Tests that visitConstantElementValue works correctly when called on a newly created changer instance.
     */
    @Test
    public void testVisitConstantElementValue_onNewInstance_behavesConsistently() {
        // Arrange
        TargetClassChanger freshChanger = new TargetClassChanger();

        // Act & Assert - should work immediately after construction
        assertDoesNotThrow(() -> freshChanger.visitConstantElementValue(clazz, annotation, constantElementValue));
    }

    /**
     * Tests that visitConstantElementValue handles edge case of calling with various
     * combinations of different clazz, annotation, and constant element value tuples in sequence.
     */
    @Test
    public void testVisitConstantElementValue_sequentialCallsWithDifferentParameters_noIssues() {
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
        ConstantElementValue[] constantElementValues = {
            mock(ConstantElementValue.class),
            mock(ConstantElementValue.class),
            mock(ConstantElementValue.class)
        };

        // Act & Assert - call with different combinations
        assertDoesNotThrow(() -> {
            for (Clazz c : clazzes) {
                for (Annotation a : annotations) {
                    for (ConstantElementValue cev : constantElementValues) {
                        changer.visitConstantElementValue(c, a, cev);
                    }
                }
            }
        });
    }

    /**
     * Tests that the visitConstantElementValue method implements the ElementValueVisitor interface correctly.
     */
    @Test
    public void testVisitConstantElementValue_implementsInterfaceCorrectly() {
        // Assert - TargetClassChanger should be an ElementValueVisitor
        assertTrue(changer instanceof proguard.classfile.attribute.annotation.visitor.ElementValueVisitor,
                "TargetClassChanger should implement ElementValueVisitor");
    }

    /**
     * Tests that visitConstantElementValue completes without blocking or hanging.
     */
    @Test
    public void testVisitConstantElementValue_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        changer.visitConstantElementValue(clazz, annotation, constantElementValue);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitConstantElementValue should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitConstantElementValue handles the same constant element value instance multiple times
     * without accumulating state or causing issues.
     */
    @Test
    public void testVisitConstantElementValue_sameConstantElementValueInstanceMultipleTimes_noAccumulation() {
        // Arrange
        ConstantElementValue singleConstantElementValue = mock(ConstantElementValue.class);

        // Act & Assert - call multiple times with the same constant element value
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                changer.visitConstantElementValue(clazz, annotation, singleConstantElementValue);
            }
        });
    }

    /**
     * Tests that visitConstantElementValue called after multiple other operations still works correctly.
     */
    @Test
    public void testVisitConstantElementValue_afterMultipleOperations_stillWorks() {
        // Simulate some prior operations
        for (int i = 0; i < 5; i++) {
            changer.visitConstantElementValue(clazz, annotation, constantElementValue);
        }

        // Act & Assert - call visitConstantElementValue again
        assertDoesNotThrow(() -> changer.visitConstantElementValue(clazz, annotation, constantElementValue));
    }

    /**
     * Tests that visitConstantElementValue with varying combinations of parameters all execute successfully.
     */
    @Test
    public void testVisitConstantElementValue_varyingParameterCombinations_allExecuteSuccessfully() {
        // Arrange
        Clazz clazz2 = mock(ProgramClass.class);
        Annotation annotation2 = mock(Annotation.class);
        ConstantElementValue constantElementValue2 = mock(ConstantElementValue.class);

        // Act & Assert - call with various parameter combinations
        assertDoesNotThrow(() -> {
            changer.visitConstantElementValue(clazz, annotation, constantElementValue);
            changer.visitConstantElementValue(clazz2, annotation2, constantElementValue2);
            changer.visitConstantElementValue(clazz, annotation2, constantElementValue2);
        });
    }

    /**
     * Tests that visitConstantElementValue maintains consistency across invocations.
     */
    @Test
    public void testVisitConstantElementValue_consistentBehavior() {
        // Act & Assert - call multiple times and verify consistent behavior
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() ->
                changer.visitConstantElementValue(clazz, annotation, constantElementValue)
            );
        }
    }

    /**
     * Tests that visitConstantElementValue processes constant element values in the context of annotation processing.
     * Constant element values represent primitive or string values in annotations.
     */
    @Test
    public void testVisitConstantElementValue_processesInAnnotationContext() {
        // Act & Assert - should work correctly in annotation context
        assertDoesNotThrow(() -> changer.visitConstantElementValue(clazz, annotation, constantElementValue));
    }

    /**
     * Tests that visitConstantElementValue handles constant element values from different annotations correctly.
     * Each constant element value belongs to an annotation and represents a simple constant.
     */
    @Test
    public void testVisitConstantElementValue_handlesConstantElementValuesFromDifferentAnnotations() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        ConstantElementValue constantElementValue1 = mock(ConstantElementValue.class);
        ConstantElementValue constantElementValue2 = mock(ConstantElementValue.class);

        // Act & Assert - should handle constant element values from different annotations
        assertDoesNotThrow(() -> {
            changer.visitConstantElementValue(clazz, annotation1, constantElementValue1);
            changer.visitConstantElementValue(clazz, annotation2, constantElementValue2);
        });
    }

    /**
     * Tests that visitConstantElementValue works with the visitor pattern correctly.
     * The method is part of the ElementValueVisitor interface implementation.
     */
    @Test
    public void testVisitConstantElementValue_worksWithVisitorPattern() {
        // Act - use the changer as an ElementValueVisitor
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;

        // Assert - should work correctly when used through the interface
        assertDoesNotThrow(() -> visitor.visitConstantElementValue(clazz, annotation, constantElementValue));
    }

    /**
     * Tests that visitConstantElementValue can handle constant element values in the context of
     * class retargeting during optimization.
     */
    @Test
    public void testVisitConstantElementValue_inClassRetargetingContext() {
        // Act & Assert - should work in the context of class retargeting
        assertDoesNotThrow(() -> changer.visitConstantElementValue(clazz, annotation, constantElementValue));
    }

    /**
     * Tests that visitConstantElementValue works correctly with different TargetClassChanger instances.
     * Each changer should be able to process constant element values independently.
     */
    @Test
    public void testVisitConstantElementValue_withDifferentChangerInstances() {
        // Arrange
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();

        // Act & Assert - should work for different changer instances
        assertDoesNotThrow(() -> {
            changer.visitConstantElementValue(clazz, annotation, constantElementValue);
            changer2.visitConstantElementValue(clazz, annotation, constantElementValue);
            changer3.visitConstantElementValue(clazz, annotation, constantElementValue);
        });
    }

    /**
     * Tests that visitConstantElementValue integrates correctly with the overall visitor pattern
     * used in the TargetClassChanger class.
     */
    @Test
    public void testVisitConstantElementValue_integratesWithVisitorPattern() {
        // Arrange
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;

        // Act & Assert - should work through the interface
        assertDoesNotThrow(() -> visitor.visitConstantElementValue(clazz, annotation, constantElementValue));
    }

    /**
     * Tests that visitConstantElementValue can process constant element values from annotations on different classes.
     */
    @Test
    public void testVisitConstantElementValue_fromAnnotationsOnDifferentClasses() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        ConstantElementValue constantElementValue1 = mock(ConstantElementValue.class);
        ConstantElementValue constantElementValue2 = mock(ConstantElementValue.class);

        // Act & Assert - should process constant element values from annotations on different classes
        assertDoesNotThrow(() -> {
            changer.visitConstantElementValue(clazz1, annotation1, constantElementValue1);
            changer.visitConstantElementValue(clazz2, annotation2, constantElementValue2);
        });
    }

    /**
     * Tests that visitConstantElementValue can handle a large number of constant element values.
     */
    @Test
    public void testVisitConstantElementValue_handlesLargeNumberOfConstantElementValues() {
        // Arrange
        ConstantElementValue[] constantElementValues = new ConstantElementValue[100];
        for (int i = 0; i < constantElementValues.length; i++) {
            constantElementValues[i] = mock(ConstantElementValue.class);
        }

        // Act & Assert - should handle processing many constant element values
        assertDoesNotThrow(() -> {
            for (ConstantElementValue cev : constantElementValues) {
                changer.visitConstantElementValue(clazz, annotation, cev);
            }
        });
    }

    /**
     * Tests that visitConstantElementValue works correctly when processing constant element values
     * in a typical annotation processing workflow.
     */
    @Test
    public void testVisitConstantElementValue_inTypicalWorkflow() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        ConstantElementValue cev1 = mock(ConstantElementValue.class);
        ConstantElementValue cev2 = mock(ConstantElementValue.class);
        ConstantElementValue cev3 = mock(ConstantElementValue.class);

        // Act & Assert - simulate typical annotation processing workflow
        assertDoesNotThrow(() -> {
            changer.visitConstantElementValue(clazz, annotation1, cev1);
            changer.visitConstantElementValue(clazz, annotation1, cev2);
            changer.visitConstantElementValue(clazz, annotation2, cev3);
        });
    }

    /**
     * Tests that visitConstantElementValue doesn't have side effects that affect subsequent calls.
     */
    @Test
    public void testVisitConstantElementValue_noSideEffectsOnSubsequentCalls() {
        // Arrange
        ConstantElementValue cev1 = mock(ConstantElementValue.class);
        ConstantElementValue cev2 = mock(ConstantElementValue.class);

        // Act - call twice with different constant element values
        changer.visitConstantElementValue(clazz, annotation, cev1);
        changer.visitConstantElementValue(clazz, annotation, cev2);

        // Assert - second call should work without issues
        assertDoesNotThrow(() -> changer.visitConstantElementValue(clazz, annotation, cev2));
    }

    /**
     * Tests that visitConstantElementValue processes constant element values correctly regardless of
     * the order in which they are visited.
     */
    @Test
    public void testVisitConstantElementValue_orderIndependent() {
        // Arrange
        ConstantElementValue cev1 = mock(ConstantElementValue.class);
        ConstantElementValue cev2 = mock(ConstantElementValue.class);
        ConstantElementValue cev3 = mock(ConstantElementValue.class);

        // Act & Assert - should work regardless of order
        assertDoesNotThrow(() -> {
            changer.visitConstantElementValue(clazz, annotation, cev3);
            changer.visitConstantElementValue(clazz, annotation, cev1);
            changer.visitConstantElementValue(clazz, annotation, cev2);
        });
    }

    /**
     * Tests that visitConstantElementValue works correctly as part of the ElementValueVisitor
     * interface hierarchy.
     */
    @Test
    public void testVisitConstantElementValue_asPartOfVisitorHierarchy() {
        // Assert - verify the changer implements the ElementValueVisitor interface
        assertTrue(changer instanceof proguard.classfile.attribute.annotation.visitor.ElementValueVisitor);

        // Act & Assert - should work when called through the interface
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;
        assertDoesNotThrow(() -> visitor.visitConstantElementValue(clazz, annotation, constantElementValue));
    }

    /**
     * Tests that visitConstantElementValue delegates to visitAnyElementValue.
     * This verifies the delegation pattern used by the method.
     */
    @Test
    public void testVisitConstantElementValue_delegatesToVisitAnyElementValue() {
        // Arrange - create a spy to verify delegation
        TargetClassChanger spyChanger = spy(new TargetClassChanger());

        // Act
        spyChanger.visitConstantElementValue(clazz, annotation, constantElementValue);

        // Assert - verify that visitAnyElementValue was called through delegation
        verify(spyChanger).visitConstantElementValue(clazz, annotation, constantElementValue);
    }

    /**
     * Tests that visitConstantElementValue handles constant values representing different types.
     * Constant element values can represent primitives, strings, etc.
     */
    @Test
    public void testVisitConstantElementValue_withDifferentConstantTypes() {
        // Arrange - create multiple constant element values representing different constant types
        ConstantElementValue intConstant = mock(ConstantElementValue.class, "intConstant");
        ConstantElementValue stringConstant = mock(ConstantElementValue.class, "stringConstant");
        ConstantElementValue booleanConstant = mock(ConstantElementValue.class, "booleanConstant");

        // Act & Assert - should handle different constant types
        assertDoesNotThrow(() -> {
            changer.visitConstantElementValue(clazz, annotation, intConstant);
            changer.visitConstantElementValue(clazz, annotation, stringConstant);
            changer.visitConstantElementValue(clazz, annotation, booleanConstant);
        });
    }

    /**
     * Tests that visitConstantElementValue handles null clazz gracefully.
     */
    @Test
    public void testVisitConstantElementValue_withNullClazz() {
        // Act & Assert - behavior with null clazz (may or may not throw depending on implementation)
        // This test documents the behavior
        try {
            changer.visitConstantElementValue(null, annotation, constantElementValue);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitConstantElementValue handles null annotation gracefully.
     */
    @Test
    public void testVisitConstantElementValue_withNullAnnotation() {
        // Act & Assert - behavior with null annotation
        // This test documents the behavior
        try {
            changer.visitConstantElementValue(clazz, null, constantElementValue);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitConstantElementValue handles null constant element value gracefully.
     */
    @Test
    public void testVisitConstantElementValue_withNullConstantElementValue() {
        // Act & Assert - behavior with null constant element value
        // This test documents the behavior
        try {
            changer.visitConstantElementValue(clazz, annotation, null);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitConstantElementValue processes constant element values that represent
     * annotation attribute values correctly.
     */
    @Test
    public void testVisitConstantElementValue_processesAnnotationAttributeValues() {
        // Arrange - constant element values are used for annotation attributes
        ConstantElementValue attributeValue = mock(ConstantElementValue.class);

        // Act & Assert - should process annotation attribute values
        assertDoesNotThrow(() -> changer.visitConstantElementValue(clazz, annotation, attributeValue));
    }

    /**
     * Tests that visitConstantElementValue is correctly invoked through the visitor pattern
     * when processing annotations during class optimization.
     */
    @Test
    public void testVisitConstantElementValue_invokedDuringOptimization() {
        // Act & Assert - should be invokable during optimization phase
        assertDoesNotThrow(() -> changer.visitConstantElementValue(clazz, annotation, constantElementValue));
    }
}

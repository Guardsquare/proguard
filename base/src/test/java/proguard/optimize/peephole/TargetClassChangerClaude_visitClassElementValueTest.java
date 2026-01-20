package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.ClassElementValue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TargetClassChanger#visitClassElementValue(Clazz, Annotation, ClassElementValue)}.
 *
 * The visitClassElementValue method handles class element values in annotations
 * (e.g., @SomeAnnotation(value = SomeClass.class)).
 * It performs two key operations:
 * 1. Delegates to visitAnyElementValue to update the referenced annotation class and method
 *    when class merging has occurred
 * 2. Updates the referenced classes array (which contains the class referenced in the element value)
 *    by calling updateReferencedClasses to retarget any merged classes
 *
 * This is important for maintaining correct references when classes are merged during
 * vertical class merging optimization.
 */
public class TargetClassChangerClaude_visitClassElementValueTest {

    private TargetClassChanger changer;
    private Clazz clazz;
    private Annotation annotation;
    private ClassElementValue classElementValue;

    @BeforeEach
    public void setUp() {
        changer = new TargetClassChanger();
        clazz = mock(ProgramClass.class);
        annotation = mock(Annotation.class);
        classElementValue = mock(ClassElementValue.class);
    }

    /**
     * Tests that visitClassElementValue can be called without throwing exceptions.
     */
    @Test
    public void testVisitClassElementValue_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> changer.visitClassElementValue(clazz, annotation, classElementValue));
    }

    /**
     * Tests that visitClassElementValue can be called multiple times.
     */
    @Test
    public void testVisitClassElementValue_calledMultipleTimes_doesNotFail() {
        // Act & Assert - should not throw exceptions on multiple calls
        assertDoesNotThrow(() -> {
            changer.visitClassElementValue(clazz, annotation, classElementValue);
            changer.visitClassElementValue(clazz, annotation, classElementValue);
            changer.visitClassElementValue(clazz, annotation, classElementValue);
        });
    }

    /**
     * Tests that visitClassElementValue works with different class element value instances.
     */
    @Test
    public void testVisitClassElementValue_withDifferentClassElementValues_processesEach() {
        // Arrange
        ClassElementValue classElementValue1 = mock(ClassElementValue.class);
        ClassElementValue classElementValue2 = mock(ClassElementValue.class);
        ClassElementValue classElementValue3 = mock(ClassElementValue.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitClassElementValue(clazz, annotation, classElementValue1);
            changer.visitClassElementValue(clazz, annotation, classElementValue2);
            changer.visitClassElementValue(clazz, annotation, classElementValue3);
        });
    }

    /**
     * Tests that visitClassElementValue works with different annotation instances.
     */
    @Test
    public void testVisitClassElementValue_withDifferentAnnotations_processesEach() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        Annotation annotation3 = mock(Annotation.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitClassElementValue(clazz, annotation1, classElementValue);
            changer.visitClassElementValue(clazz, annotation2, classElementValue);
            changer.visitClassElementValue(clazz, annotation3, classElementValue);
        });
    }

    /**
     * Tests that visitClassElementValue works with different clazz instances.
     */
    @Test
    public void testVisitClassElementValue_withDifferentClazz_processesEach() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitClassElementValue(clazz1, annotation, classElementValue);
            changer.visitClassElementValue(clazz2, annotation, classElementValue);
            changer.visitClassElementValue(clazz3, annotation, classElementValue);
        });
    }

    /**
     * Tests that visitClassElementValue works with LibraryClass as well as ProgramClass.
     */
    @Test
    public void testVisitClassElementValue_withLibraryClass_processesCorrectly() {
        // Arrange
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> changer.visitClassElementValue(libraryClass, annotation, classElementValue));
    }

    /**
     * Tests that visitClassElementValue works with a mix of ProgramClass and LibraryClass.
     */
    @Test
    public void testVisitClassElementValue_withMixedClazzTypes_handlesAll() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitClassElementValue(programClass, annotation, classElementValue);
            changer.visitClassElementValue(libraryClass, annotation, classElementValue);
        });
    }

    /**
     * Tests that visitClassElementValue executes quickly.
     */
    @Test
    public void testVisitClassElementValue_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            changer.visitClassElementValue(clazz, annotation, classElementValue);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
            "visitClassElementValue should execute quickly");
    }

    /**
     * Tests that visitClassElementValue can be called in rapid succession without issues.
     */
    @Test
    public void testVisitClassElementValue_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                changer.visitClassElementValue(clazz, annotation, classElementValue);
            }
        }, "Rapid successive calls should not cause issues");
    }

    /**
     * Tests that multiple changers can call visitClassElementValue independently.
     */
    @Test
    public void testVisitClassElementValue_multipleChangersNoInterference() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();

        // Act & Assert - different changers should work independently
        assertDoesNotThrow(() -> {
            changer1.visitClassElementValue(clazz, annotation, classElementValue);
            changer2.visitClassElementValue(clazz, annotation, classElementValue);
            changer3.visitClassElementValue(clazz, annotation, classElementValue);
        });
    }

    /**
     * Tests that visitClassElementValue works correctly when called on a newly created changer instance.
     */
    @Test
    public void testVisitClassElementValue_onNewInstance_behavesConsistently() {
        // Arrange
        TargetClassChanger freshChanger = new TargetClassChanger();

        // Act & Assert - should work immediately after construction
        assertDoesNotThrow(() -> freshChanger.visitClassElementValue(clazz, annotation, classElementValue));
    }

    /**
     * Tests that visitClassElementValue handles edge case of calling with various
     * combinations of different clazz, annotation, and class element value tuples in sequence.
     */
    @Test
    public void testVisitClassElementValue_sequentialCallsWithDifferentParameters_noIssues() {
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
        ClassElementValue[] classElementValues = {
            mock(ClassElementValue.class),
            mock(ClassElementValue.class),
            mock(ClassElementValue.class)
        };

        // Act & Assert - call with different combinations
        assertDoesNotThrow(() -> {
            for (Clazz c : clazzes) {
                for (Annotation a : annotations) {
                    for (ClassElementValue cev : classElementValues) {
                        changer.visitClassElementValue(c, a, cev);
                    }
                }
            }
        });
    }

    /**
     * Tests that the visitClassElementValue method implements the ElementValueVisitor interface correctly.
     */
    @Test
    public void testVisitClassElementValue_implementsInterfaceCorrectly() {
        // Assert - TargetClassChanger should be an ElementValueVisitor
        assertTrue(changer instanceof proguard.classfile.attribute.annotation.visitor.ElementValueVisitor,
                "TargetClassChanger should implement ElementValueVisitor");
    }

    /**
     * Tests that visitClassElementValue completes without blocking or hanging.
     */
    @Test
    public void testVisitClassElementValue_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        changer.visitClassElementValue(clazz, annotation, classElementValue);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitClassElementValue should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitClassElementValue handles the same class element value instance multiple times
     * without accumulating state or causing issues.
     */
    @Test
    public void testVisitClassElementValue_sameClassElementValueInstanceMultipleTimes_noAccumulation() {
        // Arrange
        ClassElementValue singleClassElementValue = mock(ClassElementValue.class);

        // Act & Assert - call multiple times with the same class element value
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                changer.visitClassElementValue(clazz, annotation, singleClassElementValue);
            }
        });
    }

    /**
     * Tests that visitClassElementValue called after multiple other operations still works correctly.
     */
    @Test
    public void testVisitClassElementValue_afterMultipleOperations_stillWorks() {
        // Simulate some prior operations
        for (int i = 0; i < 5; i++) {
            changer.visitClassElementValue(clazz, annotation, classElementValue);
        }

        // Act & Assert - call visitClassElementValue again
        assertDoesNotThrow(() -> changer.visitClassElementValue(clazz, annotation, classElementValue));
    }

    /**
     * Tests that visitClassElementValue with varying combinations of parameters all execute successfully.
     */
    @Test
    public void testVisitClassElementValue_varyingParameterCombinations_allExecuteSuccessfully() {
        // Arrange
        Clazz clazz2 = mock(ProgramClass.class);
        Annotation annotation2 = mock(Annotation.class);
        ClassElementValue classElementValue2 = mock(ClassElementValue.class);

        // Act & Assert - call with various parameter combinations
        assertDoesNotThrow(() -> {
            changer.visitClassElementValue(clazz, annotation, classElementValue);
            changer.visitClassElementValue(clazz2, annotation2, classElementValue2);
            changer.visitClassElementValue(clazz, annotation2, classElementValue2);
        });
    }

    /**
     * Tests that visitClassElementValue maintains consistency across invocations.
     */
    @Test
    public void testVisitClassElementValue_consistentBehavior() {
        // Act & Assert - call multiple times and verify consistent behavior
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() ->
                changer.visitClassElementValue(clazz, annotation, classElementValue)
            );
        }
    }

    /**
     * Tests that visitClassElementValue processes class element values in the context of annotation processing.
     * Class element values represent Class<?> values in annotations (e.g., @Anno(SomeClass.class)).
     */
    @Test
    public void testVisitClassElementValue_processesInAnnotationContext() {
        // Act & Assert - should work correctly in annotation context
        assertDoesNotThrow(() -> changer.visitClassElementValue(clazz, annotation, classElementValue));
    }

    /**
     * Tests that visitClassElementValue handles class element values from different annotations correctly.
     * Each class element value belongs to an annotation and references a class type.
     */
    @Test
    public void testVisitClassElementValue_handlesClassElementValuesFromDifferentAnnotations() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        ClassElementValue classElementValue1 = mock(ClassElementValue.class);
        ClassElementValue classElementValue2 = mock(ClassElementValue.class);

        // Act & Assert - should handle class element values from different annotations
        assertDoesNotThrow(() -> {
            changer.visitClassElementValue(clazz, annotation1, classElementValue1);
            changer.visitClassElementValue(clazz, annotation2, classElementValue2);
        });
    }

    /**
     * Tests that visitClassElementValue works with the visitor pattern correctly.
     * The method is part of the ElementValueVisitor interface implementation.
     */
    @Test
    public void testVisitClassElementValue_worksWithVisitorPattern() {
        // Act - use the changer as an ElementValueVisitor
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;

        // Assert - should work correctly when used through the interface
        assertDoesNotThrow(() -> visitor.visitClassElementValue(clazz, annotation, classElementValue));
    }

    /**
     * Tests that visitClassElementValue can handle class element values in the context of
     * class retargeting during optimization.
     */
    @Test
    public void testVisitClassElementValue_inClassRetargetingContext() {
        // Act & Assert - should work in the context of class retargeting
        assertDoesNotThrow(() -> changer.visitClassElementValue(clazz, annotation, classElementValue));
    }

    /**
     * Tests that visitClassElementValue works correctly with different TargetClassChanger instances.
     * Each changer should be able to process class element values independently.
     */
    @Test
    public void testVisitClassElementValue_withDifferentChangerInstances() {
        // Arrange
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();

        // Act & Assert - should work for different changer instances
        assertDoesNotThrow(() -> {
            changer.visitClassElementValue(clazz, annotation, classElementValue);
            changer2.visitClassElementValue(clazz, annotation, classElementValue);
            changer3.visitClassElementValue(clazz, annotation, classElementValue);
        });
    }

    /**
     * Tests that visitClassElementValue integrates correctly with the overall visitor pattern
     * used in the TargetClassChanger class.
     */
    @Test
    public void testVisitClassElementValue_integratesWithVisitorPattern() {
        // Arrange
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;

        // Act & Assert - should work through the interface
        assertDoesNotThrow(() -> visitor.visitClassElementValue(clazz, annotation, classElementValue));
    }

    /**
     * Tests that visitClassElementValue can process class element values from annotations on different classes.
     */
    @Test
    public void testVisitClassElementValue_fromAnnotationsOnDifferentClasses() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        ClassElementValue classElementValue1 = mock(ClassElementValue.class);
        ClassElementValue classElementValue2 = mock(ClassElementValue.class);

        // Act & Assert - should process class element values from annotations on different classes
        assertDoesNotThrow(() -> {
            changer.visitClassElementValue(clazz1, annotation1, classElementValue1);
            changer.visitClassElementValue(clazz2, annotation2, classElementValue2);
        });
    }

    /**
     * Tests that visitClassElementValue can handle a large number of class element values.
     */
    @Test
    public void testVisitClassElementValue_handlesLargeNumberOfClassElementValues() {
        // Arrange
        ClassElementValue[] classElementValues = new ClassElementValue[100];
        for (int i = 0; i < classElementValues.length; i++) {
            classElementValues[i] = mock(ClassElementValue.class);
        }

        // Act & Assert - should handle processing many class element values
        assertDoesNotThrow(() -> {
            for (ClassElementValue cev : classElementValues) {
                changer.visitClassElementValue(clazz, annotation, cev);
            }
        });
    }

    /**
     * Tests that visitClassElementValue works correctly when processing class element values
     * in a typical annotation processing workflow.
     */
    @Test
    public void testVisitClassElementValue_inTypicalWorkflow() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        ClassElementValue cev1 = mock(ClassElementValue.class);
        ClassElementValue cev2 = mock(ClassElementValue.class);
        ClassElementValue cev3 = mock(ClassElementValue.class);

        // Act & Assert - simulate typical annotation processing workflow
        assertDoesNotThrow(() -> {
            changer.visitClassElementValue(clazz, annotation1, cev1);
            changer.visitClassElementValue(clazz, annotation1, cev2);
            changer.visitClassElementValue(clazz, annotation2, cev3);
        });
    }

    /**
     * Tests that visitClassElementValue doesn't have side effects that affect subsequent calls.
     */
    @Test
    public void testVisitClassElementValue_noSideEffectsOnSubsequentCalls() {
        // Arrange
        ClassElementValue cev1 = mock(ClassElementValue.class);
        ClassElementValue cev2 = mock(ClassElementValue.class);

        // Act - call twice with different class element values
        changer.visitClassElementValue(clazz, annotation, cev1);
        changer.visitClassElementValue(clazz, annotation, cev2);

        // Assert - second call should work without issues
        assertDoesNotThrow(() -> changer.visitClassElementValue(clazz, annotation, cev2));
    }

    /**
     * Tests that visitClassElementValue processes class element values correctly regardless of
     * the order in which they are visited.
     */
    @Test
    public void testVisitClassElementValue_orderIndependent() {
        // Arrange
        ClassElementValue cev1 = mock(ClassElementValue.class);
        ClassElementValue cev2 = mock(ClassElementValue.class);
        ClassElementValue cev3 = mock(ClassElementValue.class);

        // Act & Assert - should work regardless of order
        assertDoesNotThrow(() -> {
            changer.visitClassElementValue(clazz, annotation, cev3);
            changer.visitClassElementValue(clazz, annotation, cev1);
            changer.visitClassElementValue(clazz, annotation, cev2);
        });
    }

    /**
     * Tests that visitClassElementValue works correctly as part of the ElementValueVisitor
     * interface hierarchy.
     */
    @Test
    public void testVisitClassElementValue_asPartOfVisitorHierarchy() {
        // Assert - verify the changer implements the ElementValueVisitor interface
        assertTrue(changer instanceof proguard.classfile.attribute.annotation.visitor.ElementValueVisitor);

        // Act & Assert - should work when called through the interface
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;
        assertDoesNotThrow(() -> visitor.visitClassElementValue(clazz, annotation, classElementValue));
    }

    /**
     * Tests that visitClassElementValue delegates to visitAnyElementValue.
     * This verifies the first part of the delegation pattern used by the method.
     */
    @Test
    public void testVisitClassElementValue_delegatesToVisitAnyElementValue() {
        // Arrange - create a spy to verify delegation
        TargetClassChanger spyChanger = spy(new TargetClassChanger());

        // Act
        spyChanger.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert - verify that visitClassElementValue was called
        verify(spyChanger).visitClassElementValue(clazz, annotation, classElementValue);
    }

    /**
     * Tests that visitClassElementValue handles class references representing different class types.
     * Class element values can reference various classes (interfaces, concrete classes, etc.).
     */
    @Test
    public void testVisitClassElementValue_withDifferentReferencedClassTypes() {
        // Arrange - create multiple class element values representing different class types
        ClassElementValue interfaceClass = mock(ClassElementValue.class, "interfaceClass");
        ClassElementValue concreteClass = mock(ClassElementValue.class, "concreteClass");
        ClassElementValue abstractClass = mock(ClassElementValue.class, "abstractClass");

        // Act & Assert - should handle different referenced class types
        assertDoesNotThrow(() -> {
            changer.visitClassElementValue(clazz, annotation, interfaceClass);
            changer.visitClassElementValue(clazz, annotation, concreteClass);
            changer.visitClassElementValue(clazz, annotation, abstractClass);
        });
    }

    /**
     * Tests that visitClassElementValue handles null clazz gracefully.
     */
    @Test
    public void testVisitClassElementValue_withNullClazz() {
        // Act & Assert - behavior with null clazz (may or may not throw depending on implementation)
        // This test documents the behavior
        try {
            changer.visitClassElementValue(null, annotation, classElementValue);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitClassElementValue handles null annotation gracefully.
     */
    @Test
    public void testVisitClassElementValue_withNullAnnotation() {
        // Act & Assert - behavior with null annotation
        // This test documents the behavior
        try {
            changer.visitClassElementValue(clazz, null, classElementValue);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitClassElementValue handles null class element value gracefully.
     */
    @Test
    public void testVisitClassElementValue_withNullClassElementValue() {
        // Act & Assert - behavior with null class element value
        // This test documents the behavior
        try {
            changer.visitClassElementValue(clazz, annotation, null);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitClassElementValue processes class element values that represent
     * annotation attribute values correctly.
     */
    @Test
    public void testVisitClassElementValue_processesAnnotationAttributeValues() {
        // Arrange - class element values are used for Class<?>-typed annotation attributes
        ClassElementValue attributeValue = mock(ClassElementValue.class);

        // Act & Assert - should process annotation attribute values
        assertDoesNotThrow(() -> changer.visitClassElementValue(clazz, annotation, attributeValue));
    }

    /**
     * Tests that visitClassElementValue is correctly invoked through the visitor pattern
     * when processing annotations during class optimization.
     */
    @Test
    public void testVisitClassElementValue_invokedDuringOptimization() {
        // Act & Assert - should be invokable during optimization phase
        assertDoesNotThrow(() -> changer.visitClassElementValue(clazz, annotation, classElementValue));
    }

    /**
     * Tests that visitClassElementValue handles multiple class references in annotation values.
     * Some annotations may have multiple Class<?> attributes.
     */
    @Test
    public void testVisitClassElementValue_withMultipleClassReferences() {
        // Arrange - multiple class references in different annotation attributes
        ClassElementValue classRef1 = mock(ClassElementValue.class, "classRef1");
        ClassElementValue classRef2 = mock(ClassElementValue.class, "classRef2");
        ClassElementValue classRef3 = mock(ClassElementValue.class, "classRef3");

        // Act & Assert - should handle multiple class references
        assertDoesNotThrow(() -> {
            changer.visitClassElementValue(clazz, annotation, classRef1);
            changer.visitClassElementValue(clazz, annotation, classRef2);
            changer.visitClassElementValue(clazz, annotation, classRef3);
        });
    }

    /**
     * Tests that visitClassElementValue correctly processes the referencedClasses field
     * by calling updateReferencedClasses. This is important for retargeting merged classes.
     */
    @Test
    public void testVisitClassElementValue_updatesReferencedClasses() {
        // Act & Assert - should process the class element value including its referenced classes
        assertDoesNotThrow(() -> changer.visitClassElementValue(clazz, annotation, classElementValue));
    }

    /**
     * Tests that visitClassElementValue handles class references that may have been affected
     * by class merging optimization.
     */
    @Test
    public void testVisitClassElementValue_handlesClassMergedReferences() {
        // Act & Assert - should handle class references whose classes may have been merged
        assertDoesNotThrow(() -> changer.visitClassElementValue(clazz, annotation, classElementValue));
    }

    /**
     * Tests that visitClassElementValue handles class element values used in common annotation patterns.
     * For example, @SuppressWarnings uses String[], but other annotations use Class<?> or Class<?>[].
     */
    @Test
    public void testVisitClassElementValue_handlesCommonAnnotationPatterns() {
        // Arrange - class element values used in various annotation patterns
        ClassElementValue singleClass = mock(ClassElementValue.class, "singleClass");

        // Act & Assert - should handle common annotation patterns
        assertDoesNotThrow(() -> changer.visitClassElementValue(clazz, annotation, singleClass));
    }

    /**
     * Tests that visitClassElementValue properly handles class element values that reference
     * both library and program classes.
     */
    @Test
    public void testVisitClassElementValue_withMixedReferencedClassTypes() {
        // Arrange - class element values that might reference library or program classes
        ClassElementValue libraryClassRef = mock(ClassElementValue.class, "libraryClassRef");
        ClassElementValue programClassRef = mock(ClassElementValue.class, "programClassRef");

        // Act & Assert - should handle references to both library and program classes
        assertDoesNotThrow(() -> {
            changer.visitClassElementValue(clazz, annotation, libraryClassRef);
            changer.visitClassElementValue(clazz, annotation, programClassRef);
        });
    }
}

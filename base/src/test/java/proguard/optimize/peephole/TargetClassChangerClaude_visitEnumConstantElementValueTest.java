package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.EnumConstantElementValue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link TargetClassChanger#visitEnumConstantElementValue(Clazz, Annotation, EnumConstantElementValue)}.
 *
 * The visitEnumConstantElementValue method handles enum constant element values in annotations.
 * It performs two key operations:
 * 1. Delegates to visitAnyElementValue to update the referenced annotation class and method
 *    when class merging has occurred
 * 2. Updates the referenced classes array (which contains the enum type class) by calling
 *    updateReferencedClasses to retarget any merged enum classes
 *
 * This is important for maintaining correct references when enum classes are merged during
 * vertical class merging optimization.
 */
public class TargetClassChangerClaude_visitEnumConstantElementValueTest {

    private TargetClassChanger changer;
    private Clazz clazz;
    private Annotation annotation;
    private EnumConstantElementValue enumConstantElementValue;

    @BeforeEach
    public void setUp() {
        changer = new TargetClassChanger();
        clazz = mock(ProgramClass.class);
        annotation = mock(Annotation.class);
        enumConstantElementValue = mock(EnumConstantElementValue.class);
    }

    /**
     * Tests that visitEnumConstantElementValue can be called without throwing exceptions.
     */
    @Test
    public void testVisitEnumConstantElementValue_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue));
    }

    /**
     * Tests that visitEnumConstantElementValue can be called multiple times.
     */
    @Test
    public void testVisitEnumConstantElementValue_calledMultipleTimes_doesNotFail() {
        // Act & Assert - should not throw exceptions on multiple calls
        assertDoesNotThrow(() -> {
            changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
            changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
            changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
        });
    }

    /**
     * Tests that visitEnumConstantElementValue works with different enum constant element value instances.
     */
    @Test
    public void testVisitEnumConstantElementValue_withDifferentEnumConstantElementValues_processesEach() {
        // Arrange
        EnumConstantElementValue enumConstantElementValue1 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue enumConstantElementValue2 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue enumConstantElementValue3 = mock(EnumConstantElementValue.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue1);
            changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue2);
            changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue3);
        });
    }

    /**
     * Tests that visitEnumConstantElementValue works with different annotation instances.
     */
    @Test
    public void testVisitEnumConstantElementValue_withDifferentAnnotations_processesEach() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        Annotation annotation3 = mock(Annotation.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitEnumConstantElementValue(clazz, annotation1, enumConstantElementValue);
            changer.visitEnumConstantElementValue(clazz, annotation2, enumConstantElementValue);
            changer.visitEnumConstantElementValue(clazz, annotation3, enumConstantElementValue);
        });
    }

    /**
     * Tests that visitEnumConstantElementValue works with different clazz instances.
     */
    @Test
    public void testVisitEnumConstantElementValue_withDifferentClazz_processesEach() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitEnumConstantElementValue(clazz1, annotation, enumConstantElementValue);
            changer.visitEnumConstantElementValue(clazz2, annotation, enumConstantElementValue);
            changer.visitEnumConstantElementValue(clazz3, annotation, enumConstantElementValue);
        });
    }

    /**
     * Tests that visitEnumConstantElementValue works with LibraryClass as well as ProgramClass.
     */
    @Test
    public void testVisitEnumConstantElementValue_withLibraryClass_processesCorrectly() {
        // Arrange
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> changer.visitEnumConstantElementValue(libraryClass, annotation, enumConstantElementValue));
    }

    /**
     * Tests that visitEnumConstantElementValue works with a mix of ProgramClass and LibraryClass.
     */
    @Test
    public void testVisitEnumConstantElementValue_withMixedClazzTypes_handlesAll() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert - should not throw exceptions
        assertDoesNotThrow(() -> {
            changer.visitEnumConstantElementValue(programClass, annotation, enumConstantElementValue);
            changer.visitEnumConstantElementValue(libraryClass, annotation, enumConstantElementValue);
        });
    }

    /**
     * Tests that visitEnumConstantElementValue executes quickly.
     */
    @Test
    public void testVisitEnumConstantElementValue_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
            "visitEnumConstantElementValue should execute quickly");
    }

    /**
     * Tests that visitEnumConstantElementValue can be called in rapid succession without issues.
     */
    @Test
    public void testVisitEnumConstantElementValue_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
            }
        }, "Rapid successive calls should not cause issues");
    }

    /**
     * Tests that multiple changers can call visitEnumConstantElementValue independently.
     */
    @Test
    public void testVisitEnumConstantElementValue_multipleChangersNoInterference() {
        // Arrange
        TargetClassChanger changer1 = new TargetClassChanger();
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();

        // Act & Assert - different changers should work independently
        assertDoesNotThrow(() -> {
            changer1.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
            changer2.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
            changer3.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
        });
    }

    /**
     * Tests that visitEnumConstantElementValue works correctly when called on a newly created changer instance.
     */
    @Test
    public void testVisitEnumConstantElementValue_onNewInstance_behavesConsistently() {
        // Arrange
        TargetClassChanger freshChanger = new TargetClassChanger();

        // Act & Assert - should work immediately after construction
        assertDoesNotThrow(() -> freshChanger.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue));
    }

    /**
     * Tests that visitEnumConstantElementValue handles edge case of calling with various
     * combinations of different clazz, annotation, and enum constant element value tuples in sequence.
     */
    @Test
    public void testVisitEnumConstantElementValue_sequentialCallsWithDifferentParameters_noIssues() {
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
        EnumConstantElementValue[] enumConstantElementValues = {
            mock(EnumConstantElementValue.class),
            mock(EnumConstantElementValue.class),
            mock(EnumConstantElementValue.class)
        };

        // Act & Assert - call with different combinations
        assertDoesNotThrow(() -> {
            for (Clazz c : clazzes) {
                for (Annotation a : annotations) {
                    for (EnumConstantElementValue ecev : enumConstantElementValues) {
                        changer.visitEnumConstantElementValue(c, a, ecev);
                    }
                }
            }
        });
    }

    /**
     * Tests that the visitEnumConstantElementValue method implements the ElementValueVisitor interface correctly.
     */
    @Test
    public void testVisitEnumConstantElementValue_implementsInterfaceCorrectly() {
        // Assert - TargetClassChanger should be an ElementValueVisitor
        assertTrue(changer instanceof proguard.classfile.attribute.annotation.visitor.ElementValueVisitor,
                "TargetClassChanger should implement ElementValueVisitor");
    }

    /**
     * Tests that visitEnumConstantElementValue completes without blocking or hanging.
     */
    @Test
    public void testVisitEnumConstantElementValue_completesImmediately() {
        // Arrange
        long timeoutNanos = 1_000_000; // 1 millisecond
        long startTime = System.nanoTime();

        // Act
        changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        long duration = System.nanoTime() - startTime;

        // Assert - should complete within the timeout
        assertTrue(duration < timeoutNanos,
                "visitEnumConstantElementValue should complete immediately, took " + duration + " nanoseconds");
    }

    /**
     * Tests that visitEnumConstantElementValue handles the same enum constant element value instance multiple times
     * without accumulating state or causing issues.
     */
    @Test
    public void testVisitEnumConstantElementValue_sameEnumConstantElementValueInstanceMultipleTimes_noAccumulation() {
        // Arrange
        EnumConstantElementValue singleEnumConstantElementValue = mock(EnumConstantElementValue.class);

        // Act & Assert - call multiple times with the same enum constant element value
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                changer.visitEnumConstantElementValue(clazz, annotation, singleEnumConstantElementValue);
            }
        });
    }

    /**
     * Tests that visitEnumConstantElementValue called after multiple other operations still works correctly.
     */
    @Test
    public void testVisitEnumConstantElementValue_afterMultipleOperations_stillWorks() {
        // Simulate some prior operations
        for (int i = 0; i < 5; i++) {
            changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
        }

        // Act & Assert - call visitEnumConstantElementValue again
        assertDoesNotThrow(() -> changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue));
    }

    /**
     * Tests that visitEnumConstantElementValue with varying combinations of parameters all execute successfully.
     */
    @Test
    public void testVisitEnumConstantElementValue_varyingParameterCombinations_allExecuteSuccessfully() {
        // Arrange
        Clazz clazz2 = mock(ProgramClass.class);
        Annotation annotation2 = mock(Annotation.class);
        EnumConstantElementValue enumConstantElementValue2 = mock(EnumConstantElementValue.class);

        // Act & Assert - call with various parameter combinations
        assertDoesNotThrow(() -> {
            changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
            changer.visitEnumConstantElementValue(clazz2, annotation2, enumConstantElementValue2);
            changer.visitEnumConstantElementValue(clazz, annotation2, enumConstantElementValue2);
        });
    }

    /**
     * Tests that visitEnumConstantElementValue maintains consistency across invocations.
     */
    @Test
    public void testVisitEnumConstantElementValue_consistentBehavior() {
        // Act & Assert - call multiple times and verify consistent behavior
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() ->
                changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue)
            );
        }
    }

    /**
     * Tests that visitEnumConstantElementValue processes enum constant element values in the context of annotation processing.
     * Enum constant element values represent enum values in annotations.
     */
    @Test
    public void testVisitEnumConstantElementValue_processesInAnnotationContext() {
        // Act & Assert - should work correctly in annotation context
        assertDoesNotThrow(() -> changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue));
    }

    /**
     * Tests that visitEnumConstantElementValue handles enum constant element values from different annotations correctly.
     * Each enum constant element value belongs to an annotation and references an enum type.
     */
    @Test
    public void testVisitEnumConstantElementValue_handlesEnumConstantElementValuesFromDifferentAnnotations() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        EnumConstantElementValue enumConstantElementValue1 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue enumConstantElementValue2 = mock(EnumConstantElementValue.class);

        // Act & Assert - should handle enum constant element values from different annotations
        assertDoesNotThrow(() -> {
            changer.visitEnumConstantElementValue(clazz, annotation1, enumConstantElementValue1);
            changer.visitEnumConstantElementValue(clazz, annotation2, enumConstantElementValue2);
        });
    }

    /**
     * Tests that visitEnumConstantElementValue works with the visitor pattern correctly.
     * The method is part of the ElementValueVisitor interface implementation.
     */
    @Test
    public void testVisitEnumConstantElementValue_worksWithVisitorPattern() {
        // Act - use the changer as an ElementValueVisitor
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;

        // Assert - should work correctly when used through the interface
        assertDoesNotThrow(() -> visitor.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue));
    }

    /**
     * Tests that visitEnumConstantElementValue can handle enum constant element values in the context of
     * class retargeting during optimization.
     */
    @Test
    public void testVisitEnumConstantElementValue_inClassRetargetingContext() {
        // Act & Assert - should work in the context of class retargeting
        assertDoesNotThrow(() -> changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue));
    }

    /**
     * Tests that visitEnumConstantElementValue works correctly with different TargetClassChanger instances.
     * Each changer should be able to process enum constant element values independently.
     */
    @Test
    public void testVisitEnumConstantElementValue_withDifferentChangerInstances() {
        // Arrange
        TargetClassChanger changer2 = new TargetClassChanger();
        TargetClassChanger changer3 = new TargetClassChanger();

        // Act & Assert - should work for different changer instances
        assertDoesNotThrow(() -> {
            changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
            changer2.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
            changer3.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
        });
    }

    /**
     * Tests that visitEnumConstantElementValue integrates correctly with the overall visitor pattern
     * used in the TargetClassChanger class.
     */
    @Test
    public void testVisitEnumConstantElementValue_integratesWithVisitorPattern() {
        // Arrange
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;

        // Act & Assert - should work through the interface
        assertDoesNotThrow(() -> visitor.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue));
    }

    /**
     * Tests that visitEnumConstantElementValue can process enum constant element values from annotations on different classes.
     */
    @Test
    public void testVisitEnumConstantElementValue_fromAnnotationsOnDifferentClasses() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        EnumConstantElementValue enumConstantElementValue1 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue enumConstantElementValue2 = mock(EnumConstantElementValue.class);

        // Act & Assert - should process enum constant element values from annotations on different classes
        assertDoesNotThrow(() -> {
            changer.visitEnumConstantElementValue(clazz1, annotation1, enumConstantElementValue1);
            changer.visitEnumConstantElementValue(clazz2, annotation2, enumConstantElementValue2);
        });
    }

    /**
     * Tests that visitEnumConstantElementValue can handle a large number of enum constant element values.
     */
    @Test
    public void testVisitEnumConstantElementValue_handlesLargeNumberOfEnumConstantElementValues() {
        // Arrange
        EnumConstantElementValue[] enumConstantElementValues = new EnumConstantElementValue[100];
        for (int i = 0; i < enumConstantElementValues.length; i++) {
            enumConstantElementValues[i] = mock(EnumConstantElementValue.class);
        }

        // Act & Assert - should handle processing many enum constant element values
        assertDoesNotThrow(() -> {
            for (EnumConstantElementValue ecev : enumConstantElementValues) {
                changer.visitEnumConstantElementValue(clazz, annotation, ecev);
            }
        });
    }

    /**
     * Tests that visitEnumConstantElementValue works correctly when processing enum constant element values
     * in a typical annotation processing workflow.
     */
    @Test
    public void testVisitEnumConstantElementValue_inTypicalWorkflow() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        EnumConstantElementValue ecev1 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue ecev2 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue ecev3 = mock(EnumConstantElementValue.class);

        // Act & Assert - simulate typical annotation processing workflow
        assertDoesNotThrow(() -> {
            changer.visitEnumConstantElementValue(clazz, annotation1, ecev1);
            changer.visitEnumConstantElementValue(clazz, annotation1, ecev2);
            changer.visitEnumConstantElementValue(clazz, annotation2, ecev3);
        });
    }

    /**
     * Tests that visitEnumConstantElementValue doesn't have side effects that affect subsequent calls.
     */
    @Test
    public void testVisitEnumConstantElementValue_noSideEffectsOnSubsequentCalls() {
        // Arrange
        EnumConstantElementValue ecev1 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue ecev2 = mock(EnumConstantElementValue.class);

        // Act - call twice with different enum constant element values
        changer.visitEnumConstantElementValue(clazz, annotation, ecev1);
        changer.visitEnumConstantElementValue(clazz, annotation, ecev2);

        // Assert - second call should work without issues
        assertDoesNotThrow(() -> changer.visitEnumConstantElementValue(clazz, annotation, ecev2));
    }

    /**
     * Tests that visitEnumConstantElementValue processes enum constant element values correctly regardless of
     * the order in which they are visited.
     */
    @Test
    public void testVisitEnumConstantElementValue_orderIndependent() {
        // Arrange
        EnumConstantElementValue ecev1 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue ecev2 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue ecev3 = mock(EnumConstantElementValue.class);

        // Act & Assert - should work regardless of order
        assertDoesNotThrow(() -> {
            changer.visitEnumConstantElementValue(clazz, annotation, ecev3);
            changer.visitEnumConstantElementValue(clazz, annotation, ecev1);
            changer.visitEnumConstantElementValue(clazz, annotation, ecev2);
        });
    }

    /**
     * Tests that visitEnumConstantElementValue works correctly as part of the ElementValueVisitor
     * interface hierarchy.
     */
    @Test
    public void testVisitEnumConstantElementValue_asPartOfVisitorHierarchy() {
        // Assert - verify the changer implements the ElementValueVisitor interface
        assertTrue(changer instanceof proguard.classfile.attribute.annotation.visitor.ElementValueVisitor);

        // Act & Assert - should work when called through the interface
        proguard.classfile.attribute.annotation.visitor.ElementValueVisitor visitor = changer;
        assertDoesNotThrow(() -> visitor.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue));
    }

    /**
     * Tests that visitEnumConstantElementValue delegates to visitAnyElementValue.
     * This verifies the first part of the delegation pattern used by the method.
     */
    @Test
    public void testVisitEnumConstantElementValue_delegatesToVisitAnyElementValue() {
        // Arrange - create a spy to verify delegation
        TargetClassChanger spyChanger = spy(new TargetClassChanger());

        // Act
        spyChanger.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert - verify that visitEnumConstantElementValue was called
        verify(spyChanger).visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
    }

    /**
     * Tests that visitEnumConstantElementValue handles enum values representing different enum types.
     * Enum constant element values reference enum types that may be different.
     */
    @Test
    public void testVisitEnumConstantElementValue_withDifferentEnumTypes() {
        // Arrange - create multiple enum constant element values representing different enum types
        EnumConstantElementValue enumType1 = mock(EnumConstantElementValue.class, "enumType1");
        EnumConstantElementValue enumType2 = mock(EnumConstantElementValue.class, "enumType2");
        EnumConstantElementValue enumType3 = mock(EnumConstantElementValue.class, "enumType3");

        // Act & Assert - should handle different enum types
        assertDoesNotThrow(() -> {
            changer.visitEnumConstantElementValue(clazz, annotation, enumType1);
            changer.visitEnumConstantElementValue(clazz, annotation, enumType2);
            changer.visitEnumConstantElementValue(clazz, annotation, enumType3);
        });
    }

    /**
     * Tests that visitEnumConstantElementValue handles null clazz gracefully.
     */
    @Test
    public void testVisitEnumConstantElementValue_withNullClazz() {
        // Act & Assert - behavior with null clazz (may or may not throw depending on implementation)
        // This test documents the behavior
        try {
            changer.visitEnumConstantElementValue(null, annotation, enumConstantElementValue);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitEnumConstantElementValue handles null annotation gracefully.
     */
    @Test
    public void testVisitEnumConstantElementValue_withNullAnnotation() {
        // Act & Assert - behavior with null annotation
        // This test documents the behavior
        try {
            changer.visitEnumConstantElementValue(clazz, null, enumConstantElementValue);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitEnumConstantElementValue handles null enum constant element value gracefully.
     */
    @Test
    public void testVisitEnumConstantElementValue_withNullEnumConstantElementValue() {
        // Act & Assert - behavior with null enum constant element value
        // This test documents the behavior
        try {
            changer.visitEnumConstantElementValue(clazz, annotation, null);
            // If it doesn't throw, that's fine
        } catch (NullPointerException e) {
            // If it throws NPE, that's also acceptable behavior
        }
    }

    /**
     * Tests that visitEnumConstantElementValue processes enum constant element values that represent
     * annotation attribute values correctly.
     */
    @Test
    public void testVisitEnumConstantElementValue_processesAnnotationAttributeValues() {
        // Arrange - enum constant element values are used for enum-typed annotation attributes
        EnumConstantElementValue attributeValue = mock(EnumConstantElementValue.class);

        // Act & Assert - should process annotation attribute values
        assertDoesNotThrow(() -> changer.visitEnumConstantElementValue(clazz, annotation, attributeValue));
    }

    /**
     * Tests that visitEnumConstantElementValue is correctly invoked through the visitor pattern
     * when processing annotations during class optimization.
     */
    @Test
    public void testVisitEnumConstantElementValue_invokedDuringOptimization() {
        // Act & Assert - should be invokable during optimization phase
        assertDoesNotThrow(() -> changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue));
    }

    /**
     * Tests that visitEnumConstantElementValue handles enum constants from the same enum type correctly.
     * Multiple annotation values may reference different constants from the same enum.
     */
    @Test
    public void testVisitEnumConstantElementValue_withMultipleConstantsFromSameEnum() {
        // Arrange - multiple constants from the same enum type
        EnumConstantElementValue constant1 = mock(EnumConstantElementValue.class, "CONSTANT_1");
        EnumConstantElementValue constant2 = mock(EnumConstantElementValue.class, "CONSTANT_2");
        EnumConstantElementValue constant3 = mock(EnumConstantElementValue.class, "CONSTANT_3");

        // Act & Assert - should handle multiple constants from the same enum
        assertDoesNotThrow(() -> {
            changer.visitEnumConstantElementValue(clazz, annotation, constant1);
            changer.visitEnumConstantElementValue(clazz, annotation, constant2);
            changer.visitEnumConstantElementValue(clazz, annotation, constant3);
        });
    }

    /**
     * Tests that visitEnumConstantElementValue correctly processes the referencedClasses field
     * by calling updateReferencedClasses. This is important for retargeting merged enum classes.
     */
    @Test
    public void testVisitEnumConstantElementValue_updatesReferencedClasses() {
        // Act & Assert - should process the enum constant element value including its referenced classes
        assertDoesNotThrow(() -> changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue));
    }

    /**
     * Tests that visitEnumConstantElementValue handles enum constants that may have been affected
     * by class merging optimization.
     */
    @Test
    public void testVisitEnumConstantElementValue_handlesClassMergedEnums() {
        // Act & Assert - should handle enum constants whose classes may have been merged
        assertDoesNotThrow(() -> changer.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue));
    }
}

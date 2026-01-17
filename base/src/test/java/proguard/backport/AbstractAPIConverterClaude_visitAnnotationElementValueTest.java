package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.AnnotationElementValue;
import proguard.classfile.attribute.annotation.visitor.AnnotationVisitor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)}.
 *
 * The visitAnnotationElementValue method delegates to the annotationAccept method
 * of the AnnotationElementValue, which processes nested annotations by calling back
 * to the converter's visitAnnotation method. This handles annotations that contain
 * other annotations as element values.
 */
public class AbstractAPIConverterClaude_visitAnnotationElementValueTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Annotation annotation;
    private AnnotationElementValue annotationElementValue;

    /**
     * Creates a concrete test subclass of AbstractAPIConverter for testing purposes.
     * This is necessary because AbstractAPIConverter is abstract.
     */
    private static class TestAPIConverter extends AbstractAPIConverter {
        TestAPIConverter(ClassPool programClassPool,
                        ClassPool libraryClassPool,
                        WarningPrinter warningPrinter,
                        ClassVisitor modifiedClassVisitor,
                        InstructionVisitor extraInstructionVisitor) {
            super(programClassPool, libraryClassPool, warningPrinter,
                  modifiedClassVisitor, extraInstructionVisitor);

            // Initialize with empty replacements to avoid NullPointerExceptions
            setTypeReplacements(new TypeReplacement[0]);
            setMethodReplacements(new MethodReplacement[0]);
        }
    }

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        warningPrinter = mock(WarningPrinter.class);
        modifiedClassVisitor = mock(ClassVisitor.class);
        extraInstructionVisitor = mock(InstructionVisitor.class);

        converter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        clazz = mock(ProgramClass.class);
        annotation = mock(Annotation.class);
        annotationElementValue = mock(AnnotationElementValue.class);
    }

    /**
     * Tests that visitAnnotationElementValue can be called with valid mock objects without throwing exceptions.
     * This is a smoke test to ensure the method executes successfully.
     */
    @Test
    public void testVisitAnnotationElementValue_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue));
    }

    /**
     * Tests that visitAnnotationElementValue correctly delegates to annotationAccept.
     * This verifies the core functionality of the method - delegation to process nested annotations.
     */
    @Test
    public void testVisitAnnotationElementValue_delegatesToAnnotationAccept() {
        // Act
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify that annotationAccept was called with correct parameters
        verify(annotationElementValue).annotationAccept(clazz, converter);
    }

    /**
     * Tests that visitAnnotationElementValue passes the converter itself as the visitor.
     * This is crucial because the converter implements AnnotationVisitor.
     */
    @Test
    public void testVisitAnnotationElementValue_passesConverterAsVisitor() {
        // Act
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify that the converter itself is passed as the visitor parameter
        verify(annotationElementValue).annotationAccept(
            eq(clazz),
            same(converter)  // The converter itself should be passed as visitor
        );
    }

    /**
     * Tests that visitAnnotationElementValue can be called multiple times.
     * Each call should independently delegate to annotationAccept.
     */
    @Test
    public void testVisitAnnotationElementValue_calledMultipleTimes_delegatesEachTime() {
        // Act
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify annotationAccept was called exactly 3 times
        verify(annotationElementValue, times(3)).annotationAccept(clazz, converter);
    }

    /**
     * Tests that visitAnnotationElementValue works with different annotation element values.
     * Each annotation element value instance should have its annotationAccept method called.
     */
    @Test
    public void testVisitAnnotationElementValue_withDifferentAnnotationElementValues() {
        // Arrange
        AnnotationElementValue annElem1 = mock(AnnotationElementValue.class);
        AnnotationElementValue annElem2 = mock(AnnotationElementValue.class);
        AnnotationElementValue annElem3 = mock(AnnotationElementValue.class);

        // Act
        converter.visitAnnotationElementValue(clazz, annotation, annElem1);
        converter.visitAnnotationElementValue(clazz, annotation, annElem2);
        converter.visitAnnotationElementValue(clazz, annotation, annElem3);

        // Assert - verify each annotation element value's annotationAccept was called once
        verify(annElem1).annotationAccept(clazz, converter);
        verify(annElem2).annotationAccept(clazz, converter);
        verify(annElem3).annotationAccept(clazz, converter);
    }

    /**
     * Tests that visitAnnotationElementValue works with different clazz instances.
     * Each clazz should be correctly passed through to annotationAccept.
     */
    @Test
    public void testVisitAnnotationElementValue_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act
        converter.visitAnnotationElementValue(clazz1, annotation, annotationElementValue);
        converter.visitAnnotationElementValue(clazz2, annotation, annotationElementValue);

        // Assert - verify the correct clazz was passed in each call
        verify(annotationElementValue).annotationAccept(clazz1, converter);
        verify(annotationElementValue).annotationAccept(clazz2, converter);
    }

    /**
     * Tests that visitAnnotationElementValue works with different annotation instances.
     * The annotation parameter provides context for the nested annotation.
     */
    @Test
    public void testVisitAnnotationElementValue_withDifferentAnnotations() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);

        // Act
        converter.visitAnnotationElementValue(clazz, annotation1, annotationElementValue);
        converter.visitAnnotationElementValue(clazz, annotation2, annotationElementValue);

        // Assert - verify delegation occurred for each annotation context
        verify(annotationElementValue, times(2)).annotationAccept(eq(clazz), eq(converter));
    }

    /**
     * Tests that visitAnnotationElementValue doesn't interact with parameters beyond delegation.
     * The method should only delegate and not directly interact with clazz or annotation.
     */
    @Test
    public void testVisitAnnotationElementValue_doesNotDirectlyInteractWithParameters() {
        // Act
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify no direct interactions with parameters (they're only passed through)
        verifyNoInteractions(clazz);
        verifyNoInteractions(annotation);
        // annotationElementValue should have been called via delegation
        verify(annotationElementValue, times(1))
            .annotationAccept(any(), any(AnnotationVisitor.class));
    }

    /**
     * Tests that visitAnnotationElementValue doesn't trigger warnings.
     * This method should operate without generating any warnings.
     */
    @Test
    public void testVisitAnnotationElementValue_doesNotTriggerWarnings() {
        // Act
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests that visitAnnotationElementValue doesn't trigger the modified class visitor.
     * This method just visits nested annotations and shouldn't mark the class as modified directly.
     */
    @Test
    public void testVisitAnnotationElementValue_doesNotTriggerModifiedClassVisitor() {
        // Act
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify the modified class visitor was not invoked
        verifyNoInteractions(modifiedClassVisitor);
    }

    /**
     * Tests that visitAnnotationElementValue doesn't trigger the extra instruction visitor.
     * This method handles nested annotations, not instructions.
     */
    @Test
    public void testVisitAnnotationElementValue_doesNotTriggerExtraInstructionVisitor() {
        // Act
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify the extra instruction visitor was not invoked
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests that visitAnnotationElementValue works with a converter with null warning printer.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnnotationElementValue_withNullWarningPrinter_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act
        converterWithNullPrinter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify delegation still occurred
        verify(annotationElementValue).annotationAccept(clazz, converterWithNullPrinter);
    }

    /**
     * Tests that visitAnnotationElementValue works with a converter with null class visitor.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnnotationElementValue_withNullClassVisitor_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        // Act
        converterWithNullVisitor.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify delegation still occurred
        verify(annotationElementValue).annotationAccept(clazz, converterWithNullVisitor);
    }

    /**
     * Tests that visitAnnotationElementValue works with a converter with null instruction visitor.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnnotationElementValue_withNullInstructionVisitor_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        // Act
        converterWithNullInstrVisitor.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify delegation still occurred
        verify(annotationElementValue).annotationAccept(clazz, converterWithNullInstrVisitor);
    }

    /**
     * Tests that visitAnnotationElementValue maintains correct order when called with multiple annotation element values.
     * This ensures that sequential calls maintain independence and proper delegation.
     */
    @Test
    public void testVisitAnnotationElementValue_sequentialCalls_maintainIndependence() {
        // Arrange
        AnnotationElementValue annElem1 = mock(AnnotationElementValue.class);
        AnnotationElementValue annElem2 = mock(AnnotationElementValue.class);

        // Act - call with first annotation element value
        converter.visitAnnotationElementValue(clazz, annotation, annElem1);
        verify(annElem1).annotationAccept(clazz, converter);

        // Act - call with second annotation element value
        converter.visitAnnotationElementValue(clazz, annotation, annElem2);
        verify(annElem2).annotationAccept(clazz, converter);

        // Assert - first annotation element value should not have been called again
        verify(annElem1, times(1)).annotationAccept(any(), any(AnnotationVisitor.class));
    }

    /**
     * Tests that visitAnnotationElementValue integrates correctly with the visitor pattern.
     * The converter implements AnnotationVisitor, so it should be a valid visitor.
     */
    @Test
    public void testVisitAnnotationElementValue_converterIsValidVisitor() {
        // Arrange & Assert - verify the converter is an instance of AnnotationVisitor
        assertTrue(converter instanceof AnnotationVisitor,
            "Converter should implement AnnotationVisitor to be used as a visitor");

        // Act
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify it's passed as an AnnotationVisitor
        verify(annotationElementValue).annotationAccept(
            any(Clazz.class),
            any(AnnotationVisitor.class)
        );
    }

    /**
     * Tests that visitAnnotationElementValue handles repeated calls with the same parameters idempotently.
     * While each call should delegate, the delegation should be consistent.
     */
    @Test
    public void testVisitAnnotationElementValue_repeatedCallsWithSameParameters_delegatesConsistently() {
        // Act
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify delegation happened twice with identical parameters
        verify(annotationElementValue, times(2)).annotationAccept(same(clazz), same(converter));
    }

    /**
     * Tests that visitAnnotationElementValue properly integrates with all three parameters.
     * This is a comprehensive test verifying the complete parameter flow.
     */
    @Test
    public void testVisitAnnotationElementValue_integratesAllParameters() {
        // Arrange - create specific mocks
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");
        Annotation specificAnnotation = mock(Annotation.class, "specificAnnotation");
        AnnotationElementValue specificAnnElem = mock(AnnotationElementValue.class, "specificAnnElem");

        // Act
        converter.visitAnnotationElementValue(specificClazz, specificAnnotation, specificAnnElem);

        // Assert - verify specific parameters were passed correctly
        verify(specificAnnElem).annotationAccept(specificClazz, converter);
    }

    /**
     * Tests that visitAnnotationElementValue works correctly across different converter instances.
     * Different converters should independently delegate to their annotation element values.
     */
    @Test
    public void testVisitAnnotationElementValue_withDifferentConverters_delegatesIndependently() {
        // Arrange
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
        converter2.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify each converter was passed independently
        verify(annotationElementValue, times(1)).annotationAccept(clazz, converter);
        verify(annotationElementValue, times(1)).annotationAccept(clazz, converter2);
    }

    /**
     * Tests that visitAnnotationElementValue handles the visitor pattern delegation correctly.
     * The method should pass the converter (which is an AnnotationVisitor) to process nested annotations.
     */
    @Test
    public void testVisitAnnotationElementValue_delegationFollowsVisitorPattern() {
        // Act
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify the method delegates by calling annotationAccept exactly once
        verify(annotationElementValue, times(1))
            .annotationAccept(any(Clazz.class), any(AnnotationVisitor.class));
    }

    /**
     * Tests that visitAnnotationElementValue with empty class pool works correctly.
     * The method should still delegate even with empty class pools.
     */
    @Test
    public void testVisitAnnotationElementValue_withEmptyClassPools_delegatesCorrectly() {
        // Arrange - converter already has empty class pools from setUp

        // Act
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify delegation occurred
        verify(annotationElementValue).annotationAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotationElementValue can handle multiple different combinations.
     * This tests the method's ability to handle various combinations of inputs.
     */
    @Test
    public void testVisitAnnotationElementValue_withMultipleCombinations() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class, "clazz1");
        Clazz clazz2 = mock(ProgramClass.class, "clazz2");
        Annotation ann1 = mock(Annotation.class, "ann1");
        Annotation ann2 = mock(Annotation.class, "ann2");
        AnnotationElementValue annElem1 = mock(AnnotationElementValue.class, "annElem1");
        AnnotationElementValue annElem2 = mock(AnnotationElementValue.class, "annElem2");

        // Act
        converter.visitAnnotationElementValue(clazz1, ann1, annElem1);
        converter.visitAnnotationElementValue(clazz1, ann2, annElem2);
        converter.visitAnnotationElementValue(clazz2, ann1, annElem1);
        converter.visitAnnotationElementValue(clazz2, ann2, annElem2);

        // Assert - verify all combinations were processed
        verify(annElem1, times(2)).annotationAccept(any(Clazz.class), eq(converter));
        verify(annElem2, times(2)).annotationAccept(any(Clazz.class), eq(converter));
    }

    /**
     * Tests that visitAnnotationElementValue properly delegates without modifying the annotation element value.
     * The method should only delegate, not modify the annotation element value directly.
     */
    @Test
    public void testVisitAnnotationElementValue_doesNotModifyAnnotationElementValueDirectly() {
        // Arrange
        AnnotationElementValue spyAnnElem = mock(AnnotationElementValue.class);

        // Act
        converter.visitAnnotationElementValue(clazz, annotation, spyAnnElem);

        // Assert - verify only annotationAccept was called, nothing else
        verify(spyAnnElem, times(1)).annotationAccept(any(), any());
        verifyNoMoreInteractions(spyAnnElem);
    }

    /**
     * Tests visitAnnotationElementValue execution completes quickly.
     * Since it's a simple delegation method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnnotationElementValue_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
            "visitAnnotationElementValue should execute quickly as it's a simple delegation");
    }

    /**
     * Tests that visitAnnotationElementValue handles nested annotations correctly.
     * This is the primary use case - annotations containing other annotations.
     * For example: @Outer(@Inner(value = "test"))
     */
    @Test
    public void testVisitAnnotationElementValue_handlesNestedAnnotations() {
        // Arrange
        AnnotationElementValue nestedAnnElem = mock(AnnotationElementValue.class, "nestedAnnotation");

        // Act
        converter.visitAnnotationElementValue(clazz, annotation, nestedAnnElem);

        // Assert - verify nested annotation was processed
        verify(nestedAnnElem).annotationAccept(clazz, converter);
    }

    /**
     * Tests that visitAnnotationElementValue works with multiple levels of nesting.
     * Nested annotations can themselves contain nested annotations.
     */
    @Test
    public void testVisitAnnotationElementValue_withMultipleLevelsOfNesting() {
        // Arrange
        AnnotationElementValue level1 = mock(AnnotationElementValue.class, "level1");
        AnnotationElementValue level2 = mock(AnnotationElementValue.class, "level2");
        AnnotationElementValue level3 = mock(AnnotationElementValue.class, "level3");

        // Act - simulate processing nested annotations at different levels
        converter.visitAnnotationElementValue(clazz, annotation, level1);
        converter.visitAnnotationElementValue(clazz, annotation, level2);
        converter.visitAnnotationElementValue(clazz, annotation, level3);

        // Assert - verify all levels were processed
        verify(level1).annotationAccept(clazz, converter);
        verify(level2).annotationAccept(clazz, converter);
        verify(level3).annotationAccept(clazz, converter);
    }

    /**
     * Tests that visitAnnotationElementValue maintains the correct contract for the visitor pattern.
     * The method handles annotation element values by delegating to process nested annotations.
     */
    @Test
    public void testVisitAnnotationElementValue_maintainsVisitorContract() {
        // Act
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify the visitor contract is maintained
        verify(annotationElementValue, times(1))
            .annotationAccept(any(Clazz.class), any(AnnotationVisitor.class));
    }

    /**
     * Tests that visitAnnotationElementValue passes the clazz parameter correctly.
     * The clazz parameter represents the class containing the annotation.
     */
    @Test
    public void testVisitAnnotationElementValue_passesClazzParameter() {
        // Arrange
        Clazz annotationClass = mock(ProgramClass.class, "annotationClass");

        // Act
        converter.visitAnnotationElementValue(annotationClass, annotation, annotationElementValue);

        // Assert - verify the clazz was passed to annotationAccept
        verify(annotationElementValue).annotationAccept(same(annotationClass), any(AnnotationVisitor.class));
    }

    /**
     * Tests that visitAnnotationElementValue properly delegates in the context of nested annotation processing.
     * This verifies that the delegation mechanism works as expected in the visitor pattern.
     */
    @Test
    public void testVisitAnnotationElementValue_delegationInNestedAnnotationContext() {
        // Act
        converter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

        // Assert - verify delegation occurred with the converter as visitor
        verify(annotationElementValue).annotationAccept(clazz, converter);
    }

    /**
     * Tests that visitAnnotationElementValue handles complex nested annotation structures.
     * Complex annotations with multiple nested annotations should be processed correctly.
     */
    @Test
    public void testVisitAnnotationElementValue_withComplexNestedStructure() {
        // Arrange
        AnnotationElementValue annElem1 = mock(AnnotationElementValue.class, "annElem1");
        AnnotationElementValue annElem2 = mock(AnnotationElementValue.class, "annElem2");
        AnnotationElementValue annElem3 = mock(AnnotationElementValue.class, "annElem3");

        // Act - process multiple nested annotations
        converter.visitAnnotationElementValue(clazz, annotation, annElem1);
        converter.visitAnnotationElementValue(clazz, annotation, annElem2);
        converter.visitAnnotationElementValue(clazz, annotation, annElem3);

        // Assert - verify all nested annotations were processed
        verify(annElem1).annotationAccept(clazz, converter);
        verify(annElem2).annotationAccept(clazz, converter);
        verify(annElem3).annotationAccept(clazz, converter);
    }

    /**
     * Tests that visitAnnotationElementValue correctly handles the parent annotation context.
     * The parent annotation parameter provides context for the nested annotation.
     */
    @Test
    public void testVisitAnnotationElementValue_handlesParentAnnotationContext() {
        // Arrange
        Annotation parentAnnotation = mock(Annotation.class, "parentAnnotation");

        // Act
        converter.visitAnnotationElementValue(clazz, parentAnnotation, annotationElementValue);

        // Assert - verify processing occurred in the parent annotation context
        verify(annotationElementValue).annotationAccept(clazz, converter);
    }
}

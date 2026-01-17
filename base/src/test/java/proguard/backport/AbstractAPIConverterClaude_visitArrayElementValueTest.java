package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.ArrayElementValue;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitArrayElementValue(Clazz, Annotation, ArrayElementValue)}.
 *
 * The visitArrayElementValue method delegates to the elementValuesAccept method
 * of the ArrayElementValue, which processes array element values by calling back
 * to the converter's element value visitor methods. This handles arrays in annotation
 * values (e.g., @SomeAnnotation(values = {1, 2, 3}), @Target({TYPE, METHOD})).
 */
public class AbstractAPIConverterClaude_visitArrayElementValueTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Annotation annotation;
    private ArrayElementValue arrayElementValue;

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
        arrayElementValue = mock(ArrayElementValue.class);
    }

    /**
     * Tests that visitArrayElementValue can be called with valid mock objects without throwing exceptions.
     * This is a smoke test to ensure the method executes successfully.
     */
    @Test
    public void testVisitArrayElementValue_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitArrayElementValue(clazz, annotation, arrayElementValue));
    }

    /**
     * Tests that visitArrayElementValue correctly delegates to elementValuesAccept.
     * This verifies the core functionality of the method - delegation to process array element values.
     */
    @Test
    public void testVisitArrayElementValue_delegatesToElementValuesAccept() {
        // Act
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify that elementValuesAccept was called with correct parameters
        verify(arrayElementValue).elementValuesAccept(clazz, annotation, converter);
    }

    /**
     * Tests that visitArrayElementValue passes the converter itself as the visitor.
     * This is crucial because the converter implements ElementValueVisitor.
     */
    @Test
    public void testVisitArrayElementValue_passesConverterAsVisitor() {
        // Act
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify that the converter itself is passed as the visitor parameter
        verify(arrayElementValue).elementValuesAccept(
            eq(clazz),
            eq(annotation),
            same(converter)  // The converter itself should be passed as visitor
        );
    }

    /**
     * Tests that visitArrayElementValue can be called multiple times.
     * Each call should independently delegate to elementValuesAccept.
     */
    @Test
    public void testVisitArrayElementValue_calledMultipleTimes_delegatesEachTime() {
        // Act
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify elementValuesAccept was called exactly 3 times
        verify(arrayElementValue, times(3)).elementValuesAccept(clazz, annotation, converter);
    }

    /**
     * Tests that visitArrayElementValue works with different array element values.
     * Each array element value instance should have its elementValuesAccept method called.
     */
    @Test
    public void testVisitArrayElementValue_withDifferentArrayElementValues() {
        // Arrange
        ArrayElementValue array1 = mock(ArrayElementValue.class);
        ArrayElementValue array2 = mock(ArrayElementValue.class);
        ArrayElementValue array3 = mock(ArrayElementValue.class);

        // Act
        converter.visitArrayElementValue(clazz, annotation, array1);
        converter.visitArrayElementValue(clazz, annotation, array2);
        converter.visitArrayElementValue(clazz, annotation, array3);

        // Assert - verify each array's elementValuesAccept was called once
        verify(array1).elementValuesAccept(clazz, annotation, converter);
        verify(array2).elementValuesAccept(clazz, annotation, converter);
        verify(array3).elementValuesAccept(clazz, annotation, converter);
    }

    /**
     * Tests that visitArrayElementValue works with different clazz instances.
     * Each clazz should be correctly passed through to elementValuesAccept.
     */
    @Test
    public void testVisitArrayElementValue_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act
        converter.visitArrayElementValue(clazz1, annotation, arrayElementValue);
        converter.visitArrayElementValue(clazz2, annotation, arrayElementValue);

        // Assert - verify the correct clazz was passed in each call
        verify(arrayElementValue).elementValuesAccept(clazz1, annotation, converter);
        verify(arrayElementValue).elementValuesAccept(clazz2, annotation, converter);
    }

    /**
     * Tests that visitArrayElementValue works with different annotation instances.
     * Each annotation should be correctly passed through to elementValuesAccept.
     */
    @Test
    public void testVisitArrayElementValue_withDifferentAnnotations_passesCorrectAnnotation() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);

        // Act
        converter.visitArrayElementValue(clazz, annotation1, arrayElementValue);
        converter.visitArrayElementValue(clazz, annotation2, arrayElementValue);

        // Assert - verify the correct annotation was passed in each call
        verify(arrayElementValue).elementValuesAccept(clazz, annotation1, converter);
        verify(arrayElementValue).elementValuesAccept(clazz, annotation2, converter);
    }

    /**
     * Tests that visitArrayElementValue doesn't interact with parameters beyond delegation.
     * The method should only delegate and not directly interact with clazz or annotation.
     */
    @Test
    public void testVisitArrayElementValue_doesNotDirectlyInteractWithParameters() {
        // Act
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify no direct interactions with parameters (they're only passed through)
        verifyNoInteractions(clazz);
        verifyNoInteractions(annotation);
        // arrayElementValue should have been called via delegation
        verify(arrayElementValue, times(1))
            .elementValuesAccept(any(), any(), any(ElementValueVisitor.class));
    }

    /**
     * Tests that visitArrayElementValue doesn't trigger warnings.
     * This method should operate without generating any warnings.
     */
    @Test
    public void testVisitArrayElementValue_doesNotTriggerWarnings() {
        // Act
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests that visitArrayElementValue doesn't trigger the modified class visitor.
     * This method just visits array element values and shouldn't mark the class as modified directly.
     */
    @Test
    public void testVisitArrayElementValue_doesNotTriggerModifiedClassVisitor() {
        // Act
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify the modified class visitor was not invoked
        verifyNoInteractions(modifiedClassVisitor);
    }

    /**
     * Tests that visitArrayElementValue doesn't trigger the extra instruction visitor.
     * This method handles array element values, not instructions.
     */
    @Test
    public void testVisitArrayElementValue_doesNotTriggerExtraInstructionVisitor() {
        // Act
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify the extra instruction visitor was not invoked
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests that visitArrayElementValue works with a converter with null warning printer.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitArrayElementValue_withNullWarningPrinter_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act
        converterWithNullPrinter.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify delegation still occurred
        verify(arrayElementValue).elementValuesAccept(clazz, annotation, converterWithNullPrinter);
    }

    /**
     * Tests that visitArrayElementValue works with a converter with null class visitor.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitArrayElementValue_withNullClassVisitor_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        // Act
        converterWithNullVisitor.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify delegation still occurred
        verify(arrayElementValue).elementValuesAccept(clazz, annotation, converterWithNullVisitor);
    }

    /**
     * Tests that visitArrayElementValue works with a converter with null instruction visitor.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitArrayElementValue_withNullInstructionVisitor_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        // Act
        converterWithNullInstrVisitor.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify delegation still occurred
        verify(arrayElementValue).elementValuesAccept(clazz, annotation, converterWithNullInstrVisitor);
    }

    /**
     * Tests that visitArrayElementValue maintains correct order when called with multiple arrays.
     * This ensures that sequential calls maintain independence and proper delegation.
     */
    @Test
    public void testVisitArrayElementValue_sequentialCalls_maintainIndependence() {
        // Arrange
        ArrayElementValue array1 = mock(ArrayElementValue.class);
        ArrayElementValue array2 = mock(ArrayElementValue.class);

        // Act - call with first array
        converter.visitArrayElementValue(clazz, annotation, array1);
        verify(array1).elementValuesAccept(clazz, annotation, converter);

        // Act - call with second array
        converter.visitArrayElementValue(clazz, annotation, array2);
        verify(array2).elementValuesAccept(clazz, annotation, converter);

        // Assert - first array should not have been called again
        verify(array1, times(1)).elementValuesAccept(any(), any(), any(ElementValueVisitor.class));
    }

    /**
     * Tests that visitArrayElementValue integrates correctly with the visitor pattern.
     * The converter implements ElementValueVisitor, so it should be a valid visitor.
     */
    @Test
    public void testVisitArrayElementValue_converterIsValidVisitor() {
        // Arrange & Assert - verify the converter is an instance of ElementValueVisitor
        assertTrue(converter instanceof ElementValueVisitor,
            "Converter should implement ElementValueVisitor to be used as a visitor");

        // Act
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify it's passed as an ElementValueVisitor
        verify(arrayElementValue).elementValuesAccept(
            any(Clazz.class),
            any(Annotation.class),
            any(ElementValueVisitor.class)
        );
    }

    /**
     * Tests that visitArrayElementValue handles repeated calls with the same parameters idempotently.
     * While each call should delegate, the delegation should be consistent.
     */
    @Test
    public void testVisitArrayElementValue_repeatedCallsWithSameParameters_delegatesConsistently() {
        // Act
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify delegation happened twice with identical parameters
        verify(arrayElementValue, times(2))
            .elementValuesAccept(same(clazz), same(annotation), same(converter));
    }

    /**
     * Tests that visitArrayElementValue properly integrates with all three parameters.
     * This is a comprehensive test verifying the complete parameter flow.
     */
    @Test
    public void testVisitArrayElementValue_integratesAllParameters() {
        // Arrange - create specific mocks
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");
        Annotation specificAnnotation = mock(Annotation.class, "specificAnnotation");
        ArrayElementValue specificArray = mock(ArrayElementValue.class, "specificArray");

        // Act
        converter.visitArrayElementValue(specificClazz, specificAnnotation, specificArray);

        // Assert - verify all specific parameters were passed correctly
        verify(specificArray).elementValuesAccept(specificClazz, specificAnnotation, converter);
    }

    /**
     * Tests that visitArrayElementValue works correctly across different converter instances.
     * Different converters should independently delegate to their array element values.
     */
    @Test
    public void testVisitArrayElementValue_withDifferentConverters_delegatesIndependently() {
        // Arrange
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);
        converter2.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify each converter was passed independently
        verify(arrayElementValue, times(1)).elementValuesAccept(clazz, annotation, converter);
        verify(arrayElementValue, times(1)).elementValuesAccept(clazz, annotation, converter2);
    }

    /**
     * Tests that visitArrayElementValue handles the visitor pattern delegation correctly.
     * The method should pass the converter (which is an ElementValueVisitor) to process array elements.
     */
    @Test
    public void testVisitArrayElementValue_delegationFollowsVisitorPattern() {
        // Act
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify the method delegates by calling elementValuesAccept exactly once
        verify(arrayElementValue, times(1))
            .elementValuesAccept(any(Clazz.class), any(Annotation.class), any(ElementValueVisitor.class));
    }

    /**
     * Tests that visitArrayElementValue with empty class pool works correctly.
     * The method should still delegate even with empty class pools.
     */
    @Test
    public void testVisitArrayElementValue_withEmptyClassPools_delegatesCorrectly() {
        // Arrange - converter already has empty class pools from setUp

        // Act
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify delegation occurred
        verify(arrayElementValue).elementValuesAccept(clazz, annotation, converter);
    }

    /**
     * Tests visitArrayElementValue can handle multiple different combinations.
     * This tests the method's ability to handle various combinations of inputs.
     */
    @Test
    public void testVisitArrayElementValue_withMultipleCombinations() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class, "clazz1");
        Clazz clazz2 = mock(ProgramClass.class, "clazz2");
        Annotation ann1 = mock(Annotation.class, "ann1");
        Annotation ann2 = mock(Annotation.class, "ann2");
        ArrayElementValue array1 = mock(ArrayElementValue.class, "array1");
        ArrayElementValue array2 = mock(ArrayElementValue.class, "array2");

        // Act
        converter.visitArrayElementValue(clazz1, ann1, array1);
        converter.visitArrayElementValue(clazz1, ann2, array2);
        converter.visitArrayElementValue(clazz2, ann1, array1);
        converter.visitArrayElementValue(clazz2, ann2, array2);

        // Assert - verify all combinations were processed
        verify(array1, times(2)).elementValuesAccept(any(Clazz.class), any(Annotation.class), eq(converter));
        verify(array2, times(2)).elementValuesAccept(any(Clazz.class), any(Annotation.class), eq(converter));
    }

    /**
     * Tests that visitArrayElementValue properly delegates without modifying the array.
     * The method should only delegate, not modify the array directly.
     */
    @Test
    public void testVisitArrayElementValue_doesNotModifyArrayDirectly() {
        // Arrange
        ArrayElementValue spyArray = mock(ArrayElementValue.class);

        // Act
        converter.visitArrayElementValue(clazz, annotation, spyArray);

        // Assert - verify only elementValuesAccept was called, nothing else
        verify(spyArray, times(1)).elementValuesAccept(any(), any(), any());
        verifyNoMoreInteractions(spyArray);
    }

    /**
     * Tests visitArrayElementValue execution completes quickly.
     * Since it's a simple delegation method, it should have minimal overhead.
     */
    @Test
    public void testVisitArrayElementValue_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitArrayElementValue(clazz, annotation, arrayElementValue);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
            "visitArrayElementValue should execute quickly as it's a simple delegation");
    }

    /**
     * Tests that visitArrayElementValue handles arrays in annotation context correctly.
     * This is the primary use case - arrays as annotation element values.
     * For example: @Target({TYPE, METHOD}), @SuppressWarnings({"unchecked", "rawtypes"})
     */
    @Test
    public void testVisitArrayElementValue_handlesAnnotationArrays() {
        // Arrange
        ArrayElementValue annotationArray = mock(ArrayElementValue.class, "annotationArray");

        // Act
        converter.visitArrayElementValue(clazz, annotation, annotationArray);

        // Assert - verify array was processed
        verify(annotationArray).elementValuesAccept(clazz, annotation, converter);
    }

    /**
     * Tests that visitArrayElementValue passes all three parameters correctly.
     * All three parameters (clazz, annotation, converter) are needed for proper processing.
     */
    @Test
    public void testVisitArrayElementValue_passesAllThreeParameters() {
        // Act
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify all three parameters were passed
        verify(arrayElementValue).elementValuesAccept(
            same(clazz),
            same(annotation),
            same(converter)
        );
    }

    /**
     * Tests that visitArrayElementValue handles empty arrays correctly.
     * Arrays can be empty (e.g., @SomeAnnotation(values = {})).
     */
    @Test
    public void testVisitArrayElementValue_withEmptyArray() {
        // Arrange
        ArrayElementValue emptyArray = mock(ArrayElementValue.class, "emptyArray");

        // Act
        converter.visitArrayElementValue(clazz, annotation, emptyArray);

        // Assert - verify delegation occurred for empty array
        verify(emptyArray).elementValuesAccept(clazz, annotation, converter);
    }

    /**
     * Tests that visitArrayElementValue handles arrays with single element.
     * Single-element arrays are common (e.g., @Target(TYPE)).
     */
    @Test
    public void testVisitArrayElementValue_withSingleElementArray() {
        // Arrange
        ArrayElementValue singleElemArray = mock(ArrayElementValue.class, "singleElementArray");

        // Act
        converter.visitArrayElementValue(clazz, annotation, singleElemArray);

        // Assert - verify delegation occurred
        verify(singleElemArray).elementValuesAccept(clazz, annotation, converter);
    }

    /**
     * Tests that visitArrayElementValue handles arrays with multiple elements.
     * Multi-element arrays are common (e.g., @Target({TYPE, METHOD, FIELD})).
     */
    @Test
    public void testVisitArrayElementValue_withMultiElementArray() {
        // Arrange
        ArrayElementValue multiElemArray = mock(ArrayElementValue.class, "multiElementArray");

        // Act
        converter.visitArrayElementValue(clazz, annotation, multiElemArray);

        // Assert - verify delegation occurred
        verify(multiElemArray).elementValuesAccept(clazz, annotation, converter);
    }

    /**
     * Tests that visitArrayElementValue properly delegates in the context of array processing.
     * This verifies that the delegation mechanism works as expected in the visitor pattern.
     */
    @Test
    public void testVisitArrayElementValue_delegationInArrayContext() {
        // Act
        converter.visitArrayElementValue(clazz, annotation, arrayElementValue);

        // Assert - verify delegation occurred with the converter as visitor
        verify(arrayElementValue).elementValuesAccept(clazz, annotation, converter);
    }

    /**
     * Tests that visitArrayElementValue maintains the annotation context during delegation.
     * The annotation parameter provides context for the array elements.
     */
    @Test
    public void testVisitArrayElementValue_maintainsAnnotationContext() {
        // Arrange
        Annotation contextAnnotation = mock(Annotation.class, "contextAnnotation");

        // Act
        converter.visitArrayElementValue(clazz, contextAnnotation, arrayElementValue);

        // Assert - verify the annotation context was maintained
        verify(arrayElementValue).elementValuesAccept(clazz, contextAnnotation, converter);
    }

    /**
     * Tests that visitArrayElementValue correctly handles the clazz context during delegation.
     * The clazz parameter represents the class containing the annotation.
     */
    @Test
    public void testVisitArrayElementValue_maintainsClazzContext() {
        // Arrange
        Clazz contextClazz = mock(ProgramClass.class, "contextClazz");

        // Act
        converter.visitArrayElementValue(contextClazz, annotation, arrayElementValue);

        // Assert - verify the clazz context was maintained
        verify(arrayElementValue).elementValuesAccept(contextClazz, annotation, converter);
    }
}

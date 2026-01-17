package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.annotation.AnnotationsAttribute;
import proguard.classfile.attribute.annotation.visitor.AnnotationVisitor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
 *
 * The visitAnyAnnotationsAttribute method delegates to the annotationsAccept method
 * of the AnnotationsAttribute, which processes each annotation in the attribute
 * by calling back to the converter's visitAnnotation method.
 */
public class AbstractAPIConverterClaude_visitAnyAnnotationsAttributeTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private AnnotationsAttribute annotationsAttribute;

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
        annotationsAttribute = mock(AnnotationsAttribute.class);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute correctly delegates to annotationsAccept.
     * This verifies the core functionality of the method - delegation to process annotations.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_delegatesToAnnotationsAccept() {
        // Act
        converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify that annotationsAccept was called with correct parameters
        verify(annotationsAttribute).annotationsAccept(clazz, converter);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute can be called with valid mocks without exceptions.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() ->
            converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute)
        );
    }

    /**
     * Tests that visitAnyAnnotationsAttribute can be called multiple times.
     * Each call should independently delegate to annotationsAccept.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_calledMultipleTimes_delegatesEachTime() {
        // Act
        converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);
        converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);
        converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify annotationsAccept was called exactly 3 times
        verify(annotationsAttribute, times(3)).annotationsAccept(clazz, converter);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute works with different attribute instances.
     * Each attribute instance should have its annotationsAccept method called.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withDifferentAttributes_delegatesToEach() {
        // Arrange
        AnnotationsAttribute attr1 = mock(AnnotationsAttribute.class);
        AnnotationsAttribute attr2 = mock(AnnotationsAttribute.class);
        AnnotationsAttribute attr3 = mock(AnnotationsAttribute.class);

        // Act
        converter.visitAnyAnnotationsAttribute(clazz, attr1);
        converter.visitAnyAnnotationsAttribute(clazz, attr2);
        converter.visitAnyAnnotationsAttribute(clazz, attr3);

        // Assert - verify each attribute's annotationsAccept was called once
        verify(attr1).annotationsAccept(clazz, converter);
        verify(attr2).annotationsAccept(clazz, converter);
        verify(attr3).annotationsAccept(clazz, converter);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute passes the converter itself as the visitor.
     * This is crucial because the converter implements AnnotationVisitor.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_passesConverterAsVisitor() {
        // Act
        converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify that the converter itself is passed as the visitor parameter
        verify(annotationsAttribute).annotationsAccept(
            eq(clazz),
            same(converter)  // The converter itself should be passed as visitor
        );
    }

    /**
     * Tests that visitAnyAnnotationsAttribute works with different clazz instances.
     * Each clazz should be correctly passed through to annotationsAccept.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act
        converter.visitAnyAnnotationsAttribute(clazz1, annotationsAttribute);
        converter.visitAnyAnnotationsAttribute(clazz2, annotationsAttribute);

        // Assert - verify the correct clazz was passed in each call
        verify(annotationsAttribute).annotationsAccept(clazz1, converter);
        verify(annotationsAttribute).annotationsAccept(clazz2, converter);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute doesn't interact with parameters beyond delegation.
     * The method should only delegate and not directly interact with clazz.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_doesNotDirectlyInteractWithParameters() {
        // Act
        converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify no direct interactions with clazz (it's only passed through)
        verifyNoInteractions(clazz);
        // annotationsAttribute should have been called via delegation
        verify(annotationsAttribute, times(1)).annotationsAccept(any(), any(AnnotationVisitor.class));
    }

    /**
     * Tests that visitAnyAnnotationsAttribute doesn't trigger warnings.
     * This method should operate without generating any warnings.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_doesNotTriggerWarnings() {
        // Act
        converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute doesn't trigger the modified class visitor.
     * This method just visits annotations and shouldn't mark the class as modified directly.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_doesNotTriggerModifiedClassVisitor() {
        // Act
        converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify the modified class visitor was not invoked
        verifyNoInteractions(modifiedClassVisitor);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute doesn't trigger the extra instruction visitor.
     * This method handles annotation attributes, not instructions.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_doesNotTriggerExtraInstructionVisitor() {
        // Act
        converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify the extra instruction visitor was not invoked
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute works with a converter with null warning printer.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withNullWarningPrinter_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act
        converterWithNullPrinter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify delegation still occurred
        verify(annotationsAttribute).annotationsAccept(clazz, converterWithNullPrinter);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute works with a converter with null class visitor.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withNullClassVisitor_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        // Act
        converterWithNullVisitor.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify delegation still occurred
        verify(annotationsAttribute).annotationsAccept(clazz, converterWithNullVisitor);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute works with a converter with null instruction visitor.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withNullInstructionVisitor_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        // Act
        converterWithNullInstrVisitor.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify delegation still occurred
        verify(annotationsAttribute).annotationsAccept(clazz, converterWithNullInstrVisitor);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute maintains correct order when called with multiple attributes.
     * This ensures that sequential calls maintain independence and proper delegation.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_sequentialCalls_maintainIndependence() {
        // Arrange
        AnnotationsAttribute attr1 = mock(AnnotationsAttribute.class);
        AnnotationsAttribute attr2 = mock(AnnotationsAttribute.class);

        // Act - call with first attribute
        converter.visitAnyAnnotationsAttribute(clazz, attr1);
        verify(attr1).annotationsAccept(clazz, converter);

        // Act - call with second attribute
        converter.visitAnyAnnotationsAttribute(clazz, attr2);
        verify(attr2).annotationsAccept(clazz, converter);

        // Assert - first attribute should not have been called again
        verify(attr1, times(1)).annotationsAccept(any(), any(AnnotationVisitor.class));
    }

    /**
     * Tests that visitAnyAnnotationsAttribute integrates correctly with the visitor pattern.
     * The converter implements AnnotationVisitor, so it should be a valid visitor.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_converterIsValidVisitor() {
        // Arrange & Assert - verify the converter is an instance of AnnotationVisitor
        assertTrue(converter instanceof AnnotationVisitor,
            "Converter should implement AnnotationVisitor to be used as a visitor");

        // Act
        converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify it's passed as an AnnotationVisitor
        verify(annotationsAttribute).annotationsAccept(
            any(Clazz.class),
            any(AnnotationVisitor.class)
        );
    }

    /**
     * Tests that visitAnyAnnotationsAttribute handles repeated calls with the same parameters idempotently.
     * While each call should delegate, the delegation should be consistent.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_repeatedCallsWithSameParameters_delegatesConsistently() {
        // Act
        converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);
        converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify delegation happened twice with identical parameters
        verify(annotationsAttribute, times(2)).annotationsAccept(same(clazz), same(converter));
    }

    /**
     * Tests that visitAnyAnnotationsAttribute properly integrates with both parameters.
     * This is a comprehensive test verifying the complete parameter flow.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_integratesBothParameters() {
        // Arrange - create specific mocks
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");
        AnnotationsAttribute specificAttr = mock(AnnotationsAttribute.class, "specificAttr");

        // Act
        converter.visitAnyAnnotationsAttribute(specificClazz, specificAttr);

        // Assert - verify both specific parameters were passed correctly
        verify(specificAttr).annotationsAccept(specificClazz, converter);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute works correctly across different converter instances.
     * Different converters should independently delegate to their annotations.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withDifferentConverters_delegatesIndependently() {
        // Arrange
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act
        converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);
        converter2.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify each converter was passed independently
        verify(annotationsAttribute, times(1)).annotationsAccept(clazz, converter);
        verify(annotationsAttribute, times(1)).annotationsAccept(clazz, converter2);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute handles the visitor pattern delegation correctly.
     * The method should pass the converter (which is an AnnotationVisitor) to process annotations.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_delegationFollowsVisitorPattern() {
        // Act
        converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify the method delegates by calling annotationsAccept exactly once
        verify(annotationsAttribute, times(1)).annotationsAccept(any(Clazz.class), any(AnnotationVisitor.class));
    }

    /**
     * Tests that visitAnyAnnotationsAttribute with empty class pool works correctly.
     * The method should still delegate even with empty class pools.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withEmptyClassPools_delegatesCorrectly() {
        // Arrange - converter already has empty class pools from setUp

        // Act
        converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

        // Assert - verify delegation occurred
        verify(annotationsAttribute).annotationsAccept(clazz, converter);
    }

    /**
     * Tests visitAnyAnnotationsAttribute can handle multiple different class-attribute combinations.
     * This tests the method's ability to handle various combinations of inputs.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_withMultipleCombinations() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class, "clazz1");
        Clazz clazz2 = mock(ProgramClass.class, "clazz2");
        AnnotationsAttribute attr1 = mock(AnnotationsAttribute.class, "attr1");
        AnnotationsAttribute attr2 = mock(AnnotationsAttribute.class, "attr2");

        // Act
        converter.visitAnyAnnotationsAttribute(clazz1, attr1);
        converter.visitAnyAnnotationsAttribute(clazz1, attr2);
        converter.visitAnyAnnotationsAttribute(clazz2, attr1);
        converter.visitAnyAnnotationsAttribute(clazz2, attr2);

        // Assert - verify all combinations were processed
        verify(attr1).annotationsAccept(clazz1, converter);
        verify(attr2).annotationsAccept(clazz1, converter);
        verify(attr1).annotationsAccept(clazz2, converter);
        verify(attr2).annotationsAccept(clazz2, converter);
    }

    /**
     * Tests that visitAnyAnnotationsAttribute properly delegates without modifying the attribute.
     * The method should only delegate, not modify the attribute directly.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_doesNotModifyAttributeDirectly() {
        // Arrange
        AnnotationsAttribute spyAttribute = mock(AnnotationsAttribute.class);

        // Act
        converter.visitAnyAnnotationsAttribute(clazz, spyAttribute);

        // Assert - verify only annotationsAccept was called, nothing else
        verify(spyAttribute, times(1)).annotationsAccept(any(), any());
        verifyNoMoreInteractions(spyAttribute);
    }

    /**
     * Tests visitAnyAnnotationsAttribute execution completes quickly.
     * Since it's a simple delegation method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyAnnotationsAttribute should execute quickly as it's a simple delegation");
    }

    /**
     * Tests that visitAnyAnnotationsAttribute maintains the correct contract for the visitor pattern.
     * The method name starts with "visitAny" indicating it handles any type of annotations attribute.
     */
    @Test
    public void testVisitAnyAnnotationsAttribute_handlesAnyAnnotationsAttribute() {
        // Arrange - could be RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations, etc.
        AnnotationsAttribute runtimeVisibleAnnotations = mock(AnnotationsAttribute.class, "runtimeVisible");
        AnnotationsAttribute runtimeInvisibleAnnotations = mock(AnnotationsAttribute.class, "runtimeInvisible");

        // Act
        converter.visitAnyAnnotationsAttribute(clazz, runtimeVisibleAnnotations);
        converter.visitAnyAnnotationsAttribute(clazz, runtimeInvisibleAnnotations);

        // Assert - verify both types were processed
        verify(runtimeVisibleAnnotations).annotationsAccept(clazz, converter);
        verify(runtimeInvisibleAnnotations).annotationsAccept(clazz, converter);
    }
}

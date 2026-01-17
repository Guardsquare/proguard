package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.annotation.AnnotationDefaultAttribute;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitAnnotationDefaultAttribute(Clazz, Method, AnnotationDefaultAttribute)}.
 *
 * The visitAnnotationDefaultAttribute method delegates to the defaultValueAccept method
 * of the AnnotationDefaultAttribute, which processes the default value by calling back
 * to the converter's ElementValueVisitor methods.
 */
public class AbstractAPIConverterClaude_visitAnnotationDefaultAttributeTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Method method;
    private AnnotationDefaultAttribute annotationDefaultAttribute;

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
        method = mock(Method.class);
        annotationDefaultAttribute = mock(AnnotationDefaultAttribute.class);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute correctly delegates to defaultValueAccept.
     * This verifies the core functionality of the method - delegation to process default values.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_delegatesToDefaultValueAccept() {
        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify that defaultValueAccept was called with correct parameters
        verify(annotationDefaultAttribute).defaultValueAccept(clazz, converter);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute can be called with valid mocks without exceptions.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() ->
            converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute)
        );
    }

    /**
     * Tests that visitAnnotationDefaultAttribute can be called multiple times.
     * Each call should independently delegate to defaultValueAccept.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_calledMultipleTimes_delegatesEachTime() {
        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify defaultValueAccept was called exactly 3 times
        verify(annotationDefaultAttribute, times(3)).defaultValueAccept(clazz, converter);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with different attribute instances.
     * Each attribute instance should have its defaultValueAccept method called.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withDifferentAttributes_delegatesToEach() {
        // Arrange
        AnnotationDefaultAttribute attr1 = mock(AnnotationDefaultAttribute.class);
        AnnotationDefaultAttribute attr2 = mock(AnnotationDefaultAttribute.class);
        AnnotationDefaultAttribute attr3 = mock(AnnotationDefaultAttribute.class);

        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method, attr1);
        converter.visitAnnotationDefaultAttribute(clazz, method, attr2);
        converter.visitAnnotationDefaultAttribute(clazz, method, attr3);

        // Assert - verify each attribute's defaultValueAccept was called once
        verify(attr1).defaultValueAccept(clazz, converter);
        verify(attr2).defaultValueAccept(clazz, converter);
        verify(attr3).defaultValueAccept(clazz, converter);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute passes the converter itself as the visitor.
     * This is crucial because the converter implements ElementValueVisitor.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_passesConverterAsVisitor() {
        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify that the converter itself is passed as the visitor parameter
        verify(annotationDefaultAttribute).defaultValueAccept(
            eq(clazz),
            same(converter)  // The converter itself should be passed as visitor
        );
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with different clazz instances.
     * Each clazz should be correctly passed through to defaultValueAccept.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act
        converter.visitAnnotationDefaultAttribute(clazz1, method, annotationDefaultAttribute);
        converter.visitAnnotationDefaultAttribute(clazz2, method, annotationDefaultAttribute);

        // Assert - verify the correct clazz was passed in each call
        verify(annotationDefaultAttribute).defaultValueAccept(clazz1, converter);
        verify(annotationDefaultAttribute).defaultValueAccept(clazz2, converter);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with different method instances.
     * Each method should be accepted by the visitor, even though it's not directly passed to defaultValueAccept.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withDifferentMethod_acceptsEach() {
        // Arrange
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method1, annotationDefaultAttribute);
        converter.visitAnnotationDefaultAttribute(clazz, method2, annotationDefaultAttribute);

        // Assert - verify delegation occurred for each method context
        verify(annotationDefaultAttribute, times(2)).defaultValueAccept(eq(clazz), eq(converter));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute doesn't interact with parameters beyond delegation.
     * The method should only delegate and not directly interact with clazz or method.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_doesNotDirectlyInteractWithParameters() {
        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify no direct interactions with parameters (they're only passed through)
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        // annotationDefaultAttribute should have been called via delegation
        verify(annotationDefaultAttribute, times(1))
            .defaultValueAccept(any(), any(ElementValueVisitor.class));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute doesn't trigger warnings.
     * This method should operate without generating any warnings.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_doesNotTriggerWarnings() {
        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute doesn't trigger the modified class visitor.
     * This method just visits annotation default values and shouldn't mark the class as modified directly.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_doesNotTriggerModifiedClassVisitor() {
        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify the modified class visitor was not invoked
        verifyNoInteractions(modifiedClassVisitor);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute doesn't trigger the extra instruction visitor.
     * This method handles annotation default attributes, not instructions.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_doesNotTriggerExtraInstructionVisitor() {
        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify the extra instruction visitor was not invoked
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with a converter with null warning printer.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withNullWarningPrinter_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act
        converterWithNullPrinter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify delegation still occurred
        verify(annotationDefaultAttribute).defaultValueAccept(clazz, converterWithNullPrinter);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with a converter with null class visitor.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withNullClassVisitor_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        // Act
        converterWithNullVisitor.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify delegation still occurred
        verify(annotationDefaultAttribute).defaultValueAccept(clazz, converterWithNullVisitor);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with a converter with null instruction visitor.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withNullInstructionVisitor_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        // Act
        converterWithNullInstrVisitor.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify delegation still occurred
        verify(annotationDefaultAttribute).defaultValueAccept(clazz, converterWithNullInstrVisitor);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute maintains correct order when called with multiple attributes.
     * This ensures that sequential calls maintain independence and proper delegation.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_sequentialCalls_maintainIndependence() {
        // Arrange
        AnnotationDefaultAttribute attr1 = mock(AnnotationDefaultAttribute.class);
        AnnotationDefaultAttribute attr2 = mock(AnnotationDefaultAttribute.class);

        // Act - call with first attribute
        converter.visitAnnotationDefaultAttribute(clazz, method, attr1);
        verify(attr1).defaultValueAccept(clazz, converter);

        // Act - call with second attribute
        converter.visitAnnotationDefaultAttribute(clazz, method, attr2);
        verify(attr2).defaultValueAccept(clazz, converter);

        // Assert - first attribute should not have been called again
        verify(attr1, times(1)).defaultValueAccept(any(), any(ElementValueVisitor.class));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute integrates correctly with the visitor pattern.
     * The converter implements ElementValueVisitor, so it should be a valid visitor.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_converterIsValidVisitor() {
        // Arrange & Assert - verify the converter is an instance of ElementValueVisitor
        assertTrue(converter instanceof ElementValueVisitor,
            "Converter should implement ElementValueVisitor to be used as a visitor");

        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify it's passed as an ElementValueVisitor
        verify(annotationDefaultAttribute).defaultValueAccept(
            any(Clazz.class),
            any(ElementValueVisitor.class)
        );
    }

    /**
     * Tests that visitAnnotationDefaultAttribute handles repeated calls with the same parameters idempotently.
     * While each call should delegate, the delegation should be consistent.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_repeatedCallsWithSameParameters_delegatesConsistently() {
        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify delegation happened twice with identical parameters
        verify(annotationDefaultAttribute, times(2)).defaultValueAccept(same(clazz), same(converter));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute properly integrates with all three parameters.
     * This is a comprehensive test verifying the complete parameter flow.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_integratesAllParameters() {
        // Arrange - create specific mocks
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");
        Method specificMethod = mock(Method.class, "specificMethod");
        AnnotationDefaultAttribute specificAttr = mock(AnnotationDefaultAttribute.class, "specificAttr");

        // Act
        converter.visitAnnotationDefaultAttribute(specificClazz, specificMethod, specificAttr);

        // Assert - verify specific parameters were passed correctly
        verify(specificAttr).defaultValueAccept(specificClazz, converter);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works correctly across different converter instances.
     * Different converters should independently delegate to their annotation default attributes.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withDifferentConverters_delegatesIndependently() {
        // Arrange
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        converter2.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify each converter was passed independently
        verify(annotationDefaultAttribute, times(1)).defaultValueAccept(clazz, converter);
        verify(annotationDefaultAttribute, times(1)).defaultValueAccept(clazz, converter2);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute handles the visitor pattern delegation correctly.
     * The method should pass the converter (which is an ElementValueVisitor) to process default values.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_delegationFollowsVisitorPattern() {
        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify the method delegates by calling defaultValueAccept exactly once
        verify(annotationDefaultAttribute, times(1))
            .defaultValueAccept(any(Clazz.class), any(ElementValueVisitor.class));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute with empty class pool works correctly.
     * The method should still delegate even with empty class pools.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withEmptyClassPools_delegatesCorrectly() {
        // Arrange - converter already has empty class pools from setUp

        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify delegation occurred
        verify(annotationDefaultAttribute).defaultValueAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotationDefaultAttribute can handle multiple different combinations.
     * This tests the method's ability to handle various combinations of inputs.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withMultipleCombinations() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class, "clazz1");
        Clazz clazz2 = mock(ProgramClass.class, "clazz2");
        Method method1 = mock(Method.class, "method1");
        Method method2 = mock(Method.class, "method2");
        AnnotationDefaultAttribute attr1 = mock(AnnotationDefaultAttribute.class, "attr1");
        AnnotationDefaultAttribute attr2 = mock(AnnotationDefaultAttribute.class, "attr2");

        // Act
        converter.visitAnnotationDefaultAttribute(clazz1, method1, attr1);
        converter.visitAnnotationDefaultAttribute(clazz1, method2, attr2);
        converter.visitAnnotationDefaultAttribute(clazz2, method1, attr1);
        converter.visitAnnotationDefaultAttribute(clazz2, method2, attr2);

        // Assert - verify all combinations were processed
        verify(attr1, times(2)).defaultValueAccept(any(Clazz.class), eq(converter));
        verify(attr2, times(2)).defaultValueAccept(any(Clazz.class), eq(converter));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute properly delegates without modifying the attribute.
     * The method should only delegate, not modify the attribute directly.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_doesNotModifyAttributeDirectly() {
        // Arrange
        AnnotationDefaultAttribute spyAttribute = mock(AnnotationDefaultAttribute.class);

        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method, spyAttribute);

        // Assert - verify only defaultValueAccept was called, nothing else
        verify(spyAttribute, times(1)).defaultValueAccept(any(), any());
        verifyNoMoreInteractions(spyAttribute);
    }

    /**
     * Tests visitAnnotationDefaultAttribute execution completes quickly.
     * Since it's a simple delegation method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
            "visitAnnotationDefaultAttribute should execute quickly as it's a simple delegation");
    }

    /**
     * Tests that visitAnnotationDefaultAttribute is used for annotation type methods.
     * Annotation default values are only valid for annotation type methods.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_handlesAnnotationTypeMethods() {
        // Arrange
        Method annotationMethod = mock(Method.class, "annotationMethod");

        // Act
        converter.visitAnnotationDefaultAttribute(clazz, annotationMethod, annotationDefaultAttribute);

        // Assert - verify delegation occurred for annotation method
        verify(annotationDefaultAttribute).defaultValueAccept(clazz, converter);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute passes clazz parameter correctly.
     * The clazz parameter represents the class containing the annotation type.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_passesClazzParameter() {
        // Arrange
        Clazz annotationClass = mock(ProgramClass.class, "annotationClass");

        // Act
        converter.visitAnnotationDefaultAttribute(annotationClass, method, annotationDefaultAttribute);

        // Assert - verify the clazz was passed to defaultValueAccept
        verify(annotationDefaultAttribute).defaultValueAccept(same(annotationClass), any(ElementValueVisitor.class));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute properly delegates in the context of annotation processing.
     * This verifies that the delegation mechanism works as expected in the visitor pattern.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_delegationInAnnotationContext() {
        // Act
        converter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify delegation occurred with the converter as visitor
        verify(annotationDefaultAttribute).defaultValueAccept(clazz, converter);
    }
}

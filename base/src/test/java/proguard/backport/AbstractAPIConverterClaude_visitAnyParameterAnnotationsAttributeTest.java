package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.annotation.ParameterAnnotationsAttribute;
import proguard.classfile.attribute.annotation.visitor.AnnotationVisitor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute)}.
 *
 * The visitAnyParameterAnnotationsAttribute method delegates to the annotationsAccept method
 * of the ParameterAnnotationsAttribute, which processes parameter annotations by calling back
 * to the converter's visitAnnotation method for each annotation.
 */
public class AbstractAPIConverterClaude_visitAnyParameterAnnotationsAttributeTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Method method;
    private ParameterAnnotationsAttribute parameterAnnotationsAttribute;

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
        parameterAnnotationsAttribute = mock(ParameterAnnotationsAttribute.class);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute correctly delegates to annotationsAccept.
     * This verifies the core functionality of the method - delegation to process parameter annotations.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_delegatesToAnnotationsAccept() {
        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify that annotationsAccept was called with correct parameters
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, method, converter);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute can be called with valid mocks without exceptions.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() ->
            converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute)
        );
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute can be called multiple times.
     * Each call should independently delegate to annotationsAccept.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_calledMultipleTimes_delegatesEachTime() {
        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify annotationsAccept was called exactly 3 times
        verify(parameterAnnotationsAttribute, times(3)).annotationsAccept(clazz, method, converter);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute works with different attribute instances.
     * Each attribute instance should have its annotationsAccept method called.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withDifferentAttributes_delegatesToEach() {
        // Arrange
        ParameterAnnotationsAttribute attr1 = mock(ParameterAnnotationsAttribute.class);
        ParameterAnnotationsAttribute attr2 = mock(ParameterAnnotationsAttribute.class);
        ParameterAnnotationsAttribute attr3 = mock(ParameterAnnotationsAttribute.class);

        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, attr1);
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, attr2);
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, attr3);

        // Assert - verify each attribute's annotationsAccept was called once
        verify(attr1).annotationsAccept(clazz, method, converter);
        verify(attr2).annotationsAccept(clazz, method, converter);
        verify(attr3).annotationsAccept(clazz, method, converter);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute passes the converter itself as the visitor.
     * This is crucial because the converter implements AnnotationVisitor.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_passesConverterAsVisitor() {
        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify that the converter itself is passed as the visitor parameter
        verify(parameterAnnotationsAttribute).annotationsAccept(
            eq(clazz),
            eq(method),
            same(converter)  // The converter itself should be passed as visitor
        );
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute works with different clazz instances.
     * Each clazz should be correctly passed through to annotationsAccept.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz1, method, parameterAnnotationsAttribute);
        converter.visitAnyParameterAnnotationsAttribute(clazz2, method, parameterAnnotationsAttribute);

        // Assert - verify the correct clazz was passed in each call
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz1, method, converter);
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz2, method, converter);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute works with different method instances.
     * Each method should be correctly passed through to annotationsAccept.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withDifferentMethod_passesCorrectMethod() {
        // Arrange
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method1, parameterAnnotationsAttribute);
        converter.visitAnyParameterAnnotationsAttribute(clazz, method2, parameterAnnotationsAttribute);

        // Assert - verify the correct method was passed in each call
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, method1, converter);
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, method2, converter);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute doesn't interact with parameters beyond delegation.
     * The method should only delegate and not directly interact with clazz or method.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_doesNotDirectlyInteractWithParameters() {
        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify no direct interactions with parameters (they're only passed through)
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        // parameterAnnotationsAttribute should have been called via delegation
        verify(parameterAnnotationsAttribute, times(1))
            .annotationsAccept(any(), any(), any(AnnotationVisitor.class));
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute doesn't trigger warnings.
     * This method should operate without generating any warnings.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_doesNotTriggerWarnings() {
        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute doesn't trigger the modified class visitor.
     * This method just visits parameter annotations and shouldn't mark the class as modified directly.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_doesNotTriggerModifiedClassVisitor() {
        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify the modified class visitor was not invoked
        verifyNoInteractions(modifiedClassVisitor);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute doesn't trigger the extra instruction visitor.
     * This method handles parameter annotation attributes, not instructions.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_doesNotTriggerExtraInstructionVisitor() {
        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify the extra instruction visitor was not invoked
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute works with a converter with null warning printer.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withNullWarningPrinter_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act
        converterWithNullPrinter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify delegation still occurred
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, method, converterWithNullPrinter);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute works with a converter with null class visitor.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withNullClassVisitor_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        // Act
        converterWithNullVisitor.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify delegation still occurred
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, method, converterWithNullVisitor);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute works with a converter with null instruction visitor.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withNullInstructionVisitor_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        // Act
        converterWithNullInstrVisitor.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify delegation still occurred
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, method, converterWithNullInstrVisitor);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute maintains correct order when called with multiple attributes.
     * This ensures that sequential calls maintain independence and proper delegation.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_sequentialCalls_maintainIndependence() {
        // Arrange
        ParameterAnnotationsAttribute attr1 = mock(ParameterAnnotationsAttribute.class);
        ParameterAnnotationsAttribute attr2 = mock(ParameterAnnotationsAttribute.class);

        // Act - call with first attribute
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, attr1);
        verify(attr1).annotationsAccept(clazz, method, converter);

        // Act - call with second attribute
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, attr2);
        verify(attr2).annotationsAccept(clazz, method, converter);

        // Assert - first attribute should not have been called again
        verify(attr1, times(1)).annotationsAccept(any(), any(), any(AnnotationVisitor.class));
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute integrates correctly with the visitor pattern.
     * The converter implements AnnotationVisitor, so it should be a valid visitor.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_converterIsValidVisitor() {
        // Arrange & Assert - verify the converter is an instance of AnnotationVisitor
        assertTrue(converter instanceof AnnotationVisitor,
            "Converter should implement AnnotationVisitor to be used as a visitor");

        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify it's passed as an AnnotationVisitor
        verify(parameterAnnotationsAttribute).annotationsAccept(
            any(Clazz.class),
            any(Method.class),
            any(AnnotationVisitor.class)
        );
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute handles repeated calls with the same parameters idempotently.
     * While each call should delegate, the delegation should be consistent.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_repeatedCallsWithSameParameters_delegatesConsistently() {
        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify delegation happened twice with identical parameters
        verify(parameterAnnotationsAttribute, times(2))
            .annotationsAccept(same(clazz), same(method), same(converter));
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute properly integrates with all three parameters.
     * This is a comprehensive test verifying the complete parameter flow.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_integratesAllParameters() {
        // Arrange - create specific mocks
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");
        Method specificMethod = mock(Method.class, "specificMethod");
        ParameterAnnotationsAttribute specificAttr = mock(ParameterAnnotationsAttribute.class, "specificAttr");

        // Act
        converter.visitAnyParameterAnnotationsAttribute(specificClazz, specificMethod, specificAttr);

        // Assert - verify all specific parameters were passed correctly
        verify(specificAttr).annotationsAccept(specificClazz, specificMethod, converter);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute works correctly across different converter instances.
     * Different converters should independently delegate to their parameter annotations.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withDifferentConverters_delegatesIndependently() {
        // Arrange
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);
        converter2.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify each converter was passed independently
        verify(parameterAnnotationsAttribute, times(1)).annotationsAccept(clazz, method, converter);
        verify(parameterAnnotationsAttribute, times(1)).annotationsAccept(clazz, method, converter2);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute handles the visitor pattern delegation correctly.
     * The method should pass the converter (which is an AnnotationVisitor) to process parameter annotations.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_delegationFollowsVisitorPattern() {
        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify the method delegates by calling annotationsAccept exactly once
        verify(parameterAnnotationsAttribute, times(1))
            .annotationsAccept(any(Clazz.class), any(Method.class), any(AnnotationVisitor.class));
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute with empty class pool works correctly.
     * The method should still delegate even with empty class pools.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withEmptyClassPools_delegatesCorrectly() {
        // Arrange - converter already has empty class pools from setUp

        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);

        // Assert - verify delegation occurred
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, method, converter);
    }

    /**
     * Tests visitAnyParameterAnnotationsAttribute can handle multiple different combinations.
     * This tests the method's ability to handle various combinations of inputs.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withMultipleCombinations() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class, "clazz1");
        Clazz clazz2 = mock(ProgramClass.class, "clazz2");
        Method method1 = mock(Method.class, "method1");
        Method method2 = mock(Method.class, "method2");
        ParameterAnnotationsAttribute attr1 = mock(ParameterAnnotationsAttribute.class, "attr1");
        ParameterAnnotationsAttribute attr2 = mock(ParameterAnnotationsAttribute.class, "attr2");

        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz1, method1, attr1);
        converter.visitAnyParameterAnnotationsAttribute(clazz1, method2, attr2);
        converter.visitAnyParameterAnnotationsAttribute(clazz2, method1, attr1);
        converter.visitAnyParameterAnnotationsAttribute(clazz2, method2, attr2);

        // Assert - verify all combinations were processed
        verify(attr1).annotationsAccept(clazz1, method1, converter);
        verify(attr2).annotationsAccept(clazz1, method2, converter);
        verify(attr1).annotationsAccept(clazz2, method1, converter);
        verify(attr2).annotationsAccept(clazz2, method2, converter);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute properly delegates without modifying the attribute.
     * The method should only delegate, not modify the attribute directly.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_doesNotModifyAttributeDirectly() {
        // Arrange
        ParameterAnnotationsAttribute spyAttribute = mock(ParameterAnnotationsAttribute.class);

        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, spyAttribute);

        // Assert - verify only annotationsAccept was called, nothing else
        verify(spyAttribute, times(1)).annotationsAccept(any(), any(), any());
        verifyNoMoreInteractions(spyAttribute);
    }

    /**
     * Tests visitAnyParameterAnnotationsAttribute execution completes quickly.
     * Since it's a simple delegation method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitAnyParameterAnnotationsAttribute(clazz, method, parameterAnnotationsAttribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
            "visitAnyParameterAnnotationsAttribute should execute quickly as it's a simple delegation");
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute maintains the correct contract for the visitor pattern.
     * The method name starts with "visitAny" indicating it handles any type of parameter annotations attribute.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_handlesAnyParameterAnnotationsAttribute() {
        // Arrange - could be RuntimeVisibleParameterAnnotations, RuntimeInvisibleParameterAnnotations, etc.
        ParameterAnnotationsAttribute runtimeVisible = mock(ParameterAnnotationsAttribute.class, "runtimeVisible");
        ParameterAnnotationsAttribute runtimeInvisible = mock(ParameterAnnotationsAttribute.class, "runtimeInvisible");

        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, runtimeVisible);
        converter.visitAnyParameterAnnotationsAttribute(clazz, method, runtimeInvisible);

        // Assert - verify both types were processed
        verify(runtimeVisible).annotationsAccept(clazz, method, converter);
        verify(runtimeInvisible).annotationsAccept(clazz, method, converter);
    }

    /**
     * Tests that visitAnyParameterAnnotationsAttribute handles methods with different parameter counts.
     * Parameter annotations can exist for methods with various numbers of parameters.
     */
    @Test
    public void testVisitAnyParameterAnnotationsAttribute_withDifferentParameterCounts() {
        // Arrange
        Method noParamMethod = mock(Method.class, "noParams");
        Method oneParamMethod = mock(Method.class, "oneParam");
        Method multiParamMethod = mock(Method.class, "multiParams");

        // Act
        converter.visitAnyParameterAnnotationsAttribute(clazz, noParamMethod, parameterAnnotationsAttribute);
        converter.visitAnyParameterAnnotationsAttribute(clazz, oneParamMethod, parameterAnnotationsAttribute);
        converter.visitAnyParameterAnnotationsAttribute(clazz, multiParamMethod, parameterAnnotationsAttribute);

        // Assert - verify all were processed regardless of parameter count
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, noParamMethod, converter);
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, oneParamMethod, converter);
        verify(parameterAnnotationsAttribute).annotationsAccept(clazz, multiParamMethod, converter);
    }
}

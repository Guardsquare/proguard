package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitAnnotation(Clazz, Annotation)}.
 *
 * The visitAnnotation method updates the type index of an annotation by calling updateDescriptor,
 * which may replace type references based on the converter's type replacement rules. It also
 * delegates to elementValuesAccept to process the annotation's element values.
 */
public class AbstractAPIConverterClaude_visitAnnotationTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Annotation annotation;

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
    }

    /**
     * Tests that visitAnnotation can be called with valid mock objects without throwing exceptions.
     * This is a smoke test to ensure the method executes successfully.
     */
    @Test
    public void testVisitAnnotation_withValidMocks_doesNotThrowException() {
        // Arrange
        when(clazz.getString(anyInt())).thenReturn("Ljava/lang/Override;");
        annotation.u2typeIndex = 1;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnnotation(clazz, annotation));
    }

    /**
     * Tests that visitAnnotation delegates to elementValuesAccept.
     * This verifies the core delegation behavior of the method.
     */
    @Test
    public void testVisitAnnotation_delegatesToElementValuesAccept() {
        // Arrange
        annotation.u2typeIndex = 5;
        when(clazz.getString(5)).thenReturn("Ljava/lang/Deprecated;");

        // Act
        converter.visitAnnotation(clazz, annotation);

        // Assert - verify that elementValuesAccept was called with correct parameters
        verify(annotation).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation with a simple annotation type.
     * Standard Java annotations should be processed correctly.
     */
    @Test
    public void testVisitAnnotation_withOverrideAnnotation() {
        // Arrange
        int typeIndex = 10;
        annotation.u2typeIndex = typeIndex;
        when(clazz.getString(typeIndex)).thenReturn("Ljava/lang/Override;");

        // Act
        converter.visitAnnotation(clazz, annotation);

        // Assert - verify the type was read and delegation occurred
        verify(clazz, atLeastOnce()).getString(typeIndex);
        verify(annotation).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation with a Deprecated annotation type.
     */
    @Test
    public void testVisitAnnotation_withDeprecatedAnnotation() {
        // Arrange
        int typeIndex = 15;
        annotation.u2typeIndex = typeIndex;
        when(clazz.getString(typeIndex)).thenReturn("Ljava/lang/Deprecated;");

        // Act
        converter.visitAnnotation(clazz, annotation);

        // Assert
        verify(clazz, atLeastOnce()).getString(typeIndex);
        verify(annotation).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation with a SuppressWarnings annotation type.
     */
    @Test
    public void testVisitAnnotation_withSuppressWarningsAnnotation() {
        // Arrange
        int typeIndex = 20;
        annotation.u2typeIndex = typeIndex;
        when(clazz.getString(typeIndex)).thenReturn("Ljava/lang/SuppressWarnings;");

        // Act
        converter.visitAnnotation(clazz, annotation);

        // Assert
        verify(clazz, atLeastOnce()).getString(typeIndex);
        verify(annotation).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation with a custom annotation type.
     */
    @Test
    public void testVisitAnnotation_withCustomAnnotation() {
        // Arrange
        int typeIndex = 25;
        annotation.u2typeIndex = typeIndex;
        when(clazz.getString(typeIndex)).thenReturn("Lcom/example/MyAnnotation;");

        // Act
        converter.visitAnnotation(clazz, annotation);

        // Assert
        verify(clazz, atLeastOnce()).getString(typeIndex);
        verify(annotation).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation with a Retention annotation type.
     */
    @Test
    public void testVisitAnnotation_withRetentionAnnotation() {
        // Arrange
        int typeIndex = 30;
        annotation.u2typeIndex = typeIndex;
        when(clazz.getString(typeIndex)).thenReturn("Ljava/lang/annotation/Retention;");

        // Act
        converter.visitAnnotation(clazz, annotation);

        // Assert
        verify(clazz, atLeastOnce()).getString(typeIndex);
        verify(annotation).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation with a Target annotation type.
     */
    @Test
    public void testVisitAnnotation_withTargetAnnotation() {
        // Arrange
        int typeIndex = 35;
        annotation.u2typeIndex = typeIndex;
        when(clazz.getString(typeIndex)).thenReturn("Ljava/lang/annotation/Target;");

        // Act
        converter.visitAnnotation(clazz, annotation);

        // Assert
        verify(clazz, atLeastOnce()).getString(typeIndex);
        verify(annotation).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation with a Documented annotation type.
     */
    @Test
    public void testVisitAnnotation_withDocumentedAnnotation() {
        // Arrange
        int typeIndex = 40;
        annotation.u2typeIndex = typeIndex;
        when(clazz.getString(typeIndex)).thenReturn("Ljava/lang/annotation/Documented;");

        // Act
        converter.visitAnnotation(clazz, annotation);

        // Assert
        verify(clazz, atLeastOnce()).getString(typeIndex);
        verify(annotation).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation with an Inherited annotation type.
     */
    @Test
    public void testVisitAnnotation_withInheritedAnnotation() {
        // Arrange
        int typeIndex = 45;
        annotation.u2typeIndex = typeIndex;
        when(clazz.getString(typeIndex)).thenReturn("Ljava/lang/annotation/Inherited;");

        // Act
        converter.visitAnnotation(clazz, annotation);

        // Assert
        verify(clazz, atLeastOnce()).getString(typeIndex);
        verify(annotation).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation can be called multiple times.
     * Each call should independently process the annotation.
     */
    @Test
    public void testVisitAnnotation_calledMultipleTimes() {
        // Arrange
        int typeIndex = 50;
        annotation.u2typeIndex = typeIndex;
        when(clazz.getString(typeIndex)).thenReturn("Ljava/lang/Override;");

        // Act
        converter.visitAnnotation(clazz, annotation);
        converter.visitAnnotation(clazz, annotation);
        converter.visitAnnotation(clazz, annotation);

        // Assert - verify getString was called at least 3 times
        verify(clazz, atLeast(3)).getString(typeIndex);
        verify(annotation, times(3)).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation with different annotation instances.
     * Each instance should have its type processed and elements delegated independently.
     */
    @Test
    public void testVisitAnnotation_withDifferentAnnotations() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        Annotation annotation3 = mock(Annotation.class);

        annotation1.u2typeIndex = 1;
        annotation2.u2typeIndex = 2;
        annotation3.u2typeIndex = 3;

        when(clazz.getString(1)).thenReturn("Ljava/lang/Override;");
        when(clazz.getString(2)).thenReturn("Ljava/lang/Deprecated;");
        when(clazz.getString(3)).thenReturn("Ljava/lang/SuppressWarnings;");

        // Act
        converter.visitAnnotation(clazz, annotation1);
        converter.visitAnnotation(clazz, annotation2);
        converter.visitAnnotation(clazz, annotation3);

        // Assert - verify each annotation's type was read and delegation occurred
        verify(clazz, atLeastOnce()).getString(1);
        verify(clazz, atLeastOnce()).getString(2);
        verify(clazz, atLeastOnce()).getString(3);
        verify(annotation1).elementValuesAccept(clazz, converter);
        verify(annotation2).elementValuesAccept(clazz, converter);
        verify(annotation3).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation with different clazz instances.
     * Each clazz should provide its own string constants.
     */
    @Test
    public void testVisitAnnotation_withDifferentClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        annotation.u2typeIndex = 10;

        when(clazz1.getString(10)).thenReturn("Ljava/lang/Override;");
        when(clazz2.getString(10)).thenReturn("Ljava/lang/Deprecated;");

        // Act
        converter.visitAnnotation(clazz1, annotation);
        converter.visitAnnotation(clazz2, annotation);

        // Assert
        verify(clazz1, atLeastOnce()).getString(10);
        verify(clazz2, atLeastOnce()).getString(10);
        verify(annotation, times(2)).elementValuesAccept(any(Clazz.class), eq(converter));
    }

    /**
     * Tests that visitAnnotation passes the converter itself as the visitor.
     * This is crucial because the converter implements ElementValueVisitor.
     */
    @Test
    public void testVisitAnnotation_passesConverterAsVisitor() {
        // Arrange
        annotation.u2typeIndex = 15;
        when(clazz.getString(15)).thenReturn("Ljava/lang/Override;");

        // Act
        converter.visitAnnotation(clazz, annotation);

        // Assert - verify that the converter itself is passed as the visitor parameter
        verify(annotation).elementValuesAccept(eq(clazz), same(converter));
    }

    /**
     * Tests visitAnnotation doesn't trigger warnings for standard annotations.
     * Processing standard annotation types should not generate warnings.
     */
    @Test
    public void testVisitAnnotation_doesNotTriggerWarnings() {
        // Arrange
        annotation.u2typeIndex = 20;
        when(clazz.getString(20)).thenReturn("Ljava/lang/Override;");

        // Act
        converter.visitAnnotation(clazz, annotation);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests visitAnnotation with a converter with null warning printer.
     * The method should still process annotations correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnnotation_withNullWarningPrinter() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        annotation.u2typeIndex = 25;
        when(clazz.getString(25)).thenReturn("Ljava/lang/Deprecated;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullPrinter.visitAnnotation(clazz, annotation)
        );
    }

    /**
     * Tests visitAnnotation with a converter with null class visitor.
     * The method should still process annotations correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnnotation_withNullClassVisitor() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        annotation.u2typeIndex = 30;
        when(clazz.getString(30)).thenReturn("Ljava/lang/SuppressWarnings;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullVisitor.visitAnnotation(clazz, annotation)
        );
    }

    /**
     * Tests visitAnnotation with a converter with null instruction visitor.
     * The method should still process annotations correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnnotation_withNullInstructionVisitor() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        annotation.u2typeIndex = 35;
        when(clazz.getString(35)).thenReturn("Ljava/lang/Override;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullInstrVisitor.visitAnnotation(clazz, annotation)
        );
    }

    /**
     * Tests visitAnnotation processes type by calling getString on clazz.
     * This is the key interaction - reading the type string from the constant pool.
     */
    @Test
    public void testVisitAnnotation_readsTypeFromClazz() {
        // Arrange
        int typeIndex = 100;
        annotation.u2typeIndex = typeIndex;
        when(clazz.getString(typeIndex)).thenReturn("Lcom/example/CustomAnnotation;");

        // Act
        converter.visitAnnotation(clazz, annotation);

        // Assert - verify the type was read from the clazz
        verify(clazz, atLeastOnce()).getString(typeIndex);
    }

    /**
     * Tests that visitAnnotation integrates correctly with the visitor pattern.
     * The converter implements ElementValueVisitor, so it should be a valid visitor.
     */
    @Test
    public void testVisitAnnotation_converterIsValidVisitor() {
        // Arrange & Assert - verify the converter is an instance of ElementValueVisitor
        assertTrue(converter instanceof ElementValueVisitor,
            "Converter should implement ElementValueVisitor to be used as a visitor");

        // Arrange
        annotation.u2typeIndex = 40;
        when(clazz.getString(40)).thenReturn("Ljava/lang/Deprecated;");

        // Act
        converter.visitAnnotation(clazz, annotation);

        // Assert - verify it's passed as an ElementValueVisitor
        verify(annotation).elementValuesAccept(any(Clazz.class), any(ElementValueVisitor.class));
    }

    /**
     * Tests visitAnnotation handles sequential calls independently.
     * Each call should process the annotation without interference from previous calls.
     */
    @Test
    public void testVisitAnnotation_sequentialCallsAreIndependent() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);

        annotation1.u2typeIndex = 10;
        annotation2.u2typeIndex = 20;

        when(clazz.getString(10)).thenReturn("Ljava/lang/Override;");
        when(clazz.getString(20)).thenReturn("Ljava/lang/Deprecated;");

        // Act
        converter.visitAnnotation(clazz, annotation1);
        converter.visitAnnotation(clazz, annotation2);

        // Assert - verify both were processed independently
        verify(clazz, atLeastOnce()).getString(10);
        verify(clazz, atLeastOnce()).getString(20);
        verify(annotation1).elementValuesAccept(clazz, converter);
        verify(annotation2).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation with empty class pools.
     * The method should still process annotations even with empty class pools.
     */
    @Test
    public void testVisitAnnotation_withEmptyClassPools() {
        // Arrange - converter already has empty class pools from setUp
        annotation.u2typeIndex = 50;
        when(clazz.getString(50)).thenReturn("Ljava/lang/Override;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converter.visitAnnotation(clazz, annotation)
        );
    }

    /**
     * Tests visitAnnotation across different converter instances.
     * Different converters should independently process annotations.
     */
    @Test
    public void testVisitAnnotation_withDifferentConverters() {
        // Arrange
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        annotation.u2typeIndex = 60;
        when(clazz.getString(60)).thenReturn("Ljava/lang/Deprecated;");

        // Act
        converter.visitAnnotation(clazz, annotation);
        converter2.visitAnnotation(clazz, annotation);

        // Assert - verify both converters processed the annotation
        verify(clazz, atLeast(2)).getString(60);
        verify(annotation, times(1)).elementValuesAccept(clazz, converter);
        verify(annotation, times(1)).elementValuesAccept(clazz, converter2);
    }

    /**
     * Tests visitAnnotation with various standard Java annotations.
     * Different standard annotations should all be processed correctly.
     */
    @Test
    public void testVisitAnnotation_withVariousStandardAnnotations() {
        // Arrange
        Annotation ann1 = mock(Annotation.class);
        Annotation ann2 = mock(Annotation.class);
        Annotation ann3 = mock(Annotation.class);
        Annotation ann4 = mock(Annotation.class);
        Annotation ann5 = mock(Annotation.class);

        ann1.u2typeIndex = 1;
        ann2.u2typeIndex = 2;
        ann3.u2typeIndex = 3;
        ann4.u2typeIndex = 4;
        ann5.u2typeIndex = 5;

        when(clazz.getString(1)).thenReturn("Ljava/lang/Override;");
        when(clazz.getString(2)).thenReturn("Ljava/lang/Deprecated;");
        when(clazz.getString(3)).thenReturn("Ljava/lang/SuppressWarnings;");
        when(clazz.getString(4)).thenReturn("Ljava/lang/FunctionalInterface;");
        when(clazz.getString(5)).thenReturn("Ljava/lang/SafeVarargs;");

        // Act
        converter.visitAnnotation(clazz, ann1);
        converter.visitAnnotation(clazz, ann2);
        converter.visitAnnotation(clazz, ann3);
        converter.visitAnnotation(clazz, ann4);
        converter.visitAnnotation(clazz, ann5);

        // Assert - verify all types were read
        verify(clazz, atLeastOnce()).getString(1);
        verify(clazz, atLeastOnce()).getString(2);
        verify(clazz, atLeastOnce()).getString(3);
        verify(clazz, atLeastOnce()).getString(4);
        verify(clazz, atLeastOnce()).getString(5);
        verify(ann1).elementValuesAccept(clazz, converter);
        verify(ann2).elementValuesAccept(clazz, converter);
        verify(ann3).elementValuesAccept(clazz, converter);
        verify(ann4).elementValuesAccept(clazz, converter);
        verify(ann5).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation with various meta-annotations.
     * Meta-annotations used to define other annotations should be processed correctly.
     */
    @Test
    public void testVisitAnnotation_withMetaAnnotations() {
        // Arrange
        Annotation ann1 = mock(Annotation.class);
        Annotation ann2 = mock(Annotation.class);
        Annotation ann3 = mock(Annotation.class);
        Annotation ann4 = mock(Annotation.class);

        ann1.u2typeIndex = 1;
        ann2.u2typeIndex = 2;
        ann3.u2typeIndex = 3;
        ann4.u2typeIndex = 4;

        when(clazz.getString(1)).thenReturn("Ljava/lang/annotation/Retention;");
        when(clazz.getString(2)).thenReturn("Ljava/lang/annotation/Target;");
        when(clazz.getString(3)).thenReturn("Ljava/lang/annotation/Documented;");
        when(clazz.getString(4)).thenReturn("Ljava/lang/annotation/Inherited;");

        // Act
        converter.visitAnnotation(clazz, ann1);
        converter.visitAnnotation(clazz, ann2);
        converter.visitAnnotation(clazz, ann3);
        converter.visitAnnotation(clazz, ann4);

        // Assert - verify all meta-annotation types were read
        verify(clazz, atLeastOnce()).getString(1);
        verify(clazz, atLeastOnce()).getString(2);
        verify(clazz, atLeastOnce()).getString(3);
        verify(clazz, atLeastOnce()).getString(4);
        verify(ann1).elementValuesAccept(clazz, converter);
        verify(ann2).elementValuesAccept(clazz, converter);
        verify(ann3).elementValuesAccept(clazz, converter);
        verify(ann4).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation executes quickly.
     * Since it's processing annotations with delegation, it should have minimal overhead.
     */
    @Test
    public void testVisitAnnotation_executesQuickly() {
        // Arrange
        annotation.u2typeIndex = 100;
        when(clazz.getString(100)).thenReturn("Ljava/lang/Override;");
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitAnnotation(clazz, annotation);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 200ms for 1000 calls)
        assertTrue(durationMs < 200,
            "visitAnnotation should execute quickly");
    }

    /**
     * Tests visitAnnotation with custom framework annotations.
     * Framework-specific annotations should be processed correctly.
     */
    @Test
    public void testVisitAnnotation_withFrameworkAnnotations() {
        // Arrange
        Annotation ann1 = mock(Annotation.class);
        Annotation ann2 = mock(Annotation.class);
        Annotation ann3 = mock(Annotation.class);

        ann1.u2typeIndex = 1;
        ann2.u2typeIndex = 2;
        ann3.u2typeIndex = 3;

        when(clazz.getString(1)).thenReturn("Ljavax/inject/Inject;");
        when(clazz.getString(2)).thenReturn("Ljavax/annotation/Nullable;");
        when(clazz.getString(3)).thenReturn("Ljavax/annotation/Nonnull;");

        // Act
        converter.visitAnnotation(clazz, ann1);
        converter.visitAnnotation(clazz, ann2);
        converter.visitAnnotation(clazz, ann3);

        // Assert - verify all framework annotation types were read
        verify(clazz, atLeastOnce()).getString(1);
        verify(clazz, atLeastOnce()).getString(2);
        verify(clazz, atLeastOnce()).getString(3);
        verify(ann1).elementValuesAccept(clazz, converter);
        verify(ann2).elementValuesAccept(clazz, converter);
        verify(ann3).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation with custom domain-specific annotations.
     * Domain-specific annotations should be processed correctly.
     */
    @Test
    public void testVisitAnnotation_withDomainSpecificAnnotations() {
        // Arrange
        Annotation ann1 = mock(Annotation.class);
        Annotation ann2 = mock(Annotation.class);
        Annotation ann3 = mock(Annotation.class);

        ann1.u2typeIndex = 1;
        ann2.u2typeIndex = 2;
        ann3.u2typeIndex = 3;

        when(clazz.getString(1)).thenReturn("Lcom/example/Entity;");
        when(clazz.getString(2)).thenReturn("Lcom/example/Repository;");
        when(clazz.getString(3)).thenReturn("Lcom/example/Controller;");

        // Act
        converter.visitAnnotation(clazz, ann1);
        converter.visitAnnotation(clazz, ann2);
        converter.visitAnnotation(clazz, ann3);

        // Assert - verify all custom annotation types were read
        verify(clazz, atLeastOnce()).getString(1);
        verify(clazz, atLeastOnce()).getString(2);
        verify(clazz, atLeastOnce()).getString(3);
        verify(ann1).elementValuesAccept(clazz, converter);
        verify(ann2).elementValuesAccept(clazz, converter);
        verify(ann3).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests that visitAnnotation correctly updates type index and delegates.
     * This is a comprehensive test verifying both responsibilities of the method.
     */
    @Test
    public void testVisitAnnotation_updatesTypeIndexAndDelegates() {
        // Arrange
        int typeIndex = 150;
        annotation.u2typeIndex = typeIndex;
        when(clazz.getString(typeIndex)).thenReturn("Ljava/lang/Override;");

        // Act
        converter.visitAnnotation(clazz, annotation);

        // Assert - verify both reading the type and delegating to element values
        verify(clazz, atLeastOnce()).getString(typeIndex);
        verify(annotation).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation with repeated calls on the same annotation.
     * Each call should process the annotation consistently.
     */
    @Test
    public void testVisitAnnotation_repeatedCallsOnSameAnnotation() {
        // Arrange
        int typeIndex = 200;
        annotation.u2typeIndex = typeIndex;
        when(clazz.getString(typeIndex)).thenReturn("Ljava/lang/Deprecated;");

        // Act
        converter.visitAnnotation(clazz, annotation);
        converter.visitAnnotation(clazz, annotation);

        // Assert - verify consistent processing
        verify(clazz, atLeast(2)).getString(typeIndex);
        verify(annotation, times(2)).elementValuesAccept(clazz, converter);
    }

    /**
     * Tests visitAnnotation passes correct clazz to elementValuesAccept.
     * The clazz parameter should be passed through correctly to the delegation.
     */
    @Test
    public void testVisitAnnotation_passesCorrectClazzToElementValuesAccept() {
        // Arrange
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");
        annotation.u2typeIndex = 250;
        when(specificClazz.getString(250)).thenReturn("Ljava/lang/Override;");

        // Act
        converter.visitAnnotation(specificClazz, annotation);

        // Assert - verify the same clazz is passed to elementValuesAccept
        verify(annotation).elementValuesAccept(same(specificClazz), any(ElementValueVisitor.class));
    }

    /**
     * Tests visitAnnotation properly integrates both update and delegation.
     * This verifies the complete flow of the method.
     */
    @Test
    public void testVisitAnnotation_integratesUpdateAndDelegation() {
        // Arrange
        Annotation specificAnnotation = mock(Annotation.class, "specificAnnotation");
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");

        specificAnnotation.u2typeIndex = 300;
        when(specificClazz.getString(300)).thenReturn("Lcom/example/CustomAnnotation;");

        // Act
        converter.visitAnnotation(specificClazz, specificAnnotation);

        // Assert - verify complete flow
        verify(specificClazz, atLeastOnce()).getString(300);
        verify(specificAnnotation).elementValuesAccept(specificClazz, converter);
    }
}

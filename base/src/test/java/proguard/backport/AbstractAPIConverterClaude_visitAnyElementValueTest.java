package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.ElementValue;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitAnyElementValue(Clazz, Annotation, ElementValue)}.
 *
 * The visitAnyElementValue method is a no-op (empty) method that serves as the default
 * implementation for element value processing. Specific element value types (like
 * EnumConstantElementValue, ClassElementValue, AnnotationElementValue) have their own
 * overridden implementations. This method is called for element values that don't need
 * special processing.
 */
public class AbstractAPIConverterClaude_visitAnyElementValueTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Annotation annotation;
    private ElementValue elementValue;

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
        elementValue = mock(ElementValue.class);
    }

    /**
     * Tests that visitAnyElementValue can be called without throwing exceptions.
     * This verifies the method executes successfully as a no-op.
     */
    @Test
    public void testVisitAnyElementValue_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyElementValue(clazz, annotation, elementValue));
    }

    /**
     * Tests that visitAnyElementValue does not interact with the clazz parameter.
     * Since the method is a no-op, it should not read or modify the clazz.
     */
    @Test
    public void testVisitAnyElementValue_doesNotInteractWithClazz() {
        // Act
        converter.visitAnyElementValue(clazz, annotation, elementValue);

        // Assert - verify no interactions with clazz
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyElementValue does not interact with the annotation parameter.
     * Since the method is a no-op, it should not read or modify the annotation.
     */
    @Test
    public void testVisitAnyElementValue_doesNotInteractWithAnnotation() {
        // Act
        converter.visitAnyElementValue(clazz, annotation, elementValue);

        // Assert - verify no interactions with annotation
        verifyNoInteractions(annotation);
    }

    /**
     * Tests that visitAnyElementValue does not interact with the elementValue parameter.
     * Since the method is a no-op, it should not read or modify the element value.
     */
    @Test
    public void testVisitAnyElementValue_doesNotInteractWithElementValue() {
        // Act
        converter.visitAnyElementValue(clazz, annotation, elementValue);

        // Assert - verify no interactions with element value
        verifyNoInteractions(elementValue);
    }

    /**
     * Tests that visitAnyElementValue does not trigger warnings.
     * A no-op method should not generate any warnings.
     */
    @Test
    public void testVisitAnyElementValue_doesNotTriggerWarnings() {
        // Act
        converter.visitAnyElementValue(clazz, annotation, elementValue);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests that visitAnyElementValue does not trigger the modified class visitor.
     * A no-op method should not mark the class as modified.
     */
    @Test
    public void testVisitAnyElementValue_doesNotTriggerModifiedClassVisitor() {
        // Act
        converter.visitAnyElementValue(clazz, annotation, elementValue);

        // Assert - verify the modified class visitor was not invoked
        verifyNoInteractions(modifiedClassVisitor);
    }

    /**
     * Tests that visitAnyElementValue does not trigger the extra instruction visitor.
     * A no-op method should not interact with the instruction visitor.
     */
    @Test
    public void testVisitAnyElementValue_doesNotTriggerExtraInstructionVisitor() {
        // Act
        converter.visitAnyElementValue(clazz, annotation, elementValue);

        // Assert - verify the extra instruction visitor was not invoked
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests that visitAnyElementValue can be called multiple times without side effects.
     * Since it's a no-op, multiple calls should remain safe.
     */
    @Test
    public void testVisitAnyElementValue_calledMultipleTimes_noSideEffects() {
        // Act
        converter.visitAnyElementValue(clazz, annotation, elementValue);
        converter.visitAnyElementValue(clazz, annotation, elementValue);
        converter.visitAnyElementValue(clazz, annotation, elementValue);

        // Assert - verify no interactions occurred with any parameters
        verifyNoInteractions(clazz);
        verifyNoInteractions(annotation);
        verifyNoInteractions(elementValue);
    }

    /**
     * Tests that visitAnyElementValue works with different element value instances.
     * The no-op behavior should be consistent across different instances.
     */
    @Test
    public void testVisitAnyElementValue_withDifferentElementValues() {
        // Arrange
        ElementValue elementValue1 = mock(ElementValue.class);
        ElementValue elementValue2 = mock(ElementValue.class);
        ElementValue elementValue3 = mock(ElementValue.class);

        // Act
        converter.visitAnyElementValue(clazz, annotation, elementValue1);
        converter.visitAnyElementValue(clazz, annotation, elementValue2);
        converter.visitAnyElementValue(clazz, annotation, elementValue3);

        // Assert - verify no interactions occurred with any element values
        verifyNoInteractions(elementValue1);
        verifyNoInteractions(elementValue2);
        verifyNoInteractions(elementValue3);
    }

    /**
     * Tests that visitAnyElementValue works with different annotation instances.
     * The no-op behavior should be consistent across different annotations.
     */
    @Test
    public void testVisitAnyElementValue_withDifferentAnnotations() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        Annotation annotation3 = mock(Annotation.class);

        // Act
        converter.visitAnyElementValue(clazz, annotation1, elementValue);
        converter.visitAnyElementValue(clazz, annotation2, elementValue);
        converter.visitAnyElementValue(clazz, annotation3, elementValue);

        // Assert - verify no interactions occurred with any annotations
        verifyNoInteractions(annotation1);
        verifyNoInteractions(annotation2);
        verifyNoInteractions(annotation3);
    }

    /**
     * Tests that visitAnyElementValue works with different clazz instances.
     * The no-op behavior should be consistent across different classes.
     */
    @Test
    public void testVisitAnyElementValue_withDifferentClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act
        converter.visitAnyElementValue(clazz1, annotation, elementValue);
        converter.visitAnyElementValue(clazz2, annotation, elementValue);
        converter.visitAnyElementValue(clazz3, annotation, elementValue);

        // Assert - verify no interactions occurred with any classes
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(clazz3);
    }

    /**
     * Tests visitAnyElementValue with a converter with null warning printer.
     * The no-op method should work even with null optional dependencies.
     */
    @Test
    public void testVisitAnyElementValue_withNullWarningPrinter() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullPrinter.visitAnyElementValue(clazz, annotation, elementValue)
        );
    }

    /**
     * Tests visitAnyElementValue with a converter with null class visitor.
     * The no-op method should work even with null optional dependencies.
     */
    @Test
    public void testVisitAnyElementValue_withNullClassVisitor() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullVisitor.visitAnyElementValue(clazz, annotation, elementValue)
        );
    }

    /**
     * Tests visitAnyElementValue with a converter with null instruction visitor.
     * The no-op method should work even with null optional dependencies.
     */
    @Test
    public void testVisitAnyElementValue_withNullInstructionVisitor() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullInstrVisitor.visitAnyElementValue(clazz, annotation, elementValue)
        );
    }

    /**
     * Tests visitAnyElementValue with all null optional dependencies.
     * The no-op method should work even with all optional dependencies null.
     */
    @Test
    public void testVisitAnyElementValue_withAllNullDependencies() {
        // Arrange
        TestAPIConverter converterWithAllNulls = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            null, // null class visitor
            null  // null instruction visitor
        );

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithAllNulls.visitAnyElementValue(clazz, annotation, elementValue)
        );
    }

    /**
     * Tests that visitAnyElementValue executes extremely quickly.
     * Since it's a no-op, it should have virtually no overhead.
     */
    @Test
    public void testVisitAnyElementValue_executesVeryQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 100000; i++) {
            converter.visitAnyElementValue(clazz, annotation, elementValue);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 100000 calls)
        assertTrue(durationMs < 100,
            "visitAnyElementValue should execute very quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyElementValue handles null clazz parameter.
     * Even with null parameters, the no-op method should not throw (though this is
     * not recommended in practice).
     */
    @Test
    public void testVisitAnyElementValue_withNullClazz_doesNotThrow() {
        // Act & Assert - should not throw even with null clazz
        assertDoesNotThrow(() ->
            converter.visitAnyElementValue(null, annotation, elementValue)
        );
    }

    /**
     * Tests that visitAnyElementValue handles null annotation parameter.
     * Even with null parameters, the no-op method should not throw.
     */
    @Test
    public void testVisitAnyElementValue_withNullAnnotation_doesNotThrow() {
        // Act & Assert - should not throw even with null annotation
        assertDoesNotThrow(() ->
            converter.visitAnyElementValue(clazz, null, elementValue)
        );
    }

    /**
     * Tests that visitAnyElementValue handles null elementValue parameter.
     * Even with null parameters, the no-op method should not throw.
     */
    @Test
    public void testVisitAnyElementValue_withNullElementValue_doesNotThrow() {
        // Act & Assert - should not throw even with null element value
        assertDoesNotThrow(() ->
            converter.visitAnyElementValue(clazz, annotation, null)
        );
    }

    /**
     * Tests that visitAnyElementValue handles all null parameters.
     * Even with all null parameters, the no-op method should not throw.
     */
    @Test
    public void testVisitAnyElementValue_withAllNullParameters_doesNotThrow() {
        // Act & Assert - should not throw even with all null parameters
        assertDoesNotThrow(() ->
            converter.visitAnyElementValue(null, null, null)
        );
    }

    /**
     * Tests visitAnyElementValue with empty class pools.
     * The no-op method should work with empty class pools.
     */
    @Test
    public void testVisitAnyElementValue_withEmptyClassPools() {
        // Arrange - converter already has empty class pools from setUp

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converter.visitAnyElementValue(clazz, annotation, elementValue)
        );

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(annotation);
        verifyNoInteractions(elementValue);
    }

    /**
     * Tests visitAnyElementValue across different converter instances.
     * Different converters should all exhibit the same no-op behavior.
     */
    @Test
    public void testVisitAnyElementValue_withDifferentConverters() {
        // Arrange
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act
        converter.visitAnyElementValue(clazz, annotation, elementValue);
        converter2.visitAnyElementValue(clazz, annotation, elementValue);

        // Assert - verify no interactions occurred with either converter
        verifyNoInteractions(clazz);
        verifyNoInteractions(annotation);
        verifyNoInteractions(elementValue);
    }

    /**
     * Tests that visitAnyElementValue maintains no-op behavior in all contexts.
     * This comprehensive test verifies the no-op behavior with various parameter combinations.
     */
    @Test
    public void testVisitAnyElementValue_noOpBehaviorInAllContexts() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Annotation ann1 = mock(Annotation.class);
        Annotation ann2 = mock(Annotation.class);
        ElementValue elem1 = mock(ElementValue.class);
        ElementValue elem2 = mock(ElementValue.class);

        // Act - call with various combinations
        converter.visitAnyElementValue(clazz1, ann1, elem1);
        converter.visitAnyElementValue(clazz1, ann1, elem2);
        converter.visitAnyElementValue(clazz1, ann2, elem1);
        converter.visitAnyElementValue(clazz1, ann2, elem2);
        converter.visitAnyElementValue(clazz2, ann1, elem1);
        converter.visitAnyElementValue(clazz2, ann1, elem2);
        converter.visitAnyElementValue(clazz2, ann2, elem1);
        converter.visitAnyElementValue(clazz2, ann2, elem2);

        // Assert - verify no interactions occurred with any parameters
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
        verifyNoInteractions(ann1);
        verifyNoInteractions(ann2);
        verifyNoInteractions(elem1);
        verifyNoInteractions(elem2);
    }

    /**
     * Tests that visitAnyElementValue is truly a no-op by verifying no state changes.
     * This test ensures the method doesn't have any hidden side effects.
     */
    @Test
    public void testVisitAnyElementValue_noStateChanges() {
        // Arrange
        TestAPIConverter converterSpy = spy(converter);

        // Act
        converterSpy.visitAnyElementValue(clazz, annotation, elementValue);

        // Assert - verify only visitAnyElementValue was called, no other methods
        verify(converterSpy, times(1)).visitAnyElementValue(any(), any(), any());
        verifyNoMoreInteractions(converterSpy);
    }

    /**
     * Tests that visitAnyElementValue can handle concurrent calls safely.
     * Since it's a no-op with no shared state modification, it should be thread-safe.
     */
    @Test
    public void testVisitAnyElementValue_threadSafe() {
        // Arrange
        int threadCount = 10;
        int callsPerThread = 1000;

        // Act - call from multiple threads
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < callsPerThread; j++) {
                    converter.visitAnyElementValue(clazz, annotation, elementValue);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            assertDoesNotThrow(() -> thread.join());
        }

        // Assert - verify no interactions occurred despite concurrent calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(annotation);
        verifyNoInteractions(elementValue);
    }

    /**
     * Tests that visitAnyElementValue serves as the default implementation.
     * This method is called for element values that don't have specialized implementations,
     * such as primitive values (int, string, etc.) that don't need type replacement.
     */
    @Test
    public void testVisitAnyElementValue_servesAsDefaultImplementation() {
        // Arrange
        ElementValue primitiveElement = mock(ElementValue.class, "primitiveElement");
        ElementValue stringElement = mock(ElementValue.class, "stringElement");
        ElementValue booleanElement = mock(ElementValue.class, "booleanElement");

        // Act - these would represent element values that don't need processing
        converter.visitAnyElementValue(clazz, annotation, primitiveElement);
        converter.visitAnyElementValue(clazz, annotation, stringElement);
        converter.visitAnyElementValue(clazz, annotation, booleanElement);

        // Assert - verify no processing occurred, which is correct for these types
        verifyNoInteractions(primitiveElement);
        verifyNoInteractions(stringElement);
        verifyNoInteractions(booleanElement);
    }

    /**
     * Tests that visitAnyElementValue has zero memory allocation overhead.
     * Since it's an empty method, it shouldn't allocate any objects.
     */
    @Test
    public void testVisitAnyElementValue_zeroAllocationOverhead() {
        // Act - call multiple times
        for (int i = 0; i < 1000; i++) {
            converter.visitAnyElementValue(clazz, annotation, elementValue);
        }

        // Assert - verify no interactions, implying no object creation for processing
        verifyNoInteractions(clazz);
        verifyNoInteractions(annotation);
        verifyNoInteractions(elementValue);
    }

    /**
     * Tests that visitAnyElementValue maintains consistency across invocations.
     * Every call should behave identically as a no-op.
     */
    @Test
    public void testVisitAnyElementValue_consistentBehavior() {
        // Act - call multiple times and verify consistent behavior
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() ->
                converter.visitAnyElementValue(clazz, annotation, elementValue)
            );
        }

        // Assert - verify no interactions occurred in any of the calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(annotation);
        verifyNoInteractions(elementValue);
    }

    /**
     * Tests that visitAnyElementValue can be used as a base implementation.
     * Subclasses can override this method if they need specific behavior,
     * but the default is to do nothing.
     */
    @Test
    public void testVisitAnyElementValue_usableAsBaseImplementation() {
        // Arrange - the TestAPIConverter doesn't override this method

        // Act
        converter.visitAnyElementValue(clazz, annotation, elementValue);

        // Assert - verify the base implementation (no-op) was used
        verifyNoInteractions(clazz);
        verifyNoInteractions(annotation);
        verifyNoInteractions(elementValue);
        verifyNoInteractions(warningPrinter);
        verifyNoInteractions(modifiedClassVisitor);
        verifyNoInteractions(extraInstructionVisitor);
    }
}

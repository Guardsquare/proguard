package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.ClassElementValue;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitClassElementValue(Clazz, Annotation, ClassElementValue)}.
 *
 * The visitClassElementValue method processes class element values in annotations by:
 * 1. Getting the class name from the classElementValue
 * 2. Calling replaceClassName to potentially replace it based on type replacement rules
 * 3. If the class name changed, marking the class as modified and updating the class info index
 *
 * This is used for annotation element values that reference classes (e.g., @SomeAnnotation(value = String.class)).
 */
public class AbstractAPIConverterClaude_visitClassElementValueTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Annotation annotation;
    private ClassElementValue classElementValue;

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
        classElementValue = mock(ClassElementValue.class);
    }

    /**
     * Tests that visitClassElementValue can be called with valid mock objects without throwing exceptions.
     * This is a smoke test to ensure the method executes successfully.
     */
    @Test
    public void testVisitClassElementValue_withValidMocks_doesNotThrowException() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/String");

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitClassElementValue(clazz, annotation, classElementValue));
    }

    /**
     * Tests visitClassElementValue with a String class reference.
     * String.class is commonly used in annotation values.
     */
    @Test
    public void testVisitClassElementValue_withStringClass() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/String");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert - verify the class name was retrieved
        verify(classElementValue, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with an Object class reference.
     */
    @Test
    public void testVisitClassElementValue_withObjectClass() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/Object");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert
        verify(classElementValue, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with a custom class reference.
     */
    @Test
    public void testVisitClassElementValue_withCustomClass() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("com/example/MyClass");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert
        verify(classElementValue, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with a Class class reference.
     * Class.class is sometimes used in annotations.
     */
    @Test
    public void testVisitClassElementValue_withClassClass() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/Class");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert
        verify(classElementValue, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with an Exception class reference.
     */
    @Test
    public void testVisitClassElementValue_withExceptionClass() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/Exception");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert
        verify(classElementValue, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with a Throwable class reference.
     */
    @Test
    public void testVisitClassElementValue_withThrowableClass() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/Throwable");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert
        verify(classElementValue, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue can be called multiple times.
     * Each call should independently process the class element value.
     */
    @Test
    public void testVisitClassElementValue_calledMultipleTimes() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/String");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);
        converter.visitClassElementValue(clazz, annotation, classElementValue);
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert - verify getClassName was called at least 3 times
        verify(classElementValue, atLeast(3)).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with different class element values.
     * Each instance should have its class name processed independently.
     */
    @Test
    public void testVisitClassElementValue_withDifferentClassElementValues() {
        // Arrange
        ClassElementValue classElem1 = mock(ClassElementValue.class);
        ClassElementValue classElem2 = mock(ClassElementValue.class);
        ClassElementValue classElem3 = mock(ClassElementValue.class);

        when(classElem1.getClassName(clazz)).thenReturn("java/lang/String");
        when(classElem2.getClassName(clazz)).thenReturn("java/lang/Integer");
        when(classElem3.getClassName(clazz)).thenReturn("java/util/List");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElem1);
        converter.visitClassElementValue(clazz, annotation, classElem2);
        converter.visitClassElementValue(clazz, annotation, classElem3);

        // Assert - verify each class name was retrieved
        verify(classElem1, atLeastOnce()).getClassName(clazz);
        verify(classElem2, atLeastOnce()).getClassName(clazz);
        verify(classElem3, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with different clazz instances.
     * Each clazz context should be handled independently.
     */
    @Test
    public void testVisitClassElementValue_withDifferentClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        when(classElementValue.getClassName(clazz1)).thenReturn("java/lang/String");
        when(classElementValue.getClassName(clazz2)).thenReturn("java/lang/Integer");

        // Act
        converter.visitClassElementValue(clazz1, annotation, classElementValue);
        converter.visitClassElementValue(clazz2, annotation, classElementValue);

        // Assert
        verify(classElementValue, atLeastOnce()).getClassName(clazz1);
        verify(classElementValue, atLeastOnce()).getClassName(clazz2);
    }

    /**
     * Tests visitClassElementValue with different annotation instances.
     * Each annotation context should be handled independently.
     */
    @Test
    public void testVisitClassElementValue_withDifferentAnnotations() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);

        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/String");

        // Act
        converter.visitClassElementValue(clazz, annotation1, classElementValue);
        converter.visitClassElementValue(clazz, annotation2, classElementValue);

        // Assert - verify processing occurred for both annotation contexts
        verify(classElementValue, atLeast(2)).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue doesn't directly interact with annotation parameter.
     * The annotation parameter provides context but isn't directly used.
     */
    @Test
    public void testVisitClassElementValue_doesNotDirectlyInteractWithAnnotation() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/String");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert - verify no direct interactions with annotation
        verifyNoInteractions(annotation);
    }

    /**
     * Tests visitClassElementValue doesn't trigger warnings for standard classes.
     * Processing standard class references should not generate warnings.
     */
    @Test
    public void testVisitClassElementValue_doesNotTriggerWarnings() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/String");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests visitClassElementValue with a converter with null warning printer.
     * The method should still process class element values correctly even with null optional dependencies.
     */
    @Test
    public void testVisitClassElementValue_withNullWarningPrinter() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/String");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullPrinter.visitClassElementValue(clazz, annotation, classElementValue)
        );
    }

    /**
     * Tests visitClassElementValue with a converter with null class visitor.
     * The method should still process class element values correctly even with null optional dependencies.
     */
    @Test
    public void testVisitClassElementValue_withNullClassVisitor() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/Integer");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullVisitor.visitClassElementValue(clazz, annotation, classElementValue)
        );
    }

    /**
     * Tests visitClassElementValue with a converter with null instruction visitor.
     * The method should still process class element values correctly even with null optional dependencies.
     */
    @Test
    public void testVisitClassElementValue_withNullInstructionVisitor() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/Object");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullInstrVisitor.visitClassElementValue(clazz, annotation, classElementValue)
        );
    }

    /**
     * Tests visitClassElementValue processes class name by calling getClassName.
     * This is the key interaction - retrieving the class name from the element value.
     */
    @Test
    public void testVisitClassElementValue_retrievesClassNameFromElementValue() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("com/example/MyCustomClass");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert - verify the class name was retrieved
        verify(classElementValue, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with various standard Java classes.
     * Different standard class references should all be processed correctly.
     */
    @Test
    public void testVisitClassElementValue_withVariousStandardClasses() {
        // Arrange
        ClassElementValue elem1 = mock(ClassElementValue.class);
        ClassElementValue elem2 = mock(ClassElementValue.class);
        ClassElementValue elem3 = mock(ClassElementValue.class);
        ClassElementValue elem4 = mock(ClassElementValue.class);
        ClassElementValue elem5 = mock(ClassElementValue.class);

        when(elem1.getClassName(clazz)).thenReturn("java/lang/String");
        when(elem2.getClassName(clazz)).thenReturn("java/lang/Integer");
        when(elem3.getClassName(clazz)).thenReturn("java/lang/Boolean");
        when(elem4.getClassName(clazz)).thenReturn("java/lang/Double");
        when(elem5.getClassName(clazz)).thenReturn("java/lang/Long");

        // Act
        converter.visitClassElementValue(clazz, annotation, elem1);
        converter.visitClassElementValue(clazz, annotation, elem2);
        converter.visitClassElementValue(clazz, annotation, elem3);
        converter.visitClassElementValue(clazz, annotation, elem4);
        converter.visitClassElementValue(clazz, annotation, elem5);

        // Assert - verify all class names were retrieved
        verify(elem1, atLeastOnce()).getClassName(clazz);
        verify(elem2, atLeastOnce()).getClassName(clazz);
        verify(elem3, atLeastOnce()).getClassName(clazz);
        verify(elem4, atLeastOnce()).getClassName(clazz);
        verify(elem5, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue executes quickly.
     * Since it's processing class references, it should have minimal overhead.
     */
    @Test
    public void testVisitClassElementValue_executesQuickly() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/String");
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitClassElementValue(clazz, annotation, classElementValue);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 200ms for 1000 calls)
        assertTrue(durationMs < 200,
            "visitClassElementValue should execute quickly");
    }

    /**
     * Tests visitClassElementValue handles sequential calls independently.
     * Each call should process the class name without interference from previous calls.
     */
    @Test
    public void testVisitClassElementValue_sequentialCallsAreIndependent() {
        // Arrange
        ClassElementValue elem1 = mock(ClassElementValue.class);
        ClassElementValue elem2 = mock(ClassElementValue.class);

        when(elem1.getClassName(clazz)).thenReturn("java/lang/String");
        when(elem2.getClassName(clazz)).thenReturn("java/lang/Integer");

        // Act
        converter.visitClassElementValue(clazz, annotation, elem1);
        converter.visitClassElementValue(clazz, annotation, elem2);

        // Assert - verify both were processed independently
        verify(elem1, atLeastOnce()).getClassName(clazz);
        verify(elem2, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with empty class pools.
     * The method should still process class element values even with empty class pools.
     */
    @Test
    public void testVisitClassElementValue_withEmptyClassPools() {
        // Arrange - converter already has empty class pools from setUp
        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/String");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converter.visitClassElementValue(clazz, annotation, classElementValue)
        );
    }

    /**
     * Tests visitClassElementValue across different converter instances.
     * Different converters should independently process class element values.
     */
    @Test
    public void testVisitClassElementValue_withDifferentConverters() {
        // Arrange
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/String");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);
        converter2.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert - verify both converters processed the class name
        verify(classElementValue, atLeast(2)).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with collection class references.
     * Collection classes are commonly used in annotation values.
     */
    @Test
    public void testVisitClassElementValue_withCollectionClasses() {
        // Arrange
        ClassElementValue elem1 = mock(ClassElementValue.class);
        ClassElementValue elem2 = mock(ClassElementValue.class);
        ClassElementValue elem3 = mock(ClassElementValue.class);
        ClassElementValue elem4 = mock(ClassElementValue.class);

        when(elem1.getClassName(clazz)).thenReturn("java/util/List");
        when(elem2.getClassName(clazz)).thenReturn("java/util/Set");
        when(elem3.getClassName(clazz)).thenReturn("java/util/Map");
        when(elem4.getClassName(clazz)).thenReturn("java/util/Collection");

        // Act
        converter.visitClassElementValue(clazz, annotation, elem1);
        converter.visitClassElementValue(clazz, annotation, elem2);
        converter.visitClassElementValue(clazz, annotation, elem3);
        converter.visitClassElementValue(clazz, annotation, elem4);

        // Assert - verify all collection class names were retrieved
        verify(elem1, atLeastOnce()).getClassName(clazz);
        verify(elem2, atLeastOnce()).getClassName(clazz);
        verify(elem3, atLeastOnce()).getClassName(clazz);
        verify(elem4, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with array class references.
     * Array classes can be used in annotation values.
     */
    @Test
    public void testVisitClassElementValue_withArrayClasses() {
        // Arrange
        ClassElementValue elem1 = mock(ClassElementValue.class);
        ClassElementValue elem2 = mock(ClassElementValue.class);

        when(elem1.getClassName(clazz)).thenReturn("[Ljava/lang/String;");
        when(elem2.getClassName(clazz)).thenReturn("[[I");

        // Act
        converter.visitClassElementValue(clazz, annotation, elem1);
        converter.visitClassElementValue(clazz, annotation, elem2);

        // Assert
        verify(elem1, atLeastOnce()).getClassName(clazz);
        verify(elem2, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with inner class references.
     * Inner classes are commonly referenced in annotations.
     */
    @Test
    public void testVisitClassElementValue_withInnerClass() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("com/example/OuterClass$InnerClass");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert
        verify(classElementValue, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with annotation class references.
     * Annotation types themselves can be referenced.
     */
    @Test
    public void testVisitClassElementValue_withAnnotationClass() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/annotation/Retention");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert
        verify(classElementValue, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with interface class references.
     * Interfaces can be referenced in annotation values.
     */
    @Test
    public void testVisitClassElementValue_withInterfaceClass() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("java/io/Serializable");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert
        verify(classElementValue, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with abstract class references.
     */
    @Test
    public void testVisitClassElementValue_withAbstractClass() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("java/io/InputStream");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert
        verify(classElementValue, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with exception class references.
     * Exception classes are commonly used in annotation values.
     */
    @Test
    public void testVisitClassElementValue_withVariousExceptionClasses() {
        // Arrange
        ClassElementValue elem1 = mock(ClassElementValue.class);
        ClassElementValue elem2 = mock(ClassElementValue.class);
        ClassElementValue elem3 = mock(ClassElementValue.class);

        when(elem1.getClassName(clazz)).thenReturn("java/lang/Exception");
        when(elem2.getClassName(clazz)).thenReturn("java/io/IOException");
        when(elem3.getClassName(clazz)).thenReturn("java/lang/RuntimeException");

        // Act
        converter.visitClassElementValue(clazz, annotation, elem1);
        converter.visitClassElementValue(clazz, annotation, elem2);
        converter.visitClassElementValue(clazz, annotation, elem3);

        // Assert
        verify(elem1, atLeastOnce()).getClassName(clazz);
        verify(elem2, atLeastOnce()).getClassName(clazz);
        verify(elem3, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with repeated calls on the same class element.
     * Each call should process the class name consistently.
     */
    @Test
    public void testVisitClassElementValue_repeatedCallsOnSameElement() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/String");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert - verify consistent processing
        verify(classElementValue, atLeast(2)).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with classes from different packages.
     * Classes from various packages should all be processed correctly.
     */
    @Test
    public void testVisitClassElementValue_withClassesFromDifferentPackages() {
        // Arrange
        ClassElementValue elem1 = mock(ClassElementValue.class);
        ClassElementValue elem2 = mock(ClassElementValue.class);
        ClassElementValue elem3 = mock(ClassElementValue.class);
        ClassElementValue elem4 = mock(ClassElementValue.class);

        when(elem1.getClassName(clazz)).thenReturn("java/lang/String");
        when(elem2.getClassName(clazz)).thenReturn("javax/servlet/http/HttpServlet");
        when(elem3.getClassName(clazz)).thenReturn("org/springframework/stereotype/Controller");
        when(elem4.getClassName(clazz)).thenReturn("com/example/custom/MyClass");

        // Act
        converter.visitClassElementValue(clazz, annotation, elem1);
        converter.visitClassElementValue(clazz, annotation, elem2);
        converter.visitClassElementValue(clazz, annotation, elem3);
        converter.visitClassElementValue(clazz, annotation, elem4);

        // Assert - verify all class names were retrieved
        verify(elem1, atLeastOnce()).getClassName(clazz);
        verify(elem2, atLeastOnce()).getClassName(clazz);
        verify(elem3, atLeastOnce()).getClassName(clazz);
        verify(elem4, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with functional interface references.
     * Functional interfaces like Runnable, Callable can be referenced.
     */
    @Test
    public void testVisitClassElementValue_withFunctionalInterfaces() {
        // Arrange
        ClassElementValue elem1 = mock(ClassElementValue.class);
        ClassElementValue elem2 = mock(ClassElementValue.class);
        ClassElementValue elem3 = mock(ClassElementValue.class);

        when(elem1.getClassName(clazz)).thenReturn("java/lang/Runnable");
        when(elem2.getClassName(clazz)).thenReturn("java/util/concurrent/Callable");
        when(elem3.getClassName(clazz)).thenReturn("java/util/function/Function");

        // Act
        converter.visitClassElementValue(clazz, annotation, elem1);
        converter.visitClassElementValue(clazz, annotation, elem2);
        converter.visitClassElementValue(clazz, annotation, elem3);

        // Assert
        verify(elem1, atLeastOnce()).getClassName(clazz);
        verify(elem2, atLeastOnce()).getClassName(clazz);
        verify(elem3, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue properly integrates class name retrieval.
     * This verifies the complete flow of the method.
     */
    @Test
    public void testVisitClassElementValue_retrievesClassName() {
        // Arrange
        ClassElementValue specificElement = mock(ClassElementValue.class);
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");

        when(specificElement.getClassName(specificClazz)).thenReturn("com/example/MyCustomClass");

        // Act
        converter.visitClassElementValue(specificClazz, annotation, specificElement);

        // Assert - verify complete flow
        verify(specificElement, atLeastOnce()).getClassName(specificClazz);
    }

    /**
     * Tests visitClassElementValue with primitive wrapper classes.
     * Wrapper classes are commonly used in annotations.
     */
    @Test
    public void testVisitClassElementValue_withPrimitiveWrappers() {
        // Arrange
        ClassElementValue elem1 = mock(ClassElementValue.class);
        ClassElementValue elem2 = mock(ClassElementValue.class);
        ClassElementValue elem3 = mock(ClassElementValue.class);
        ClassElementValue elem4 = mock(ClassElementValue.class);

        when(elem1.getClassName(clazz)).thenReturn("java/lang/Integer");
        when(elem2.getClassName(clazz)).thenReturn("java/lang/Long");
        when(elem3.getClassName(clazz)).thenReturn("java/lang/Boolean");
        when(elem4.getClassName(clazz)).thenReturn("java/lang/Character");

        // Act
        converter.visitClassElementValue(clazz, annotation, elem1);
        converter.visitClassElementValue(clazz, annotation, elem2);
        converter.visitClassElementValue(clazz, annotation, elem3);
        converter.visitClassElementValue(clazz, annotation, elem4);

        // Assert
        verify(elem1, atLeastOnce()).getClassName(clazz);
        verify(elem2, atLeastOnce()).getClassName(clazz);
        verify(elem3, atLeastOnce()).getClassName(clazz);
        verify(elem4, atLeastOnce()).getClassName(clazz);
    }

    /**
     * Tests visitClassElementValue with Void class reference.
     * Void.class is sometimes used in annotations for methods with no return type.
     */
    @Test
    public void testVisitClassElementValue_withVoidClass() {
        // Arrange
        when(classElementValue.getClassName(clazz)).thenReturn("java/lang/Void");

        // Act
        converter.visitClassElementValue(clazz, annotation, classElementValue);

        // Assert
        verify(classElementValue, atLeastOnce()).getClassName(clazz);
    }
}

package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitClassConstant(Clazz, ClassConstant)}.
 *
 * The visitClassConstant method processes class constants in the constant pool by:
 * 1. Getting the class name from the ClassConstant
 * 2. Calling replaceClassName to potentially replace it based on type replacement rules
 * 3. If the class name changed, updating the name index using constantPoolEditor and marking class as modified
 *
 * This is used to replace class type references throughout the bytecode when backporting APIs.
 */
public class AbstractAPIConverterClaude_visitClassConstantTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private ProgramClass clazz;
    private ClassConstant classConstant;

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
        classConstant = mock(ClassConstant.class);
    }

    /**
     * Tests that visitClassConstant can be called with valid mock objects without throwing exceptions.
     * This is a smoke test to ensure the method executes successfully.
     */
    @Test
    public void testVisitClassConstant_withValidMocks_doesNotThrowException() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/lang/String");

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitClassConstant(clazz, classConstant));
    }

    /**
     * Tests visitClassConstant with a String class constant.
     * String is one of the most common class references.
     */
    @Test
    public void testVisitClassConstant_withStringClass() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/lang/String");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert - verify the class name was retrieved
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with an Object class constant.
     */
    @Test
    public void testVisitClassConstant_withObjectClass() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/lang/Object");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with a custom class constant.
     */
    @Test
    public void testVisitClassConstant_withCustomClass() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("com/example/MyClass");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with a Class class constant.
     * java.lang.Class is commonly referenced in reflection code.
     */
    @Test
    public void testVisitClassConstant_withClassClass() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/lang/Class");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with an Exception class constant.
     */
    @Test
    public void testVisitClassConstant_withExceptionClass() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/lang/Exception");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with a Throwable class constant.
     */
    @Test
    public void testVisitClassConstant_withThrowableClass() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/lang/Throwable");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant can be called multiple times.
     * Each call should independently process the class constant.
     */
    @Test
    public void testVisitClassConstant_calledMultipleTimes() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/lang/String");

        // Act
        converter.visitClassConstant(clazz, classConstant);
        converter.visitClassConstant(clazz, classConstant);
        converter.visitClassConstant(clazz, classConstant);

        // Assert - verify getName was called at least 3 times
        verify(classConstant, atLeast(3)).getName(clazz);
    }

    /**
     * Tests visitClassConstant with different class constant instances.
     * Each instance should have its class name processed independently.
     */
    @Test
    public void testVisitClassConstant_withDifferentClassConstants() {
        // Arrange
        ClassConstant classConst1 = mock(ClassConstant.class);
        ClassConstant classConst2 = mock(ClassConstant.class);
        ClassConstant classConst3 = mock(ClassConstant.class);

        when(classConst1.getName(clazz)).thenReturn("java/lang/String");
        when(classConst2.getName(clazz)).thenReturn("java/lang/Integer");
        when(classConst3.getName(clazz)).thenReturn("java/util/List");

        // Act
        converter.visitClassConstant(clazz, classConst1);
        converter.visitClassConstant(clazz, classConst2);
        converter.visitClassConstant(clazz, classConst3);

        // Assert - verify each class name was retrieved
        verify(classConst1, atLeastOnce()).getName(clazz);
        verify(classConst2, atLeastOnce()).getName(clazz);
        verify(classConst3, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with different clazz instances.
     * Each clazz context should be handled independently.
     */
    @Test
    public void testVisitClassConstant_withDifferentClazz() {
        // Arrange
        ProgramClass clazz1 = mock(ProgramClass.class);
        ProgramClass clazz2 = mock(ProgramClass.class);

        when(classConstant.getName(clazz1)).thenReturn("java/lang/String");
        when(classConstant.getName(clazz2)).thenReturn("java/lang/Integer");

        // Act
        converter.visitClassConstant(clazz1, classConstant);
        converter.visitClassConstant(clazz2, classConstant);

        // Assert
        verify(classConstant, atLeastOnce()).getName(clazz1);
        verify(classConstant, atLeastOnce()).getName(clazz2);
    }

    /**
     * Tests visitClassConstant doesn't trigger warnings for standard classes without replacement.
     * Processing standard class references should not generate warnings.
     */
    @Test
    public void testVisitClassConstant_doesNotTriggerWarnings() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/lang/String");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests visitClassConstant with a converter with null warning printer.
     * The method should still process class constants correctly even with null optional dependencies.
     */
    @Test
    public void testVisitClassConstant_withNullWarningPrinter() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        when(classConstant.getName(clazz)).thenReturn("java/lang/String");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullPrinter.visitClassConstant(clazz, classConstant)
        );
    }

    /**
     * Tests visitClassConstant with a converter with null class visitor.
     * The method should still process class constants correctly even with null optional dependencies.
     */
    @Test
    public void testVisitClassConstant_withNullClassVisitor() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        when(classConstant.getName(clazz)).thenReturn("java/lang/Integer");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullVisitor.visitClassConstant(clazz, classConstant)
        );
    }

    /**
     * Tests visitClassConstant with a converter with null instruction visitor.
     * The method should still process class constants correctly even with null optional dependencies.
     */
    @Test
    public void testVisitClassConstant_withNullInstructionVisitor() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        when(classConstant.getName(clazz)).thenReturn("java/lang/Object");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullInstrVisitor.visitClassConstant(clazz, classConstant)
        );
    }

    /**
     * Tests visitClassConstant processes class name by calling getName.
     * This is the key interaction - retrieving the class name from the constant.
     */
    @Test
    public void testVisitClassConstant_retrievesClassNameFromConstant() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("com/example/MyCustomClass");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert - verify the class name was retrieved
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with various standard Java classes.
     * Different standard class references should all be processed correctly.
     */
    @Test
    public void testVisitClassConstant_withVariousStandardClasses() {
        // Arrange
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);
        ClassConstant const3 = mock(ClassConstant.class);
        ClassConstant const4 = mock(ClassConstant.class);
        ClassConstant const5 = mock(ClassConstant.class);

        when(const1.getName(clazz)).thenReturn("java/lang/String");
        when(const2.getName(clazz)).thenReturn("java/lang/Integer");
        when(const3.getName(clazz)).thenReturn("java/lang/Boolean");
        when(const4.getName(clazz)).thenReturn("java/lang/Double");
        when(const5.getName(clazz)).thenReturn("java/lang/Long");

        // Act
        converter.visitClassConstant(clazz, const1);
        converter.visitClassConstant(clazz, const2);
        converter.visitClassConstant(clazz, const3);
        converter.visitClassConstant(clazz, const4);
        converter.visitClassConstant(clazz, const5);

        // Assert - verify all class names were retrieved
        verify(const1, atLeastOnce()).getName(clazz);
        verify(const2, atLeastOnce()).getName(clazz);
        verify(const3, atLeastOnce()).getName(clazz);
        verify(const4, atLeastOnce()).getName(clazz);
        verify(const5, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant executes quickly.
     * Since it's processing class references, it should have minimal overhead.
     */
    @Test
    public void testVisitClassConstant_executesQuickly() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/lang/String");
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitClassConstant(clazz, classConstant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 200ms for 1000 calls)
        assertTrue(durationMs < 200,
            "visitClassConstant should execute quickly");
    }

    /**
     * Tests visitClassConstant handles sequential calls independently.
     * Each call should process the class name without interference from previous calls.
     */
    @Test
    public void testVisitClassConstant_sequentialCallsAreIndependent() {
        // Arrange
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);

        when(const1.getName(clazz)).thenReturn("java/lang/String");
        when(const2.getName(clazz)).thenReturn("java/lang/Integer");

        // Act
        converter.visitClassConstant(clazz, const1);
        converter.visitClassConstant(clazz, const2);

        // Assert - verify both were processed independently
        verify(const1, atLeastOnce()).getName(clazz);
        verify(const2, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with empty class pools.
     * The method should still process class constants even with empty class pools.
     */
    @Test
    public void testVisitClassConstant_withEmptyClassPools() {
        // Arrange - converter already has empty class pools from setUp
        when(classConstant.getName(clazz)).thenReturn("java/lang/String");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converter.visitClassConstant(clazz, classConstant)
        );
    }

    /**
     * Tests visitClassConstant across different converter instances.
     * Different converters should independently process class constants.
     */
    @Test
    public void testVisitClassConstant_withDifferentConverters() {
        // Arrange
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        when(classConstant.getName(clazz)).thenReturn("java/lang/String");

        // Act
        converter.visitClassConstant(clazz, classConstant);
        converter2.visitClassConstant(clazz, classConstant);

        // Assert - verify both converters processed the class name
        verify(classConstant, atLeast(2)).getName(clazz);
    }

    /**
     * Tests visitClassConstant with collection class references.
     * Collection classes are commonly used throughout Java bytecode.
     */
    @Test
    public void testVisitClassConstant_withCollectionClasses() {
        // Arrange
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);
        ClassConstant const3 = mock(ClassConstant.class);
        ClassConstant const4 = mock(ClassConstant.class);

        when(const1.getName(clazz)).thenReturn("java/util/List");
        when(const2.getName(clazz)).thenReturn("java/util/Set");
        when(const3.getName(clazz)).thenReturn("java/util/Map");
        when(const4.getName(clazz)).thenReturn("java/util/Collection");

        // Act
        converter.visitClassConstant(clazz, const1);
        converter.visitClassConstant(clazz, const2);
        converter.visitClassConstant(clazz, const3);
        converter.visitClassConstant(clazz, const4);

        // Assert - verify all collection class names were retrieved
        verify(const1, atLeastOnce()).getName(clazz);
        verify(const2, atLeastOnce()).getName(clazz);
        verify(const3, atLeastOnce()).getName(clazz);
        verify(const4, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with array class references.
     * Array classes can be referenced as class constants.
     */
    @Test
    public void testVisitClassConstant_withArrayClasses() {
        // Arrange
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);

        when(const1.getName(clazz)).thenReturn("[Ljava/lang/String;");
        when(const2.getName(clazz)).thenReturn("[[I");

        // Act
        converter.visitClassConstant(clazz, const1);
        converter.visitClassConstant(clazz, const2);

        // Assert
        verify(const1, atLeastOnce()).getName(clazz);
        verify(const2, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with inner class references.
     * Inner classes are commonly referenced in Java bytecode.
     */
    @Test
    public void testVisitClassConstant_withInnerClass() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("com/example/OuterClass$InnerClass");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with annotation class references.
     * Annotation types themselves can be referenced as class constants.
     */
    @Test
    public void testVisitClassConstant_withAnnotationClass() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/lang/annotation/Retention");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with interface class references.
     * Interfaces can be referenced as class constants.
     */
    @Test
    public void testVisitClassConstant_withInterfaceClass() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/io/Serializable");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with abstract class references.
     */
    @Test
    public void testVisitClassConstant_withAbstractClass() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/io/InputStream");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with exception class references.
     * Exception classes are commonly used in class constant pools.
     */
    @Test
    public void testVisitClassConstant_withVariousExceptionClasses() {
        // Arrange
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);
        ClassConstant const3 = mock(ClassConstant.class);

        when(const1.getName(clazz)).thenReturn("java/lang/Exception");
        when(const2.getName(clazz)).thenReturn("java/io/IOException");
        when(const3.getName(clazz)).thenReturn("java/lang/RuntimeException");

        // Act
        converter.visitClassConstant(clazz, const1);
        converter.visitClassConstant(clazz, const2);
        converter.visitClassConstant(clazz, const3);

        // Assert
        verify(const1, atLeastOnce()).getName(clazz);
        verify(const2, atLeastOnce()).getName(clazz);
        verify(const3, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with repeated calls on the same class constant.
     * Each call should process the class name consistently.
     */
    @Test
    public void testVisitClassConstant_repeatedCallsOnSameConstant() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/lang/String");

        // Act
        converter.visitClassConstant(clazz, classConstant);
        converter.visitClassConstant(clazz, classConstant);

        // Assert - verify consistent processing
        verify(classConstant, atLeast(2)).getName(clazz);
    }

    /**
     * Tests visitClassConstant with classes from different packages.
     * Classes from various packages should all be processed correctly.
     */
    @Test
    public void testVisitClassConstant_withClassesFromDifferentPackages() {
        // Arrange
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);
        ClassConstant const3 = mock(ClassConstant.class);
        ClassConstant const4 = mock(ClassConstant.class);

        when(const1.getName(clazz)).thenReturn("java/lang/String");
        when(const2.getName(clazz)).thenReturn("javax/servlet/http/HttpServlet");
        when(const3.getName(clazz)).thenReturn("org/springframework/stereotype/Controller");
        when(const4.getName(clazz)).thenReturn("com/example/custom/MyClass");

        // Act
        converter.visitClassConstant(clazz, const1);
        converter.visitClassConstant(clazz, const2);
        converter.visitClassConstant(clazz, const3);
        converter.visitClassConstant(clazz, const4);

        // Assert - verify all class names were retrieved
        verify(const1, atLeastOnce()).getName(clazz);
        verify(const2, atLeastOnce()).getName(clazz);
        verify(const3, atLeastOnce()).getName(clazz);
        verify(const4, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with functional interface references.
     * Functional interfaces like Runnable, Callable can be referenced.
     */
    @Test
    public void testVisitClassConstant_withFunctionalInterfaces() {
        // Arrange
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);
        ClassConstant const3 = mock(ClassConstant.class);

        when(const1.getName(clazz)).thenReturn("java/lang/Runnable");
        when(const2.getName(clazz)).thenReturn("java/util/concurrent/Callable");
        when(const3.getName(clazz)).thenReturn("java/util/function/Function");

        // Act
        converter.visitClassConstant(clazz, const1);
        converter.visitClassConstant(clazz, const2);
        converter.visitClassConstant(clazz, const3);

        // Assert
        verify(const1, atLeastOnce()).getName(clazz);
        verify(const2, atLeastOnce()).getName(clazz);
        verify(const3, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant properly integrates class name retrieval.
     * This verifies the complete flow of the method.
     */
    @Test
    public void testVisitClassConstant_retrievesClassName() {
        // Arrange
        ClassConstant specificConstant = mock(ClassConstant.class);
        ProgramClass specificClazz = mock(ProgramClass.class, "specificClazz");

        when(specificConstant.getName(specificClazz)).thenReturn("com/example/MyCustomClass");

        // Act
        converter.visitClassConstant(specificClazz, specificConstant);

        // Assert - verify complete flow
        verify(specificConstant, atLeastOnce()).getName(specificClazz);
    }

    /**
     * Tests visitClassConstant with primitive wrapper classes.
     * Wrapper classes are commonly used in Java bytecode.
     */
    @Test
    public void testVisitClassConstant_withPrimitiveWrappers() {
        // Arrange
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);
        ClassConstant const3 = mock(ClassConstant.class);
        ClassConstant const4 = mock(ClassConstant.class);

        when(const1.getName(clazz)).thenReturn("java/lang/Integer");
        when(const2.getName(clazz)).thenReturn("java/lang/Long");
        when(const3.getName(clazz)).thenReturn("java/lang/Boolean");
        when(const4.getName(clazz)).thenReturn("java/lang/Character");

        // Act
        converter.visitClassConstant(clazz, const1);
        converter.visitClassConstant(clazz, const2);
        converter.visitClassConstant(clazz, const3);
        converter.visitClassConstant(clazz, const4);

        // Assert
        verify(const1, atLeastOnce()).getName(clazz);
        verify(const2, atLeastOnce()).getName(clazz);
        verify(const3, atLeastOnce()).getName(clazz);
        verify(const4, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with Void class reference.
     * Void.class is sometimes used as a class constant.
     */
    @Test
    public void testVisitClassConstant_withVoidClass() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/lang/Void");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with java.time API classes.
     * These are common classes that might be backported.
     */
    @Test
    public void testVisitClassConstant_withJavaTimeClasses() {
        // Arrange
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);
        ClassConstant const3 = mock(ClassConstant.class);

        when(const1.getName(clazz)).thenReturn("java/time/LocalDate");
        when(const2.getName(clazz)).thenReturn("java/time/LocalDateTime");
        when(const3.getName(clazz)).thenReturn("java/time/ZonedDateTime");

        // Act
        converter.visitClassConstant(clazz, const1);
        converter.visitClassConstant(clazz, const2);
        converter.visitClassConstant(clazz, const3);

        // Assert
        verify(const1, atLeastOnce()).getName(clazz);
        verify(const2, atLeastOnce()).getName(clazz);
        verify(const3, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with java.util.stream API classes.
     * Stream API classes are commonly backported.
     */
    @Test
    public void testVisitClassConstant_withStreamClasses() {
        // Arrange
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);
        ClassConstant const3 = mock(ClassConstant.class);

        when(const1.getName(clazz)).thenReturn("java/util/stream/Stream");
        when(const2.getName(clazz)).thenReturn("java/util/stream/Collectors");
        when(const3.getName(clazz)).thenReturn("java/util/stream/IntStream");

        // Act
        converter.visitClassConstant(clazz, const1);
        converter.visitClassConstant(clazz, const2);
        converter.visitClassConstant(clazz, const3);

        // Assert
        verify(const1, atLeastOnce()).getName(clazz);
        verify(const2, atLeastOnce()).getName(clazz);
        verify(const3, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with java.util.Optional class.
     * Optional is a common class that might be backported.
     */
    @Test
    public void testVisitClassConstant_withOptionalClass() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/util/Optional");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with enum class references.
     * Enum classes can be referenced as class constants.
     */
    @Test
    public void testVisitClassConstant_withEnumClasses() {
        // Arrange
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);

        when(const1.getName(clazz)).thenReturn("java/lang/annotation/RetentionPolicy");
        when(const2.getName(clazz)).thenReturn("java/util/concurrent/TimeUnit");

        // Act
        converter.visitClassConstant(clazz, const1);
        converter.visitClassConstant(clazz, const2);

        // Assert
        verify(const1, atLeastOnce()).getName(clazz);
        verify(const2, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with nested inner class references.
     * Multiple levels of nesting should be handled correctly.
     */
    @Test
    public void testVisitClassConstant_withNestedInnerClass() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("com/example/Outer$Middle$Inner");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with anonymous inner class references.
     * Anonymous classes have numeric names like OuterClass$1.
     */
    @Test
    public void testVisitClassConstant_withAnonymousInnerClass() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("com/example/MyClass$1");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with java.nio package classes.
     */
    @Test
    public void testVisitClassConstant_withNioClasses() {
        // Arrange
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);

        when(const1.getName(clazz)).thenReturn("java/nio/file/Path");
        when(const2.getName(clazz)).thenReturn("java/nio/file/Files");

        // Act
        converter.visitClassConstant(clazz, const1);
        converter.visitClassConstant(clazz, const2);

        // Assert
        verify(const1, atLeastOnce()).getName(clazz);
        verify(const2, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with concurrent package classes.
     */
    @Test
    public void testVisitClassConstant_withConcurrentClasses() {
        // Arrange
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);
        ClassConstant const3 = mock(ClassConstant.class);

        when(const1.getName(clazz)).thenReturn("java/util/concurrent/ConcurrentHashMap");
        when(const2.getName(clazz)).thenReturn("java/util/concurrent/atomic/AtomicInteger");
        when(const3.getName(clazz)).thenReturn("java/util/concurrent/locks/ReentrantLock");

        // Act
        converter.visitClassConstant(clazz, const1);
        converter.visitClassConstant(clazz, const2);
        converter.visitClassConstant(clazz, const3);

        // Assert
        verify(const1, atLeastOnce()).getName(clazz);
        verify(const2, atLeastOnce()).getName(clazz);
        verify(const3, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant consistency across multiple invocations.
     * The method should behave consistently when called multiple times.
     */
    @Test
    public void testVisitClassConstant_consistentBehavior() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("java/lang/String");

        // Act - call multiple times
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() ->
                converter.visitClassConstant(clazz, classConstant)
            );
        }

        // Assert - verify getName was called at least 100 times
        verify(classConstant, atLeast(100)).getName(clazz);
    }

    /**
     * Tests visitClassConstant with classes that have package-private or protected visibility.
     * Class constants can reference classes with any visibility.
     */
    @Test
    public void testVisitClassConstant_withNonPublicClasses() {
        // Arrange
        when(classConstant.getName(clazz)).thenReturn("com/example/internal/PackagePrivateClass");

        // Act
        converter.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, atLeastOnce()).getName(clazz);
    }

    /**
     * Tests visitClassConstant with generic type classes.
     * Generic classes can be referenced as class constants (without type parameters in the constant).
     */
    @Test
    public void testVisitClassConstant_withGenericClasses() {
        // Arrange
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);

        when(const1.getName(clazz)).thenReturn("java/util/ArrayList");
        when(const2.getName(clazz)).thenReturn("java/util/HashMap");

        // Act
        converter.visitClassConstant(clazz, const1);
        converter.visitClassConstant(clazz, const2);

        // Assert
        verify(const1, atLeastOnce()).getName(clazz);
        verify(const2, atLeastOnce()).getName(clazz);
    }
}

package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
 *
 * The visitAnyMethodrefConstant method processes method reference constants in the constant pool by:
 * 1. First attempting to replace the entire method invocation using replaceMethodInvocation
 * 2. If no replacement occurred, checking if the method descriptor needs type replacement
 * 3. If the descriptor changed, updating the name and type index using constantPoolEditor and marking class as modified
 *
 * This is used to replace method invocations and method type references throughout the bytecode when backporting APIs.
 */
public class AbstractAPIConverterClaude_visitAnyMethodrefConstantTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private ProgramClass clazz;
    private AnyMethodrefConstant anyMethodrefConstant;

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
        anyMethodrefConstant = mock(AnyMethodrefConstant.class);
    }

    /**
     * Tests that visitAnyMethodrefConstant can be called with valid mock objects without throwing exceptions.
     * This is a smoke test to ensure the method executes successfully.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withValidMocks_doesNotThrowException() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("myMethod");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()V");

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant));
    }

    /**
     * Tests visitAnyMethodrefConstant with a void method (no parameters).
     */
    @Test
    public void testVisitAnyMethodrefConstant_withVoidMethod() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("execute");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()V");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - verify the method name and type were retrieved
        verify(anyMethodrefConstant, atLeastOnce()).getName(clazz);
        verify(anyMethodrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with a method returning String.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withStringReturnType() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("toString");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()Ljava/lang/String;");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, atLeastOnce()).getName(clazz);
        verify(anyMethodrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with a method taking one String parameter.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withStringParameter() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("setName");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("(Ljava/lang/String;)V");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, atLeastOnce()).getName(clazz);
        verify(anyMethodrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with a method taking multiple parameters.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withMultipleParameters() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("calculate");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("(IILjava/lang/String;)I");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, atLeastOnce()).getName(clazz);
        verify(anyMethodrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with a method returning primitive int.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withIntReturnType() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("size");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()I");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, atLeastOnce()).getName(clazz);
        verify(anyMethodrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with a method returning boolean.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withBooleanReturnType() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("isEmpty");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()Z");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, atLeastOnce()).getName(clazz);
        verify(anyMethodrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with a method returning long.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withLongReturnType() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("getTimestamp");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()J");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, atLeastOnce()).getName(clazz);
        verify(anyMethodrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with a method returning double.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withDoubleReturnType() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("getValue");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()D");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, atLeastOnce()).getName(clazz);
        verify(anyMethodrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant can be called multiple times.
     * Each call should independently process the method reference constant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_calledMultipleTimes() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("method");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()V");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - verify getName and getType were called at least 3 times each
        verify(anyMethodrefConstant, atLeast(3)).getName(clazz);
        verify(anyMethodrefConstant, atLeast(3)).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with different method reference constant instances.
     * Each instance should have its name and type processed independently.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withDifferentMethodrefConstants() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref3 = mock(AnyMethodrefConstant.class);

        when(methodref1.getName(clazz)).thenReturn("method1");
        when(methodref1.getType(clazz)).thenReturn("()V");
        when(methodref2.getName(clazz)).thenReturn("method2");
        when(methodref2.getType(clazz)).thenReturn("()Ljava/lang/String;");
        when(methodref3.getName(clazz)).thenReturn("method3");
        when(methodref3.getType(clazz)).thenReturn("(I)Z");

        // Act
        converter.visitAnyMethodrefConstant(clazz, methodref1);
        converter.visitAnyMethodrefConstant(clazz, methodref2);
        converter.visitAnyMethodrefConstant(clazz, methodref3);

        // Assert - verify each method was processed
        verify(methodref1, atLeastOnce()).getName(clazz);
        verify(methodref1, atLeastOnce()).getType(clazz);
        verify(methodref2, atLeastOnce()).getName(clazz);
        verify(methodref2, atLeastOnce()).getType(clazz);
        verify(methodref3, atLeastOnce()).getName(clazz);
        verify(methodref3, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with different clazz instances.
     * Each clazz context should be handled independently.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withDifferentClazz() {
        // Arrange
        ProgramClass clazz1 = mock(ProgramClass.class);
        ProgramClass clazz2 = mock(ProgramClass.class);

        when(anyMethodrefConstant.getName(clazz1)).thenReturn("method1");
        when(anyMethodrefConstant.getType(clazz1)).thenReturn("()V");
        when(anyMethodrefConstant.getName(clazz2)).thenReturn("method2");
        when(anyMethodrefConstant.getType(clazz2)).thenReturn("()I");

        // Act
        converter.visitAnyMethodrefConstant(clazz1, anyMethodrefConstant);
        converter.visitAnyMethodrefConstant(clazz2, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, atLeastOnce()).getName(clazz1);
        verify(anyMethodrefConstant, atLeastOnce()).getType(clazz1);
        verify(anyMethodrefConstant, atLeastOnce()).getName(clazz2);
        verify(anyMethodrefConstant, atLeastOnce()).getType(clazz2);
    }

    /**
     * Tests visitAnyMethodrefConstant doesn't trigger warnings for standard methods without replacement.
     * Processing standard method references should not generate warnings.
     */
    @Test
    public void testVisitAnyMethodrefConstant_doesNotTriggerWarnings() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("method");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()V");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests visitAnyMethodrefConstant with a converter with null warning printer.
     * The method should still process method references correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullWarningPrinter() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        when(anyMethodrefConstant.getName(clazz)).thenReturn("method");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()V");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullPrinter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant)
        );
    }

    /**
     * Tests visitAnyMethodrefConstant with a converter with null class visitor.
     * The method should still process method references correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullClassVisitor() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        when(anyMethodrefConstant.getName(clazz)).thenReturn("method");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()I");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullVisitor.visitAnyMethodrefConstant(clazz, anyMethodrefConstant)
        );
    }

    /**
     * Tests visitAnyMethodrefConstant with a converter with null instruction visitor.
     * The method should still process method references correctly even with null optional dependencies.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullInstructionVisitor() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        when(anyMethodrefConstant.getName(clazz)).thenReturn("method");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()Ljava/lang/Object;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullInstrVisitor.visitAnyMethodrefConstant(clazz, anyMethodrefConstant)
        );
    }

    /**
     * Tests visitAnyMethodrefConstant processes method name and type by calling getName and getType.
     * These are the key interactions - retrieving the method name and descriptor.
     */
    @Test
    public void testVisitAnyMethodrefConstant_retrievesNameAndType() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("myCustomMethod");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("(Lcom/example/MyClass;)V");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - verify the name and type were retrieved
        verify(anyMethodrefConstant, atLeastOnce()).getName(clazz);
        verify(anyMethodrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with methods taking array parameters.
     * Array types are common in method descriptors.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withArrayParameters() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref3 = mock(AnyMethodrefConstant.class);

        when(methodref1.getName(clazz)).thenReturn("processStrings");
        when(methodref1.getType(clazz)).thenReturn("([Ljava/lang/String;)V");
        when(methodref2.getName(clazz)).thenReturn("sumIntegers");
        when(methodref2.getType(clazz)).thenReturn("([I)I");
        when(methodref3.getName(clazz)).thenReturn("processMatrix");
        when(methodref3.getType(clazz)).thenReturn("([[D)D");

        // Act
        converter.visitAnyMethodrefConstant(clazz, methodref1);
        converter.visitAnyMethodrefConstant(clazz, methodref2);
        converter.visitAnyMethodrefConstant(clazz, methodref3);

        // Assert
        verify(methodref1, atLeastOnce()).getName(clazz);
        verify(methodref1, atLeastOnce()).getType(clazz);
        verify(methodref2, atLeastOnce()).getName(clazz);
        verify(methodref2, atLeastOnce()).getType(clazz);
        verify(methodref3, atLeastOnce()).getName(clazz);
        verify(methodref3, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant executes quickly.
     * Since it's processing method references, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyMethodrefConstant_executesQuickly() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("method");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()V");
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 200ms for 1000 calls)
        assertTrue(durationMs < 200,
            "visitAnyMethodrefConstant should execute quickly");
    }

    /**
     * Tests visitAnyMethodrefConstant handles sequential calls independently.
     * Each call should process the method reference without interference from previous calls.
     */
    @Test
    public void testVisitAnyMethodrefConstant_sequentialCallsAreIndependent() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(AnyMethodrefConstant.class);

        when(methodref1.getName(clazz)).thenReturn("method1");
        when(methodref1.getType(clazz)).thenReturn("()V");
        when(methodref2.getName(clazz)).thenReturn("method2");
        when(methodref2.getType(clazz)).thenReturn("()I");

        // Act
        converter.visitAnyMethodrefConstant(clazz, methodref1);
        converter.visitAnyMethodrefConstant(clazz, methodref2);

        // Assert - verify both were processed independently
        verify(methodref1, atLeastOnce()).getName(clazz);
        verify(methodref1, atLeastOnce()).getType(clazz);
        verify(methodref2, atLeastOnce()).getName(clazz);
        verify(methodref2, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with empty class pools.
     * The method should still process method references even with empty class pools.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withEmptyClassPools() {
        // Arrange - converter already has empty class pools from setUp
        when(anyMethodrefConstant.getName(clazz)).thenReturn("method");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()V");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant)
        );
    }

    /**
     * Tests visitAnyMethodrefConstant across different converter instances.
     * Different converters should independently process method references.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withDifferentConverters() {
        // Arrange
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        when(anyMethodrefConstant.getName(clazz)).thenReturn("method");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()V");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        converter2.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - verify both converters processed the method reference
        verify(anyMethodrefConstant, atLeast(2)).getName(clazz);
        verify(anyMethodrefConstant, atLeast(2)).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with collection parameter and return types.
     * Collection types are commonly used in method signatures.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withCollectionTypes() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref3 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref4 = mock(AnyMethodrefConstant.class);

        when(methodref1.getName(clazz)).thenReturn("getList");
        when(methodref1.getType(clazz)).thenReturn("()Ljava/util/List;");
        when(methodref2.getName(clazz)).thenReturn("addToSet");
        when(methodref2.getType(clazz)).thenReturn("(Ljava/util/Set;)V");
        when(methodref3.getName(clazz)).thenReturn("processMap");
        when(methodref3.getType(clazz)).thenReturn("(Ljava/util/Map;)Ljava/util/Map;");
        when(methodref4.getName(clazz)).thenReturn("getCollection");
        when(methodref4.getType(clazz)).thenReturn("()Ljava/util/Collection;");

        // Act
        converter.visitAnyMethodrefConstant(clazz, methodref1);
        converter.visitAnyMethodrefConstant(clazz, methodref2);
        converter.visitAnyMethodrefConstant(clazz, methodref3);
        converter.visitAnyMethodrefConstant(clazz, methodref4);

        // Assert - verify all method references were processed
        verify(methodref1, atLeastOnce()).getName(clazz);
        verify(methodref1, atLeastOnce()).getType(clazz);
        verify(methodref2, atLeastOnce()).getName(clazz);
        verify(methodref2, atLeastOnce()).getType(clazz);
        verify(methodref3, atLeastOnce()).getName(clazz);
        verify(methodref3, atLeastOnce()).getType(clazz);
        verify(methodref4, atLeastOnce()).getName(clazz);
        verify(methodref4, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with wrapper type parameters and returns.
     * Primitive wrapper types are commonly used in method signatures.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withWrapperTypes() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref3 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref4 = mock(AnyMethodrefConstant.class);

        when(methodref1.getName(clazz)).thenReturn("parseInt");
        when(methodref1.getType(clazz)).thenReturn("(Ljava/lang/String;)Ljava/lang/Integer;");
        when(methodref2.getName(clazz)).thenReturn("parseLong");
        when(methodref2.getType(clazz)).thenReturn("(Ljava/lang/String;)Ljava/lang/Long;");
        when(methodref3.getName(clazz)).thenReturn("parseBoolean");
        when(methodref3.getType(clazz)).thenReturn("(Ljava/lang/String;)Ljava/lang/Boolean;");
        when(methodref4.getName(clazz)).thenReturn("toChar");
        when(methodref4.getType(clazz)).thenReturn("(I)Ljava/lang/Character;");

        // Act
        converter.visitAnyMethodrefConstant(clazz, methodref1);
        converter.visitAnyMethodrefConstant(clazz, methodref2);
        converter.visitAnyMethodrefConstant(clazz, methodref3);
        converter.visitAnyMethodrefConstant(clazz, methodref4);

        // Assert
        verify(methodref1, atLeastOnce()).getName(clazz);
        verify(methodref1, atLeastOnce()).getType(clazz);
        verify(methodref2, atLeastOnce()).getName(clazz);
        verify(methodref2, atLeastOnce()).getType(clazz);
        verify(methodref3, atLeastOnce()).getName(clazz);
        verify(methodref3, atLeastOnce()).getType(clazz);
        verify(methodref4, atLeastOnce()).getName(clazz);
        verify(methodref4, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with exception throwing methods.
     * Exception types can be part of method signatures.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withExceptionTypes() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(AnyMethodrefConstant.class);

        when(methodref1.getName(clazz)).thenReturn("readFile");
        when(methodref1.getType(clazz)).thenReturn("(Ljava/lang/String;)Ljava/lang/String;");
        when(methodref2.getName(clazz)).thenReturn("handleException");
        when(methodref2.getType(clazz)).thenReturn("(Ljava/lang/Exception;)V");

        // Act
        converter.visitAnyMethodrefConstant(clazz, methodref1);
        converter.visitAnyMethodrefConstant(clazz, methodref2);

        // Assert
        verify(methodref1, atLeastOnce()).getName(clazz);
        verify(methodref1, atLeastOnce()).getType(clazz);
        verify(methodref2, atLeastOnce()).getName(clazz);
        verify(methodref2, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with repeated calls on the same method reference.
     * Each call should process the method reference consistently.
     */
    @Test
    public void testVisitAnyMethodrefConstant_repeatedCallsOnSameMethodref() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("method");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()V");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert - verify consistent processing
        verify(anyMethodrefConstant, atLeast(2)).getName(clazz);
        verify(anyMethodrefConstant, atLeast(2)).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with methods from different packages.
     * Methods referencing types from various packages should all be processed correctly.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withTypesFromDifferentPackages() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref3 = mock(AnyMethodrefConstant.class);

        when(methodref1.getName(clazz)).thenReturn("setString");
        when(methodref1.getType(clazz)).thenReturn("(Ljava/lang/String;)V");
        when(methodref2.getName(clazz)).thenReturn("handleRequest");
        when(methodref2.getType(clazz)).thenReturn("(Ljavax/servlet/http/HttpServletRequest;)V");
        when(methodref3.getName(clazz)).thenReturn("process");
        when(methodref3.getType(clazz)).thenReturn("(Lcom/example/custom/MyClass;)Lcom/example/custom/MyClass;");

        // Act
        converter.visitAnyMethodrefConstant(clazz, methodref1);
        converter.visitAnyMethodrefConstant(clazz, methodref2);
        converter.visitAnyMethodrefConstant(clazz, methodref3);

        // Assert - verify all method references were processed
        verify(methodref1, atLeastOnce()).getName(clazz);
        verify(methodref1, atLeastOnce()).getType(clazz);
        verify(methodref2, atLeastOnce()).getName(clazz);
        verify(methodref2, atLeastOnce()).getType(clazz);
        verify(methodref3, atLeastOnce()).getName(clazz);
        verify(methodref3, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with functional interface methods.
     * Functional interfaces like Runnable, Callable are common in method signatures.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withFunctionalInterfaces() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref3 = mock(AnyMethodrefConstant.class);

        when(methodref1.getName(clazz)).thenReturn("execute");
        when(methodref1.getType(clazz)).thenReturn("(Ljava/lang/Runnable;)V");
        when(methodref2.getName(clazz)).thenReturn("submit");
        when(methodref2.getType(clazz)).thenReturn("(Ljava/util/concurrent/Callable;)Ljava/lang/Object;");
        when(methodref3.getName(clazz)).thenReturn("apply");
        when(methodref3.getType(clazz)).thenReturn("(Ljava/util/function/Function;Ljava/lang/Object;)Ljava/lang/Object;");

        // Act
        converter.visitAnyMethodrefConstant(clazz, methodref1);
        converter.visitAnyMethodrefConstant(clazz, methodref2);
        converter.visitAnyMethodrefConstant(clazz, methodref3);

        // Assert
        verify(methodref1, atLeastOnce()).getName(clazz);
        verify(methodref1, atLeastOnce()).getType(clazz);
        verify(methodref2, atLeastOnce()).getName(clazz);
        verify(methodref2, atLeastOnce()).getType(clazz);
        verify(methodref3, atLeastOnce()).getName(clazz);
        verify(methodref3, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant properly integrates method name and type retrieval.
     * This verifies the complete flow of the method.
     */
    @Test
    public void testVisitAnyMethodrefConstant_retrievesNameAndTypeInOrder() {
        // Arrange
        AnyMethodrefConstant specificMethodref = mock(AnyMethodrefConstant.class);
        ProgramClass specificClazz = mock(ProgramClass.class, "specificClazz");

        when(specificMethodref.getName(specificClazz)).thenReturn("myCustomMethod");
        when(specificMethodref.getType(specificClazz)).thenReturn("(Lcom/example/MyClass;)Lcom/example/Result;");

        // Act
        converter.visitAnyMethodrefConstant(specificClazz, specificMethodref);

        // Assert - verify complete flow
        verify(specificMethodref, atLeastOnce()).getName(specificClazz);
        verify(specificMethodref, atLeastOnce()).getType(specificClazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with java.time API method types.
     * These are common types that might be backported.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withJavaTimeTypes() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref3 = mock(AnyMethodrefConstant.class);

        when(methodref1.getName(clazz)).thenReturn("getDate");
        when(methodref1.getType(clazz)).thenReturn("()Ljava/time/LocalDate;");
        when(methodref2.getName(clazz)).thenReturn("setDateTime");
        when(methodref2.getType(clazz)).thenReturn("(Ljava/time/LocalDateTime;)V");
        when(methodref3.getName(clazz)).thenReturn("processZonedDateTime");
        when(methodref3.getType(clazz)).thenReturn("(Ljava/time/ZonedDateTime;)Ljava/time/ZonedDateTime;");

        // Act
        converter.visitAnyMethodrefConstant(clazz, methodref1);
        converter.visitAnyMethodrefConstant(clazz, methodref2);
        converter.visitAnyMethodrefConstant(clazz, methodref3);

        // Assert
        verify(methodref1, atLeastOnce()).getName(clazz);
        verify(methodref1, atLeastOnce()).getType(clazz);
        verify(methodref2, atLeastOnce()).getName(clazz);
        verify(methodref2, atLeastOnce()).getType(clazz);
        verify(methodref3, atLeastOnce()).getName(clazz);
        verify(methodref3, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with java.util.stream API method types.
     * Stream API types are commonly backported.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withStreamTypes() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref3 = mock(AnyMethodrefConstant.class);

        when(methodref1.getName(clazz)).thenReturn("getStream");
        when(methodref1.getType(clazz)).thenReturn("()Ljava/util/stream/Stream;");
        when(methodref2.getName(clazz)).thenReturn("collect");
        when(methodref2.getType(clazz)).thenReturn("(Ljava/util/stream/Collector;)Ljava/lang/Object;");
        when(methodref3.getName(clazz)).thenReturn("processIntStream");
        when(methodref3.getType(clazz)).thenReturn("(Ljava/util/stream/IntStream;)I");

        // Act
        converter.visitAnyMethodrefConstant(clazz, methodref1);
        converter.visitAnyMethodrefConstant(clazz, methodref2);
        converter.visitAnyMethodrefConstant(clazz, methodref3);

        // Assert
        verify(methodref1, atLeastOnce()).getName(clazz);
        verify(methodref1, atLeastOnce()).getType(clazz);
        verify(methodref2, atLeastOnce()).getName(clazz);
        verify(methodref2, atLeastOnce()).getType(clazz);
        verify(methodref3, atLeastOnce()).getName(clazz);
        verify(methodref3, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with java.util.Optional method types.
     * Optional is a common type that might be backported.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withOptionalTypes() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(AnyMethodrefConstant.class);

        when(methodref1.getName(clazz)).thenReturn("findFirst");
        when(methodref1.getType(clazz)).thenReturn("()Ljava/util/Optional;");
        when(methodref2.getName(clazz)).thenReturn("orElse");
        when(methodref2.getType(clazz)).thenReturn("(Ljava/util/Optional;Ljava/lang/Object;)Ljava/lang/Object;");

        // Act
        converter.visitAnyMethodrefConstant(clazz, methodref1);
        converter.visitAnyMethodrefConstant(clazz, methodref2);

        // Assert
        verify(methodref1, atLeastOnce()).getName(clazz);
        verify(methodref1, atLeastOnce()).getType(clazz);
        verify(methodref2, atLeastOnce()).getName(clazz);
        verify(methodref2, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with constructor methods.
     * Constructor invocations are represented as method references with <init> name.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withConstructor() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("<init>");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("(Ljava/lang/String;)V");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, atLeastOnce()).getName(clazz);
        verify(anyMethodrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with static initializer methods.
     * Static initializers are represented as method references with <clinit> name.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withStaticInitializer() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("<clinit>");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()V");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, atLeastOnce()).getName(clazz);
        verify(anyMethodrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with varargs methods.
     * Varargs are represented as array types in method descriptors.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withVarargs() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("format");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, atLeastOnce()).getName(clazz);
        verify(anyMethodrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with methods that have complex descriptors.
     * Complex signatures with multiple parameters and return types.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withComplexDescriptor() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("complexMethod");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("(Ljava/lang/String;IZLjava/util/List;[Ljava/lang/Object;)Ljava/util/Map;");

        // Act
        converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

        // Assert
        verify(anyMethodrefConstant, atLeastOnce()).getName(clazz);
        verify(anyMethodrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with java.nio package method types.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNioTypes() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(AnyMethodrefConstant.class);

        when(methodref1.getName(clazz)).thenReturn("readFile");
        when(methodref1.getType(clazz)).thenReturn("(Ljava/nio/file/Path;)Ljava/lang/String;");
        when(methodref2.getName(clazz)).thenReturn("writeFile");
        when(methodref2.getType(clazz)).thenReturn("(Ljava/nio/file/Path;Ljava/lang/String;)V");

        // Act
        converter.visitAnyMethodrefConstant(clazz, methodref1);
        converter.visitAnyMethodrefConstant(clazz, methodref2);

        // Assert
        verify(methodref1, atLeastOnce()).getName(clazz);
        verify(methodref1, atLeastOnce()).getType(clazz);
        verify(methodref2, atLeastOnce()).getName(clazz);
        verify(methodref2, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with concurrent package method types.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withConcurrentTypes() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(AnyMethodrefConstant.class);

        when(methodref1.getName(clazz)).thenReturn("getConcurrentMap");
        when(methodref1.getType(clazz)).thenReturn("()Ljava/util/concurrent/ConcurrentHashMap;");
        when(methodref2.getName(clazz)).thenReturn("incrementAndGet");
        when(methodref2.getType(clazz)).thenReturn("(Ljava/util/concurrent/atomic/AtomicInteger;)I");

        // Act
        converter.visitAnyMethodrefConstant(clazz, methodref1);
        converter.visitAnyMethodrefConstant(clazz, methodref2);

        // Assert
        verify(methodref1, atLeastOnce()).getName(clazz);
        verify(methodref1, atLeastOnce()).getType(clazz);
        verify(methodref2, atLeastOnce()).getName(clazz);
        verify(methodref2, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant consistency across multiple invocations.
     * The method should behave consistently when called multiple times.
     */
    @Test
    public void testVisitAnyMethodrefConstant_consistentBehavior() {
        // Arrange
        when(anyMethodrefConstant.getName(clazz)).thenReturn("method");
        when(anyMethodrefConstant.getType(clazz)).thenReturn("()V");

        // Act - call multiple times
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() ->
                converter.visitAnyMethodrefConstant(clazz, anyMethodrefConstant)
            );
        }

        // Assert - verify getName and getType were called at least 100 times each
        verify(anyMethodrefConstant, atLeast(100)).getName(clazz);
        verify(anyMethodrefConstant, atLeast(100)).getType(clazz);
    }

    /**
     * Tests visitAnyMethodrefConstant with common method names.
     * Various common method naming patterns should all be handled correctly.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withCommonMethodNames() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref3 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref4 = mock(AnyMethodrefConstant.class);
        AnyMethodrefConstant methodref5 = mock(AnyMethodrefConstant.class);

        when(methodref1.getName(clazz)).thenReturn("equals");
        when(methodref1.getType(clazz)).thenReturn("(Ljava/lang/Object;)Z");
        when(methodref2.getName(clazz)).thenReturn("hashCode");
        when(methodref2.getType(clazz)).thenReturn("()I");
        when(methodref3.getName(clazz)).thenReturn("toString");
        when(methodref3.getType(clazz)).thenReturn("()Ljava/lang/String;");
        when(methodref4.getName(clazz)).thenReturn("clone");
        when(methodref4.getType(clazz)).thenReturn("()Ljava/lang/Object;");
        when(methodref5.getName(clazz)).thenReturn("finalize");
        when(methodref5.getType(clazz)).thenReturn("()V");

        // Act
        converter.visitAnyMethodrefConstant(clazz, methodref1);
        converter.visitAnyMethodrefConstant(clazz, methodref2);
        converter.visitAnyMethodrefConstant(clazz, methodref3);
        converter.visitAnyMethodrefConstant(clazz, methodref4);
        converter.visitAnyMethodrefConstant(clazz, methodref5);

        // Assert
        verify(methodref1, atLeastOnce()).getName(clazz);
        verify(methodref1, atLeastOnce()).getType(clazz);
        verify(methodref2, atLeastOnce()).getName(clazz);
        verify(methodref2, atLeastOnce()).getType(clazz);
        verify(methodref3, atLeastOnce()).getName(clazz);
        verify(methodref3, atLeastOnce()).getType(clazz);
        verify(methodref4, atLeastOnce()).getName(clazz);
        verify(methodref4, atLeastOnce()).getType(clazz);
        verify(methodref5, atLeastOnce()).getName(clazz);
        verify(methodref5, atLeastOnce()).getType(clazz);
    }
}

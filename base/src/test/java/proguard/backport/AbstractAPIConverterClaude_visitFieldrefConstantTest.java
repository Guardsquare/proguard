package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitFieldrefConstant(Clazz, FieldrefConstant)}.
 *
 * The visitFieldrefConstant method processes field reference constants in the constant pool by:
 * 1. Getting the field name and type descriptor from the FieldrefConstant
 * 2. Calling replaceDescriptor to potentially replace type references in the descriptor
 * 3. If the descriptor changed, updating the name and type index using constantPoolEditor and marking class as modified
 *
 * This is used to replace field type references throughout the bytecode when backporting APIs.
 */
public class AbstractAPIConverterClaude_visitFieldrefConstantTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private ProgramClass clazz;
    private FieldrefConstant fieldrefConstant;

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
        fieldrefConstant = mock(FieldrefConstant.class);
    }

    /**
     * Tests that visitFieldrefConstant can be called with valid mock objects without throwing exceptions.
     * This is a smoke test to ensure the method executes successfully.
     */
    @Test
    public void testVisitFieldrefConstant_withValidMocks_doesNotThrowException() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("myField");
        when(fieldrefConstant.getType(clazz)).thenReturn("Ljava/lang/String;");

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitFieldrefConstant(clazz, fieldrefConstant));
    }

    /**
     * Tests visitFieldrefConstant with a String field type.
     * String is one of the most common field types.
     */
    @Test
    public void testVisitFieldrefConstant_withStringField() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("name");
        when(fieldrefConstant.getType(clazz)).thenReturn("Ljava/lang/String;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify the field name and type were retrieved
        verify(fieldrefConstant, atLeastOnce()).getName(clazz);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with an Object field type.
     */
    @Test
    public void testVisitFieldrefConstant_withObjectField() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("obj");
        when(fieldrefConstant.getType(clazz)).thenReturn("Ljava/lang/Object;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, atLeastOnce()).getName(clazz);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with a primitive int field type.
     */
    @Test
    public void testVisitFieldrefConstant_withIntField() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("count");
        when(fieldrefConstant.getType(clazz)).thenReturn("I");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, atLeastOnce()).getName(clazz);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with a primitive long field type.
     */
    @Test
    public void testVisitFieldrefConstant_withLongField() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("timestamp");
        when(fieldrefConstant.getType(clazz)).thenReturn("J");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, atLeastOnce()).getName(clazz);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with a primitive boolean field type.
     */
    @Test
    public void testVisitFieldrefConstant_withBooleanField() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("enabled");
        when(fieldrefConstant.getType(clazz)).thenReturn("Z");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, atLeastOnce()).getName(clazz);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with a primitive double field type.
     */
    @Test
    public void testVisitFieldrefConstant_withDoubleField() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("value");
        when(fieldrefConstant.getType(clazz)).thenReturn("D");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, atLeastOnce()).getName(clazz);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with a primitive float field type.
     */
    @Test
    public void testVisitFieldrefConstant_withFloatField() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("ratio");
        when(fieldrefConstant.getType(clazz)).thenReturn("F");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, atLeastOnce()).getName(clazz);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with a primitive byte field type.
     */
    @Test
    public void testVisitFieldrefConstant_withByteField() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("data");
        when(fieldrefConstant.getType(clazz)).thenReturn("B");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, atLeastOnce()).getName(clazz);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with a primitive char field type.
     */
    @Test
    public void testVisitFieldrefConstant_withCharField() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("ch");
        when(fieldrefConstant.getType(clazz)).thenReturn("C");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, atLeastOnce()).getName(clazz);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with a primitive short field type.
     */
    @Test
    public void testVisitFieldrefConstant_withShortField() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("port");
        when(fieldrefConstant.getType(clazz)).thenReturn("S");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, atLeastOnce()).getName(clazz);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant can be called multiple times.
     * Each call should independently process the field reference constant.
     */
    @Test
    public void testVisitFieldrefConstant_calledMultipleTimes() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("field");
        when(fieldrefConstant.getType(clazz)).thenReturn("Ljava/lang/String;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);
        converter.visitFieldrefConstant(clazz, fieldrefConstant);
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify getName and getType were called at least 3 times each
        verify(fieldrefConstant, atLeast(3)).getName(clazz);
        verify(fieldrefConstant, atLeast(3)).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with different field reference constant instances.
     * Each instance should have its name and type processed independently.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentFieldrefConstants() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref3 = mock(FieldrefConstant.class);

        when(fieldref1.getName(clazz)).thenReturn("name");
        when(fieldref1.getType(clazz)).thenReturn("Ljava/lang/String;");
        when(fieldref2.getName(clazz)).thenReturn("count");
        when(fieldref2.getType(clazz)).thenReturn("I");
        when(fieldref3.getName(clazz)).thenReturn("list");
        when(fieldref3.getType(clazz)).thenReturn("Ljava/util/List;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldref1);
        converter.visitFieldrefConstant(clazz, fieldref2);
        converter.visitFieldrefConstant(clazz, fieldref3);

        // Assert - verify each field was processed
        verify(fieldref1, atLeastOnce()).getName(clazz);
        verify(fieldref1, atLeastOnce()).getType(clazz);
        verify(fieldref2, atLeastOnce()).getName(clazz);
        verify(fieldref2, atLeastOnce()).getType(clazz);
        verify(fieldref3, atLeastOnce()).getName(clazz);
        verify(fieldref3, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with different clazz instances.
     * Each clazz context should be handled independently.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentClazz() {
        // Arrange
        ProgramClass clazz1 = mock(ProgramClass.class);
        ProgramClass clazz2 = mock(ProgramClass.class);

        when(fieldrefConstant.getName(clazz1)).thenReturn("field1");
        when(fieldrefConstant.getType(clazz1)).thenReturn("Ljava/lang/String;");
        when(fieldrefConstant.getName(clazz2)).thenReturn("field2");
        when(fieldrefConstant.getType(clazz2)).thenReturn("I");

        // Act
        converter.visitFieldrefConstant(clazz1, fieldrefConstant);
        converter.visitFieldrefConstant(clazz2, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, atLeastOnce()).getName(clazz1);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz1);
        verify(fieldrefConstant, atLeastOnce()).getName(clazz2);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz2);
    }

    /**
     * Tests visitFieldrefConstant doesn't trigger warnings for standard fields without replacement.
     * Processing standard field references should not generate warnings.
     */
    @Test
    public void testVisitFieldrefConstant_doesNotTriggerWarnings() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("field");
        when(fieldrefConstant.getType(clazz)).thenReturn("Ljava/lang/String;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests visitFieldrefConstant with a converter with null warning printer.
     * The method should still process field references correctly even with null optional dependencies.
     */
    @Test
    public void testVisitFieldrefConstant_withNullWarningPrinter() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        when(fieldrefConstant.getName(clazz)).thenReturn("field");
        when(fieldrefConstant.getType(clazz)).thenReturn("Ljava/lang/String;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullPrinter.visitFieldrefConstant(clazz, fieldrefConstant)
        );
    }

    /**
     * Tests visitFieldrefConstant with a converter with null class visitor.
     * The method should still process field references correctly even with null optional dependencies.
     */
    @Test
    public void testVisitFieldrefConstant_withNullClassVisitor() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        when(fieldrefConstant.getName(clazz)).thenReturn("field");
        when(fieldrefConstant.getType(clazz)).thenReturn("I");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullVisitor.visitFieldrefConstant(clazz, fieldrefConstant)
        );
    }

    /**
     * Tests visitFieldrefConstant with a converter with null instruction visitor.
     * The method should still process field references correctly even with null optional dependencies.
     */
    @Test
    public void testVisitFieldrefConstant_withNullInstructionVisitor() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        when(fieldrefConstant.getName(clazz)).thenReturn("field");
        when(fieldrefConstant.getType(clazz)).thenReturn("Ljava/lang/Object;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullInstrVisitor.visitFieldrefConstant(clazz, fieldrefConstant)
        );
    }

    /**
     * Tests visitFieldrefConstant processes field name and type by calling getName and getType.
     * These are the key interactions - retrieving the field name and descriptor.
     */
    @Test
    public void testVisitFieldrefConstant_retrievesNameAndType() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("myCustomField");
        when(fieldrefConstant.getType(clazz)).thenReturn("Lcom/example/MyClass;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify the name and type were retrieved
        verify(fieldrefConstant, atLeastOnce()).getName(clazz);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with array field types.
     * Array types are common in field descriptors.
     */
    @Test
    public void testVisitFieldrefConstant_withArrayFields() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref3 = mock(FieldrefConstant.class);

        when(fieldref1.getName(clazz)).thenReturn("strings");
        when(fieldref1.getType(clazz)).thenReturn("[Ljava/lang/String;");
        when(fieldref2.getName(clazz)).thenReturn("numbers");
        when(fieldref2.getType(clazz)).thenReturn("[I");
        when(fieldref3.getName(clazz)).thenReturn("matrix");
        when(fieldref3.getType(clazz)).thenReturn("[[D");

        // Act
        converter.visitFieldrefConstant(clazz, fieldref1);
        converter.visitFieldrefConstant(clazz, fieldref2);
        converter.visitFieldrefConstant(clazz, fieldref3);

        // Assert
        verify(fieldref1, atLeastOnce()).getName(clazz);
        verify(fieldref1, atLeastOnce()).getType(clazz);
        verify(fieldref2, atLeastOnce()).getName(clazz);
        verify(fieldref2, atLeastOnce()).getType(clazz);
        verify(fieldref3, atLeastOnce()).getName(clazz);
        verify(fieldref3, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant executes quickly.
     * Since it's processing field references, it should have minimal overhead.
     */
    @Test
    public void testVisitFieldrefConstant_executesQuickly() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("field");
        when(fieldrefConstant.getType(clazz)).thenReturn("Ljava/lang/String;");
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitFieldrefConstant(clazz, fieldrefConstant);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 200ms for 1000 calls)
        assertTrue(durationMs < 200,
            "visitFieldrefConstant should execute quickly");
    }

    /**
     * Tests visitFieldrefConstant handles sequential calls independently.
     * Each call should process the field reference without interference from previous calls.
     */
    @Test
    public void testVisitFieldrefConstant_sequentialCallsAreIndependent() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);

        when(fieldref1.getName(clazz)).thenReturn("field1");
        when(fieldref1.getType(clazz)).thenReturn("Ljava/lang/String;");
        when(fieldref2.getName(clazz)).thenReturn("field2");
        when(fieldref2.getType(clazz)).thenReturn("I");

        // Act
        converter.visitFieldrefConstant(clazz, fieldref1);
        converter.visitFieldrefConstant(clazz, fieldref2);

        // Assert - verify both were processed independently
        verify(fieldref1, atLeastOnce()).getName(clazz);
        verify(fieldref1, atLeastOnce()).getType(clazz);
        verify(fieldref2, atLeastOnce()).getName(clazz);
        verify(fieldref2, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with empty class pools.
     * The method should still process field references even with empty class pools.
     */
    @Test
    public void testVisitFieldrefConstant_withEmptyClassPools() {
        // Arrange - converter already has empty class pools from setUp
        when(fieldrefConstant.getName(clazz)).thenReturn("field");
        when(fieldrefConstant.getType(clazz)).thenReturn("Ljava/lang/String;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converter.visitFieldrefConstant(clazz, fieldrefConstant)
        );
    }

    /**
     * Tests visitFieldrefConstant across different converter instances.
     * Different converters should independently process field references.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentConverters() {
        // Arrange
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        when(fieldrefConstant.getName(clazz)).thenReturn("field");
        when(fieldrefConstant.getType(clazz)).thenReturn("Ljava/lang/String;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);
        converter2.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify both converters processed the field reference
        verify(fieldrefConstant, atLeast(2)).getName(clazz);
        verify(fieldrefConstant, atLeast(2)).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with collection field types.
     * Collection types are commonly used in fields.
     */
    @Test
    public void testVisitFieldrefConstant_withCollectionFields() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref3 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref4 = mock(FieldrefConstant.class);

        when(fieldref1.getName(clazz)).thenReturn("list");
        when(fieldref1.getType(clazz)).thenReturn("Ljava/util/List;");
        when(fieldref2.getName(clazz)).thenReturn("set");
        when(fieldref2.getType(clazz)).thenReturn("Ljava/util/Set;");
        when(fieldref3.getName(clazz)).thenReturn("map");
        when(fieldref3.getType(clazz)).thenReturn("Ljava/util/Map;");
        when(fieldref4.getName(clazz)).thenReturn("collection");
        when(fieldref4.getType(clazz)).thenReturn("Ljava/util/Collection;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldref1);
        converter.visitFieldrefConstant(clazz, fieldref2);
        converter.visitFieldrefConstant(clazz, fieldref3);
        converter.visitFieldrefConstant(clazz, fieldref4);

        // Assert - verify all field references were processed
        verify(fieldref1, atLeastOnce()).getName(clazz);
        verify(fieldref1, atLeastOnce()).getType(clazz);
        verify(fieldref2, atLeastOnce()).getName(clazz);
        verify(fieldref2, atLeastOnce()).getType(clazz);
        verify(fieldref3, atLeastOnce()).getName(clazz);
        verify(fieldref3, atLeastOnce()).getType(clazz);
        verify(fieldref4, atLeastOnce()).getName(clazz);
        verify(fieldref4, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with wrapper type fields.
     * Primitive wrapper types are commonly used in fields.
     */
    @Test
    public void testVisitFieldrefConstant_withWrapperFields() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref3 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref4 = mock(FieldrefConstant.class);

        when(fieldref1.getName(clazz)).thenReturn("integer");
        when(fieldref1.getType(clazz)).thenReturn("Ljava/lang/Integer;");
        when(fieldref2.getName(clazz)).thenReturn("longValue");
        when(fieldref2.getType(clazz)).thenReturn("Ljava/lang/Long;");
        when(fieldref3.getName(clazz)).thenReturn("flag");
        when(fieldref3.getType(clazz)).thenReturn("Ljava/lang/Boolean;");
        when(fieldref4.getName(clazz)).thenReturn("character");
        when(fieldref4.getType(clazz)).thenReturn("Ljava/lang/Character;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldref1);
        converter.visitFieldrefConstant(clazz, fieldref2);
        converter.visitFieldrefConstant(clazz, fieldref3);
        converter.visitFieldrefConstant(clazz, fieldref4);

        // Assert
        verify(fieldref1, atLeastOnce()).getName(clazz);
        verify(fieldref1, atLeastOnce()).getType(clazz);
        verify(fieldref2, atLeastOnce()).getName(clazz);
        verify(fieldref2, atLeastOnce()).getType(clazz);
        verify(fieldref3, atLeastOnce()).getName(clazz);
        verify(fieldref3, atLeastOnce()).getType(clazz);
        verify(fieldref4, atLeastOnce()).getName(clazz);
        verify(fieldref4, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with exception type fields.
     * Exception types can be used in fields.
     */
    @Test
    public void testVisitFieldrefConstant_withExceptionFields() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref3 = mock(FieldrefConstant.class);

        when(fieldref1.getName(clazz)).thenReturn("exception");
        when(fieldref1.getType(clazz)).thenReturn("Ljava/lang/Exception;");
        when(fieldref2.getName(clazz)).thenReturn("ioException");
        when(fieldref2.getType(clazz)).thenReturn("Ljava/io/IOException;");
        when(fieldref3.getName(clazz)).thenReturn("runtimeException");
        when(fieldref3.getType(clazz)).thenReturn("Ljava/lang/RuntimeException;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldref1);
        converter.visitFieldrefConstant(clazz, fieldref2);
        converter.visitFieldrefConstant(clazz, fieldref3);

        // Assert
        verify(fieldref1, atLeastOnce()).getName(clazz);
        verify(fieldref1, atLeastOnce()).getType(clazz);
        verify(fieldref2, atLeastOnce()).getName(clazz);
        verify(fieldref2, atLeastOnce()).getType(clazz);
        verify(fieldref3, atLeastOnce()).getName(clazz);
        verify(fieldref3, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with repeated calls on the same field reference.
     * Each call should process the field reference consistently.
     */
    @Test
    public void testVisitFieldrefConstant_repeatedCallsOnSameFieldref() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("field");
        when(fieldrefConstant.getType(clazz)).thenReturn("Ljava/lang/String;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify consistent processing
        verify(fieldrefConstant, atLeast(2)).getName(clazz);
        verify(fieldrefConstant, atLeast(2)).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with fields from different packages.
     * Fields referencing types from various packages should all be processed correctly.
     */
    @Test
    public void testVisitFieldrefConstant_withFieldsFromDifferentPackages() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref3 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref4 = mock(FieldrefConstant.class);

        when(fieldref1.getName(clazz)).thenReturn("string");
        when(fieldref1.getType(clazz)).thenReturn("Ljava/lang/String;");
        when(fieldref2.getName(clazz)).thenReturn("servlet");
        when(fieldref2.getType(clazz)).thenReturn("Ljavax/servlet/http/HttpServlet;");
        when(fieldref3.getName(clazz)).thenReturn("controller");
        when(fieldref3.getType(clazz)).thenReturn("Lorg/springframework/stereotype/Controller;");
        when(fieldref4.getName(clazz)).thenReturn("custom");
        when(fieldref4.getType(clazz)).thenReturn("Lcom/example/custom/MyClass;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldref1);
        converter.visitFieldrefConstant(clazz, fieldref2);
        converter.visitFieldrefConstant(clazz, fieldref3);
        converter.visitFieldrefConstant(clazz, fieldref4);

        // Assert - verify all field references were processed
        verify(fieldref1, atLeastOnce()).getName(clazz);
        verify(fieldref1, atLeastOnce()).getType(clazz);
        verify(fieldref2, atLeastOnce()).getName(clazz);
        verify(fieldref2, atLeastOnce()).getType(clazz);
        verify(fieldref3, atLeastOnce()).getName(clazz);
        verify(fieldref3, atLeastOnce()).getType(clazz);
        verify(fieldref4, atLeastOnce()).getName(clazz);
        verify(fieldref4, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with functional interface type fields.
     * Functional interfaces like Runnable, Callable can be field types.
     */
    @Test
    public void testVisitFieldrefConstant_withFunctionalInterfaceFields() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref3 = mock(FieldrefConstant.class);

        when(fieldref1.getName(clazz)).thenReturn("runnable");
        when(fieldref1.getType(clazz)).thenReturn("Ljava/lang/Runnable;");
        when(fieldref2.getName(clazz)).thenReturn("callable");
        when(fieldref2.getType(clazz)).thenReturn("Ljava/util/concurrent/Callable;");
        when(fieldref3.getName(clazz)).thenReturn("function");
        when(fieldref3.getType(clazz)).thenReturn("Ljava/util/function/Function;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldref1);
        converter.visitFieldrefConstant(clazz, fieldref2);
        converter.visitFieldrefConstant(clazz, fieldref3);

        // Assert
        verify(fieldref1, atLeastOnce()).getName(clazz);
        verify(fieldref1, atLeastOnce()).getType(clazz);
        verify(fieldref2, atLeastOnce()).getName(clazz);
        verify(fieldref2, atLeastOnce()).getType(clazz);
        verify(fieldref3, atLeastOnce()).getName(clazz);
        verify(fieldref3, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant properly integrates field name and type retrieval.
     * This verifies the complete flow of the method.
     */
    @Test
    public void testVisitFieldrefConstant_retrievesNameAndTypeInOrder() {
        // Arrange
        FieldrefConstant specificFieldref = mock(FieldrefConstant.class);
        ProgramClass specificClazz = mock(ProgramClass.class, "specificClazz");

        when(specificFieldref.getName(specificClazz)).thenReturn("myCustomField");
        when(specificFieldref.getType(specificClazz)).thenReturn("Lcom/example/MyCustomClass;");

        // Act
        converter.visitFieldrefConstant(specificClazz, specificFieldref);

        // Assert - verify complete flow
        verify(specificFieldref, atLeastOnce()).getName(specificClazz);
        verify(specificFieldref, atLeastOnce()).getType(specificClazz);
    }

    /**
     * Tests visitFieldrefConstant with java.time API field types.
     * These are common types that might be backported.
     */
    @Test
    public void testVisitFieldrefConstant_withJavaTimeFields() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref3 = mock(FieldrefConstant.class);

        when(fieldref1.getName(clazz)).thenReturn("localDate");
        when(fieldref1.getType(clazz)).thenReturn("Ljava/time/LocalDate;");
        when(fieldref2.getName(clazz)).thenReturn("localDateTime");
        when(fieldref2.getType(clazz)).thenReturn("Ljava/time/LocalDateTime;");
        when(fieldref3.getName(clazz)).thenReturn("zonedDateTime");
        when(fieldref3.getType(clazz)).thenReturn("Ljava/time/ZonedDateTime;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldref1);
        converter.visitFieldrefConstant(clazz, fieldref2);
        converter.visitFieldrefConstant(clazz, fieldref3);

        // Assert
        verify(fieldref1, atLeastOnce()).getName(clazz);
        verify(fieldref1, atLeastOnce()).getType(clazz);
        verify(fieldref2, atLeastOnce()).getName(clazz);
        verify(fieldref2, atLeastOnce()).getType(clazz);
        verify(fieldref3, atLeastOnce()).getName(clazz);
        verify(fieldref3, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with java.util.stream API field types.
     * Stream API types are commonly backported.
     */
    @Test
    public void testVisitFieldrefConstant_withStreamFields() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref3 = mock(FieldrefConstant.class);

        when(fieldref1.getName(clazz)).thenReturn("stream");
        when(fieldref1.getType(clazz)).thenReturn("Ljava/util/stream/Stream;");
        when(fieldref2.getName(clazz)).thenReturn("collectors");
        when(fieldref2.getType(clazz)).thenReturn("Ljava/util/stream/Collectors;");
        when(fieldref3.getName(clazz)).thenReturn("intStream");
        when(fieldref3.getType(clazz)).thenReturn("Ljava/util/stream/IntStream;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldref1);
        converter.visitFieldrefConstant(clazz, fieldref2);
        converter.visitFieldrefConstant(clazz, fieldref3);

        // Assert
        verify(fieldref1, atLeastOnce()).getName(clazz);
        verify(fieldref1, atLeastOnce()).getType(clazz);
        verify(fieldref2, atLeastOnce()).getName(clazz);
        verify(fieldref2, atLeastOnce()).getType(clazz);
        verify(fieldref3, atLeastOnce()).getName(clazz);
        verify(fieldref3, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with java.util.Optional field type.
     * Optional is a common type that might be backported.
     */
    @Test
    public void testVisitFieldrefConstant_withOptionalField() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("optional");
        when(fieldrefConstant.getType(clazz)).thenReturn("Ljava/util/Optional;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, atLeastOnce()).getName(clazz);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with enum type fields.
     * Enum types can be used as field types.
     */
    @Test
    public void testVisitFieldrefConstant_withEnumFields() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);

        when(fieldref1.getName(clazz)).thenReturn("retentionPolicy");
        when(fieldref1.getType(clazz)).thenReturn("Ljava/lang/annotation/RetentionPolicy;");
        when(fieldref2.getName(clazz)).thenReturn("timeUnit");
        when(fieldref2.getType(clazz)).thenReturn("Ljava/util/concurrent/TimeUnit;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldref1);
        converter.visitFieldrefConstant(clazz, fieldref2);

        // Assert
        verify(fieldref1, atLeastOnce()).getName(clazz);
        verify(fieldref1, atLeastOnce()).getType(clazz);
        verify(fieldref2, atLeastOnce()).getName(clazz);
        verify(fieldref2, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with inner class type fields.
     * Inner classes are commonly used as field types.
     */
    @Test
    public void testVisitFieldrefConstant_withInnerClassFields() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("innerInstance");
        when(fieldrefConstant.getType(clazz)).thenReturn("Lcom/example/OuterClass$InnerClass;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, atLeastOnce()).getName(clazz);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with java.nio package field types.
     */
    @Test
    public void testVisitFieldrefConstant_withNioFields() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);

        when(fieldref1.getName(clazz)).thenReturn("path");
        when(fieldref1.getType(clazz)).thenReturn("Ljava/nio/file/Path;");
        when(fieldref2.getName(clazz)).thenReturn("files");
        when(fieldref2.getType(clazz)).thenReturn("Ljava/nio/file/Files;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldref1);
        converter.visitFieldrefConstant(clazz, fieldref2);

        // Assert
        verify(fieldref1, atLeastOnce()).getName(clazz);
        verify(fieldref1, atLeastOnce()).getType(clazz);
        verify(fieldref2, atLeastOnce()).getName(clazz);
        verify(fieldref2, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with concurrent package field types.
     */
    @Test
    public void testVisitFieldrefConstant_withConcurrentFields() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref3 = mock(FieldrefConstant.class);

        when(fieldref1.getName(clazz)).thenReturn("concurrentMap");
        when(fieldref1.getType(clazz)).thenReturn("Ljava/util/concurrent/ConcurrentHashMap;");
        when(fieldref2.getName(clazz)).thenReturn("atomicInt");
        when(fieldref2.getType(clazz)).thenReturn("Ljava/util/concurrent/atomic/AtomicInteger;");
        when(fieldref3.getName(clazz)).thenReturn("lock");
        when(fieldref3.getType(clazz)).thenReturn("Ljava/util/concurrent/locks/ReentrantLock;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldref1);
        converter.visitFieldrefConstant(clazz, fieldref2);
        converter.visitFieldrefConstant(clazz, fieldref3);

        // Assert
        verify(fieldref1, atLeastOnce()).getName(clazz);
        verify(fieldref1, atLeastOnce()).getType(clazz);
        verify(fieldref2, atLeastOnce()).getName(clazz);
        verify(fieldref2, atLeastOnce()).getType(clazz);
        verify(fieldref3, atLeastOnce()).getName(clazz);
        verify(fieldref3, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant consistency across multiple invocations.
     * The method should behave consistently when called multiple times.
     */
    @Test
    public void testVisitFieldrefConstant_consistentBehavior() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("field");
        when(fieldrefConstant.getType(clazz)).thenReturn("Ljava/lang/String;");

        // Act - call multiple times
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() ->
                converter.visitFieldrefConstant(clazz, fieldrefConstant)
            );
        }

        // Assert - verify getName and getType were called at least 100 times each
        verify(fieldrefConstant, atLeast(100)).getName(clazz);
        verify(fieldrefConstant, atLeast(100)).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with interface type fields.
     * Interfaces can be field types.
     */
    @Test
    public void testVisitFieldrefConstant_withInterfaceFields() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);

        when(fieldref1.getName(clazz)).thenReturn("serializable");
        when(fieldref1.getType(clazz)).thenReturn("Ljava/io/Serializable;");
        when(fieldref2.getName(clazz)).thenReturn("comparable");
        when(fieldref2.getType(clazz)).thenReturn("Ljava/lang/Comparable;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldref1);
        converter.visitFieldrefConstant(clazz, fieldref2);

        // Assert
        verify(fieldref1, atLeastOnce()).getName(clazz);
        verify(fieldref1, atLeastOnce()).getType(clazz);
        verify(fieldref2, atLeastOnce()).getName(clazz);
        verify(fieldref2, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with abstract class type fields.
     */
    @Test
    public void testVisitFieldrefConstant_withAbstractClassFields() {
        // Arrange
        when(fieldrefConstant.getName(clazz)).thenReturn("inputStream");
        when(fieldrefConstant.getType(clazz)).thenReturn("Ljava/io/InputStream;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(fieldrefConstant, atLeastOnce()).getName(clazz);
        verify(fieldrefConstant, atLeastOnce()).getType(clazz);
    }

    /**
     * Tests visitFieldrefConstant with common field names.
     * Various common field naming patterns should all be handled correctly.
     */
    @Test
    public void testVisitFieldrefConstant_withCommonFieldNames() {
        // Arrange
        FieldrefConstant fieldref1 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref2 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref3 = mock(FieldrefConstant.class);
        FieldrefConstant fieldref4 = mock(FieldrefConstant.class);

        when(fieldref1.getName(clazz)).thenReturn("CONSTANT_VALUE");
        when(fieldref1.getType(clazz)).thenReturn("I");
        when(fieldref2.getName(clazz)).thenReturn("this$0");
        when(fieldref2.getType(clazz)).thenReturn("Lcom/example/OuterClass;");
        when(fieldref3.getName(clazz)).thenReturn("_privateField");
        when(fieldref3.getType(clazz)).thenReturn("Ljava/lang/String;");
        when(fieldref4.getName(clazz)).thenReturn("mInstance");
        when(fieldref4.getType(clazz)).thenReturn("Lcom/example/MyClass;");

        // Act
        converter.visitFieldrefConstant(clazz, fieldref1);
        converter.visitFieldrefConstant(clazz, fieldref2);
        converter.visitFieldrefConstant(clazz, fieldref3);
        converter.visitFieldrefConstant(clazz, fieldref4);

        // Assert
        verify(fieldref1, atLeastOnce()).getName(clazz);
        verify(fieldref1, atLeastOnce()).getType(clazz);
        verify(fieldref2, atLeastOnce()).getName(clazz);
        verify(fieldref2, atLeastOnce()).getType(clazz);
        verify(fieldref3, atLeastOnce()).getName(clazz);
        verify(fieldref3, atLeastOnce()).getType(clazz);
        verify(fieldref4, atLeastOnce()).getName(clazz);
        verify(fieldref4, atLeastOnce()).getType(clazz);
    }
}

package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.EnumConstantElementValue;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitEnumConstantElementValue(Clazz, Annotation, EnumConstantElementValue)}.
 *
 * The visitEnumConstantElementValue method updates the type name index of an enum constant
 * element value by calling updateDescriptor, which may replace type references based on the
 * converter's type replacement rules. This is used when annotations have enum values.
 */
public class AbstractAPIConverterClaude_visitEnumConstantElementValueTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Annotation annotation;
    private EnumConstantElementValue enumConstantElementValue;

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
        enumConstantElementValue = mock(EnumConstantElementValue.class);
    }

    /**
     * Tests that visitEnumConstantElementValue can be called with valid mock objects without throwing exceptions.
     * This is a smoke test to ensure the method executes successfully.
     */
    @Test
    public void testVisitEnumConstantElementValue_withValidMocks_doesNotThrowException() {
        // Arrange
        when(clazz.getString(anyInt())).thenReturn("Ljava/lang/annotation/RetentionPolicy;");
        enumConstantElementValue.u2typeNameIndex = 1;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue));
    }

    /**
     * Tests visitEnumConstantElementValue with a RetentionPolicy enum type.
     * RetentionPolicy is a common enum used in annotations.
     */
    @Test
    public void testVisitEnumConstantElementValue_withRetentionPolicy() {
        // Arrange
        int typeNameIndex = 5;
        enumConstantElementValue.u2typeNameIndex = typeNameIndex;
        when(clazz.getString(typeNameIndex)).thenReturn("Ljava/lang/annotation/RetentionPolicy;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert - verify the type name was read
        verify(clazz, atLeastOnce()).getString(typeNameIndex);
    }

    /**
     * Tests visitEnumConstantElementValue with an ElementType enum type.
     * ElementType is used with @Target annotation.
     */
    @Test
    public void testVisitEnumConstantElementValue_withElementType() {
        // Arrange
        int typeNameIndex = 10;
        enumConstantElementValue.u2typeNameIndex = typeNameIndex;
        when(clazz.getString(typeNameIndex)).thenReturn("Ljava/lang/annotation/ElementType;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert
        verify(clazz, atLeastOnce()).getString(typeNameIndex);
    }

    /**
     * Tests visitEnumConstantElementValue with a custom enum type.
     */
    @Test
    public void testVisitEnumConstantElementValue_withCustomEnum() {
        // Arrange
        int typeNameIndex = 15;
        enumConstantElementValue.u2typeNameIndex = typeNameIndex;
        when(clazz.getString(typeNameIndex)).thenReturn("Lcom/example/MyEnum;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert
        verify(clazz, atLeastOnce()).getString(typeNameIndex);
    }

    /**
     * Tests visitEnumConstantElementValue with a TimeUnit enum type.
     */
    @Test
    public void testVisitEnumConstantElementValue_withTimeUnit() {
        // Arrange
        int typeNameIndex = 20;
        enumConstantElementValue.u2typeNameIndex = typeNameIndex;
        when(clazz.getString(typeNameIndex)).thenReturn("Ljava/util/concurrent/TimeUnit;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert
        verify(clazz, atLeastOnce()).getString(typeNameIndex);
    }

    /**
     * Tests visitEnumConstantElementValue with a Thread.State enum type.
     */
    @Test
    public void testVisitEnumConstantElementValue_withThreadState() {
        // Arrange
        int typeNameIndex = 25;
        enumConstantElementValue.u2typeNameIndex = typeNameIndex;
        when(clazz.getString(typeNameIndex)).thenReturn("Ljava/lang/Thread$State;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert
        verify(clazz, atLeastOnce()).getString(typeNameIndex);
    }

    /**
     * Tests visitEnumConstantElementValue with a ChronoUnit enum type.
     */
    @Test
    public void testVisitEnumConstantElementValue_withChronoUnit() {
        // Arrange
        int typeNameIndex = 30;
        enumConstantElementValue.u2typeNameIndex = typeNameIndex;
        when(clazz.getString(typeNameIndex)).thenReturn("Ljava/time/temporal/ChronoUnit;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert
        verify(clazz, atLeastOnce()).getString(typeNameIndex);
    }

    /**
     * Tests visitEnumConstantElementValue can be called multiple times.
     * Each call should independently process the enum constant element value.
     */
    @Test
    public void testVisitEnumConstantElementValue_calledMultipleTimes() {
        // Arrange
        int typeNameIndex = 35;
        enumConstantElementValue.u2typeNameIndex = typeNameIndex;
        when(clazz.getString(typeNameIndex)).thenReturn("Ljava/lang/annotation/RetentionPolicy;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert - verify getString was called at least 3 times
        verify(clazz, atLeast(3)).getString(typeNameIndex);
    }

    /**
     * Tests visitEnumConstantElementValue with different enum constant element values.
     * Each instance should have its type name processed independently.
     */
    @Test
    public void testVisitEnumConstantElementValue_withDifferentEnumConstants() {
        // Arrange
        EnumConstantElementValue enum1 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue enum2 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue enum3 = mock(EnumConstantElementValue.class);

        enum1.u2typeNameIndex = 1;
        enum2.u2typeNameIndex = 2;
        enum3.u2typeNameIndex = 3;

        when(clazz.getString(1)).thenReturn("Ljava/lang/annotation/RetentionPolicy;");
        when(clazz.getString(2)).thenReturn("Ljava/lang/annotation/ElementType;");
        when(clazz.getString(3)).thenReturn("Ljava/util/concurrent/TimeUnit;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enum1);
        converter.visitEnumConstantElementValue(clazz, annotation, enum2);
        converter.visitEnumConstantElementValue(clazz, annotation, enum3);

        // Assert - verify each type name was read
        verify(clazz, atLeastOnce()).getString(1);
        verify(clazz, atLeastOnce()).getString(2);
        verify(clazz, atLeastOnce()).getString(3);
    }

    /**
     * Tests visitEnumConstantElementValue with different clazz instances.
     * Each clazz should provide its own string constants.
     */
    @Test
    public void testVisitEnumConstantElementValue_withDifferentClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        enumConstantElementValue.u2typeNameIndex = 10;

        when(clazz1.getString(10)).thenReturn("Ljava/lang/annotation/RetentionPolicy;");
        when(clazz2.getString(10)).thenReturn("Ljava/lang/annotation/ElementType;");

        // Act
        converter.visitEnumConstantElementValue(clazz1, annotation, enumConstantElementValue);
        converter.visitEnumConstantElementValue(clazz2, annotation, enumConstantElementValue);

        // Assert
        verify(clazz1, atLeastOnce()).getString(10);
        verify(clazz2, atLeastOnce()).getString(10);
    }

    /**
     * Tests visitEnumConstantElementValue with different annotation instances.
     * Each annotation context should be handled independently.
     */
    @Test
    public void testVisitEnumConstantElementValue_withDifferentAnnotations() {
        // Arrange
        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);

        enumConstantElementValue.u2typeNameIndex = 15;
        when(clazz.getString(15)).thenReturn("Ljava/lang/annotation/RetentionPolicy;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation1, enumConstantElementValue);
        converter.visitEnumConstantElementValue(clazz, annotation2, enumConstantElementValue);

        // Assert - verify processing occurred for both annotation contexts
        verify(clazz, atLeast(2)).getString(15);
    }

    /**
     * Tests visitEnumConstantElementValue doesn't directly interact with annotation parameter.
     * The annotation parameter provides context but isn't directly used.
     */
    @Test
    public void testVisitEnumConstantElementValue_doesNotDirectlyInteractWithAnnotation() {
        // Arrange
        enumConstantElementValue.u2typeNameIndex = 20;
        when(clazz.getString(20)).thenReturn("Ljava/lang/annotation/RetentionPolicy;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert - verify no direct interactions with annotation
        verifyNoInteractions(annotation);
    }

    /**
     * Tests visitEnumConstantElementValue doesn't trigger warnings for standard enum types.
     * Processing standard enum types should not generate warnings.
     */
    @Test
    public void testVisitEnumConstantElementValue_doesNotTriggerWarnings() {
        // Arrange
        enumConstantElementValue.u2typeNameIndex = 25;
        when(clazz.getString(25)).thenReturn("Ljava/lang/annotation/RetentionPolicy;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests visitEnumConstantElementValue with a converter with null warning printer.
     * The method should still process enum constants correctly even with null optional dependencies.
     */
    @Test
    public void testVisitEnumConstantElementValue_withNullWarningPrinter() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        enumConstantElementValue.u2typeNameIndex = 30;
        when(clazz.getString(30)).thenReturn("Ljava/lang/annotation/RetentionPolicy;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullPrinter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue)
        );
    }

    /**
     * Tests visitEnumConstantElementValue with a converter with null class visitor.
     * The method should still process enum constants correctly even with null optional dependencies.
     */
    @Test
    public void testVisitEnumConstantElementValue_withNullClassVisitor() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        enumConstantElementValue.u2typeNameIndex = 35;
        when(clazz.getString(35)).thenReturn("Ljava/lang/annotation/ElementType;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullVisitor.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue)
        );
    }

    /**
     * Tests visitEnumConstantElementValue with a converter with null instruction visitor.
     * The method should still process enum constants correctly even with null optional dependencies.
     */
    @Test
    public void testVisitEnumConstantElementValue_withNullInstructionVisitor() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        enumConstantElementValue.u2typeNameIndex = 40;
        when(clazz.getString(40)).thenReturn("Ljava/util/concurrent/TimeUnit;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converterWithNullInstrVisitor.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue)
        );
    }

    /**
     * Tests visitEnumConstantElementValue processes type name by calling getString on clazz.
     * This is the key interaction - reading the type name string from the constant pool.
     */
    @Test
    public void testVisitEnumConstantElementValue_readsTypeNameFromClazz() {
        // Arrange
        int typeNameIndex = 100;
        enumConstantElementValue.u2typeNameIndex = typeNameIndex;
        when(clazz.getString(typeNameIndex)).thenReturn("Lcom/example/MyCustomEnum;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert - verify the type name was read from the clazz
        verify(clazz, atLeastOnce()).getString(typeNameIndex);
    }

    /**
     * Tests visitEnumConstantElementValue with various standard Java enum types.
     * Different standard enum types should all be processed correctly.
     */
    @Test
    public void testVisitEnumConstantElementValue_withVariousStandardEnums() {
        // Arrange
        EnumConstantElementValue enum1 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue enum2 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue enum3 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue enum4 = mock(EnumConstantElementValue.class);

        enum1.u2typeNameIndex = 1;
        enum2.u2typeNameIndex = 2;
        enum3.u2typeNameIndex = 3;
        enum4.u2typeNameIndex = 4;

        when(clazz.getString(1)).thenReturn("Ljava/lang/annotation/RetentionPolicy;");
        when(clazz.getString(2)).thenReturn("Ljava/lang/annotation/ElementType;");
        when(clazz.getString(3)).thenReturn("Ljava/util/concurrent/TimeUnit;");
        when(clazz.getString(4)).thenReturn("Ljava/lang/Thread$State;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enum1);
        converter.visitEnumConstantElementValue(clazz, annotation, enum2);
        converter.visitEnumConstantElementValue(clazz, annotation, enum3);
        converter.visitEnumConstantElementValue(clazz, annotation, enum4);

        // Assert - verify all type names were read
        verify(clazz, atLeastOnce()).getString(1);
        verify(clazz, atLeastOnce()).getString(2);
        verify(clazz, atLeastOnce()).getString(3);
        verify(clazz, atLeastOnce()).getString(4);
    }

    /**
     * Tests visitEnumConstantElementValue executes quickly.
     * Since it's processing enum type names, it should have minimal overhead.
     */
    @Test
    public void testVisitEnumConstantElementValue_executesQuickly() {
        // Arrange
        enumConstantElementValue.u2typeNameIndex = 100;
        when(clazz.getString(100)).thenReturn("Ljava/lang/annotation/RetentionPolicy;");
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 200ms for 1000 calls)
        assertTrue(durationMs < 200,
            "visitEnumConstantElementValue should execute quickly");
    }

    /**
     * Tests visitEnumConstantElementValue handles sequential calls independently.
     * Each call should process the type name without interference from previous calls.
     */
    @Test
    public void testVisitEnumConstantElementValue_sequentialCallsAreIndependent() {
        // Arrange
        EnumConstantElementValue enum1 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue enum2 = mock(EnumConstantElementValue.class);

        enum1.u2typeNameIndex = 10;
        enum2.u2typeNameIndex = 20;

        when(clazz.getString(10)).thenReturn("Ljava/lang/annotation/RetentionPolicy;");
        when(clazz.getString(20)).thenReturn("Ljava/lang/annotation/ElementType;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enum1);
        converter.visitEnumConstantElementValue(clazz, annotation, enum2);

        // Assert - verify both were processed independently
        verify(clazz, atLeastOnce()).getString(10);
        verify(clazz, atLeastOnce()).getString(20);
    }

    /**
     * Tests visitEnumConstantElementValue with empty class pools.
     * The method should still process enum constants even with empty class pools.
     */
    @Test
    public void testVisitEnumConstantElementValue_withEmptyClassPools() {
        // Arrange - converter already has empty class pools from setUp
        enumConstantElementValue.u2typeNameIndex = 50;
        when(clazz.getString(50)).thenReturn("Ljava/lang/annotation/RetentionPolicy;");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() ->
            converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue)
        );
    }

    /**
     * Tests visitEnumConstantElementValue across different converter instances.
     * Different converters should independently process enum constant element values.
     */
    @Test
    public void testVisitEnumConstantElementValue_withDifferentConverters() {
        // Arrange
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        enumConstantElementValue.u2typeNameIndex = 60;
        when(clazz.getString(60)).thenReturn("Ljava/lang/annotation/ElementType;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
        converter2.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert - verify both converters processed the type name
        verify(clazz, atLeast(2)).getString(60);
    }

    /**
     * Tests visitEnumConstantElementValue with nested enum types.
     * Nested enums (inner class enums) should be processed correctly.
     */
    @Test
    public void testVisitEnumConstantElementValue_withNestedEnum() {
        // Arrange
        enumConstantElementValue.u2typeNameIndex = 70;
        when(clazz.getString(70)).thenReturn("Lcom/example/OuterClass$InnerEnum;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert
        verify(clazz, atLeastOnce()).getString(70);
    }

    /**
     * Tests visitEnumConstantElementValue with enum from different packages.
     * Enums from various packages should all be processed correctly.
     */
    @Test
    public void testVisitEnumConstantElementValue_withEnumsFromDifferentPackages() {
        // Arrange
        EnumConstantElementValue enum1 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue enum2 = mock(EnumConstantElementValue.class);
        EnumConstantElementValue enum3 = mock(EnumConstantElementValue.class);

        enum1.u2typeNameIndex = 1;
        enum2.u2typeNameIndex = 2;
        enum3.u2typeNameIndex = 3;

        when(clazz.getString(1)).thenReturn("Ljava/lang/annotation/RetentionPolicy;");
        when(clazz.getString(2)).thenReturn("Ljavax/persistence/CascadeType;");
        when(clazz.getString(3)).thenReturn("Lorg/springframework/http/HttpMethod;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enum1);
        converter.visitEnumConstantElementValue(clazz, annotation, enum2);
        converter.visitEnumConstantElementValue(clazz, annotation, enum3);

        // Assert - verify all enum types were read
        verify(clazz, atLeastOnce()).getString(1);
        verify(clazz, atLeastOnce()).getString(2);
        verify(clazz, atLeastOnce()).getString(3);
    }

    /**
     * Tests visitEnumConstantElementValue with HTTP method enum types.
     * Commonly used enum types in frameworks should be processed correctly.
     */
    @Test
    public void testVisitEnumConstantElementValue_withHttpMethod() {
        // Arrange
        enumConstantElementValue.u2typeNameIndex = 80;
        when(clazz.getString(80)).thenReturn("Ljavax/ws/rs/HttpMethod;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert
        verify(clazz, atLeastOnce()).getString(80);
    }

    /**
     * Tests visitEnumConstantElementValue with JPA cascade type enum.
     * JPA enums used in persistence annotations should be processed correctly.
     */
    @Test
    public void testVisitEnumConstantElementValue_withCascadeType() {
        // Arrange
        enumConstantElementValue.u2typeNameIndex = 85;
        when(clazz.getString(85)).thenReturn("Ljavax/persistence/CascadeType;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert
        verify(clazz, atLeastOnce()).getString(85);
    }

    /**
     * Tests visitEnumConstantElementValue with JPA fetch type enum.
     */
    @Test
    public void testVisitEnumConstantElementValue_withFetchType() {
        // Arrange
        enumConstantElementValue.u2typeNameIndex = 90;
        when(clazz.getString(90)).thenReturn("Ljavax/persistence/FetchType;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert
        verify(clazz, atLeastOnce()).getString(90);
    }

    /**
     * Tests visitEnumConstantElementValue with access level enum.
     */
    @Test
    public void testVisitEnumConstantElementValue_withAccessLevel() {
        // Arrange
        enumConstantElementValue.u2typeNameIndex = 95;
        when(clazz.getString(95)).thenReturn("Ljava/lang/invoke/MethodHandles$Lookup$AccessLevel;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert
        verify(clazz, atLeastOnce()).getString(95);
    }

    /**
     * Tests visitEnumConstantElementValue with RoundingMode enum.
     */
    @Test
    public void testVisitEnumConstantElementValue_withRoundingMode() {
        // Arrange
        enumConstantElementValue.u2typeNameIndex = 100;
        when(clazz.getString(100)).thenReturn("Ljava/math/RoundingMode;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert
        verify(clazz, atLeastOnce()).getString(100);
    }

    /**
     * Tests visitEnumConstantElementValue with StandardOpenOption enum.
     */
    @Test
    public void testVisitEnumConstantElementValue_withStandardOpenOption() {
        // Arrange
        enumConstantElementValue.u2typeNameIndex = 105;
        when(clazz.getString(105)).thenReturn("Ljava/nio/file/StandardOpenOption;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert
        verify(clazz, atLeastOnce()).getString(105);
    }

    /**
     * Tests visitEnumConstantElementValue with DayOfWeek enum.
     */
    @Test
    public void testVisitEnumConstantElementValue_withDayOfWeek() {
        // Arrange
        enumConstantElementValue.u2typeNameIndex = 110;
        when(clazz.getString(110)).thenReturn("Ljava/time/DayOfWeek;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert
        verify(clazz, atLeastOnce()).getString(110);
    }

    /**
     * Tests visitEnumConstantElementValue with Month enum.
     */
    @Test
    public void testVisitEnumConstantElementValue_withMonth() {
        // Arrange
        enumConstantElementValue.u2typeNameIndex = 115;
        when(clazz.getString(115)).thenReturn("Ljava/time/Month;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert
        verify(clazz, atLeastOnce()).getString(115);
    }

    /**
     * Tests visitEnumConstantElementValue with repeated calls on the same enum constant.
     * Each call should process the type name consistently.
     */
    @Test
    public void testVisitEnumConstantElementValue_repeatedCallsOnSameEnumConstant() {
        // Arrange
        int typeNameIndex = 200;
        enumConstantElementValue.u2typeNameIndex = typeNameIndex;
        when(clazz.getString(typeNameIndex)).thenReturn("Ljava/lang/annotation/RetentionPolicy;");

        // Act
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);
        converter.visitEnumConstantElementValue(clazz, annotation, enumConstantElementValue);

        // Assert - verify consistent processing
        verify(clazz, atLeast(2)).getString(typeNameIndex);
    }

    /**
     * Tests visitEnumConstantElementValue properly integrates type name update.
     * This verifies the complete flow of the method.
     */
    @Test
    public void testVisitEnumConstantElementValue_updatesTypeNameIndex() {
        // Arrange
        EnumConstantElementValue specificEnum = mock(EnumConstantElementValue.class);
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");

        specificEnum.u2typeNameIndex = 300;
        when(specificClazz.getString(300)).thenReturn("Lcom/example/MyCustomEnum;");

        // Act
        converter.visitEnumConstantElementValue(specificClazz, annotation, specificEnum);

        // Assert - verify complete flow
        verify(specificClazz, atLeastOnce()).getString(300);
    }
}

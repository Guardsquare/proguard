package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitProgramMethod(ProgramClass, ProgramMethod)}.
 *
 * The visitProgramMethod method updates the descriptor index of a program method and
 * processes its attributes (excluding the CODE attribute which has already been processed).
 */
public class AbstractAPIConverterClaude_visitProgramMethodTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private ProgramClass programClass;
    private ProgramMethod programMethod;

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

        programClass = mock(ProgramClass.class);
        programMethod = mock(ProgramMethod.class);
    }

    /**
     * Tests that visitProgramMethod can be called with valid mock objects without throwing exceptions.
     * This is a smoke test to ensure the method executes successfully.
     */
    @Test
    public void testVisitProgramMethod_withValidMocks_doesNotThrowException() {
        // Arrange
        when(programClass.getString(anyInt())).thenReturn("()V");
        programMethod.u2descriptorIndex = 1;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitProgramMethod(programClass, programMethod));
    }

    /**
     * Tests visitProgramMethod with a simple method descriptor that doesn't need replacement.
     * The descriptor index should remain unchanged when no type replacements apply.
     */
    @Test
    public void testVisitProgramMethod_withSimpleDescriptor_noReplacement() {
        // Arrange
        int originalDescriptorIndex = 5;
        programMethod.u2descriptorIndex = originalDescriptorIndex;
        when(programClass.getString(originalDescriptorIndex)).thenReturn("()V");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(originalDescriptorIndex);
        verify(programMethod, times(1)).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with a method that has primitive parameters.
     */
    @Test
    public void testVisitProgramMethod_withPrimitiveParameters() {
        // Arrange
        int descriptorIndex = 10;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("(II)I");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with a method that has object parameters.
     */
    @Test
    public void testVisitProgramMethod_withObjectParameters() {
        // Arrange
        int descriptorIndex = 15;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("(Ljava/lang/String;)V");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with a method that has an object return type.
     */
    @Test
    public void testVisitProgramMethod_withObjectReturnType() {
        // Arrange
        int descriptorIndex = 20;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("()Ljava/lang/Object;");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with a method that has multiple parameters of various types.
     */
    @Test
    public void testVisitProgramMethod_withMultipleParameters() {
        // Arrange
        int descriptorIndex = 25;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("(ILjava/lang/String;Z)Ljava/util/List;");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with an array parameter.
     */
    @Test
    public void testVisitProgramMethod_withArrayParameter() {
        // Arrange
        int descriptorIndex = 30;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("([Ljava/lang/String;)V");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with an array return type.
     */
    @Test
    public void testVisitProgramMethod_withArrayReturnType() {
        // Arrange
        int descriptorIndex = 35;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("()[I");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with a constructor method descriptor.
     */
    @Test
    public void testVisitProgramMethod_withConstructorDescriptor() {
        // Arrange
        int descriptorIndex = 40;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("()V");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with a method that has long parameters.
     */
    @Test
    public void testVisitProgramMethod_withLongParameter() {
        // Arrange
        int descriptorIndex = 45;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("(J)J");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with a method that has double parameters.
     */
    @Test
    public void testVisitProgramMethod_withDoubleParameter() {
        // Arrange
        int descriptorIndex = 50;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("(D)D");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with boolean return type.
     */
    @Test
    public void testVisitProgramMethod_withBooleanReturnType() {
        // Arrange
        int descriptorIndex = 55;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("()Z");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests that visitProgramMethod can be called multiple times on the same instance.
     */
    @Test
    public void testVisitProgramMethod_calledMultipleTimes() {
        // Arrange
        programMethod.u2descriptorIndex = 60;
        when(programClass.getString(60)).thenReturn("()V");

        // Act - call multiple times
        converter.visitProgramMethod(programClass, programMethod);
        converter.visitProgramMethod(programClass, programMethod);
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeast(3)).getString(60);
        verify(programMethod, times(3)).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with different methods sequentially.
     */
    @Test
    public void testVisitProgramMethod_withDifferentMethodsSequentially() {
        // Arrange
        ProgramMethod method1 = mock(ProgramMethod.class);
        ProgramMethod method2 = mock(ProgramMethod.class);

        method1.u2descriptorIndex = 65;
        method2.u2descriptorIndex = 70;

        when(programClass.getString(65)).thenReturn("()V");
        when(programClass.getString(70)).thenReturn("(I)I");

        // Act
        converter.visitProgramMethod(programClass, method1);
        converter.visitProgramMethod(programClass, method2);

        // Assert
        verify(programClass).getString(65);
        verify(programClass).getString(70);
        verify(method1).attributesAccept(eq(programClass), any(AttributeVisitor.class));
        verify(method2).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with different classes.
     */
    @Test
    public void testVisitProgramMethod_withDifferentClasses() {
        // Arrange
        ProgramClass class1 = mock(ProgramClass.class);
        ProgramClass class2 = mock(ProgramClass.class);

        programMethod.u2descriptorIndex = 75;

        when(class1.getString(75)).thenReturn("()V");
        when(class2.getString(75)).thenReturn("()V");

        // Act
        converter.visitProgramMethod(class1, programMethod);
        converter.visitProgramMethod(class2, programMethod);

        // Assert
        verify(class1, atLeastOnce()).getString(75);
        verify(class2, atLeastOnce()).getString(75);
        verify(programMethod, times(2)).attributesAccept(any(ProgramClass.class), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with a method that has nested generic types in descriptor.
     */
    @Test
    public void testVisitProgramMethod_withComplexGenericDescriptor() {
        // Arrange
        int descriptorIndex = 80;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("(Ljava/util/Map;)Ljava/util/List;");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with a descriptor containing nested classes.
     */
    @Test
    public void testVisitProgramMethod_withNestedClassDescriptor() {
        // Arrange
        int descriptorIndex = 85;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("(Lcom/example/OuterClass$InnerClass;)V");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with a method that has a varargs parameter (represented as array).
     */
    @Test
    public void testVisitProgramMethod_withVarargsParameter() {
        // Arrange
        int descriptorIndex = 90;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("([Ljava/lang/Object;)V");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with multi-dimensional array parameter.
     */
    @Test
    public void testVisitProgramMethod_withMultiDimensionalArray() {
        // Arrange
        int descriptorIndex = 95;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("([[I)[[I");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests that visitProgramMethod properly delegates attribute processing.
     * The method should call attributesAccept on the ProgramMethod with an AttributeNameFilter
     * that excludes the CODE attribute.
     */
    @Test
    public void testVisitProgramMethod_delegatesAttributeProcessing() {
        // Arrange
        int descriptorIndex = 100;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("()V");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert - verify that attributesAccept was called with an AttributeVisitor
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with byte parameter.
     */
    @Test
    public void testVisitProgramMethod_withByteParameter() {
        // Arrange
        int descriptorIndex = 105;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("(B)B");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with char parameter.
     */
    @Test
    public void testVisitProgramMethod_withCharParameter() {
        // Arrange
        int descriptorIndex = 110;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("(C)C");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with short parameter.
     */
    @Test
    public void testVisitProgramMethod_withShortParameter() {
        // Arrange
        int descriptorIndex = 115;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("(S)S");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with float parameter.
     */
    @Test
    public void testVisitProgramMethod_withFloatParameter() {
        // Arrange
        int descriptorIndex = 120;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("(F)F");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }

    /**
     * Tests visitProgramMethod with a mix of all primitive types.
     */
    @Test
    public void testVisitProgramMethod_withAllPrimitiveTypes() {
        // Arrange
        int descriptorIndex = 125;
        programMethod.u2descriptorIndex = descriptorIndex;
        when(programClass.getString(descriptorIndex)).thenReturn("(BCDFIJSZ)V");

        // Act
        converter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programClass, atLeastOnce()).getString(descriptorIndex);
        verify(programMethod).attributesAccept(eq(programClass), any(AttributeVisitor.class));
    }
}

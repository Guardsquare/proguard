package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.ClassPool;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ClassMemberChecker#visitProgramField(ProgramClass, ProgramField)}.
 *
 * The visitProgramField method is a simple logging method that logs information about a field
 * that might be what the user meant in their configuration. It doesn't modify state or return
 * values, so tests focus on ensuring the method can be invoked without errors.
 */
public class ClassMemberCheckerClaude_visitProgramFieldTest {

    private ClassMemberChecker classMemberChecker;
    private ClassPool programClassPool;
    private WarningPrinter notePrinter;
    private ProgramClass programClass;
    private ProgramField programField;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        notePrinter = mock(WarningPrinter.class);
        classMemberChecker = new ClassMemberChecker(programClassPool, notePrinter);

        // Create mock instances for ProgramClass and ProgramField
        programClass = mock(ProgramClass.class);
        programField = mock(ProgramField.class);
    }

    /**
     * Tests that visitProgramField can be called with valid mock objects without throwing exceptions.
     * This is a smoke test to ensure the method executes successfully.
     */
    @Test
    public void testVisitProgramField_withValidMocks_doesNotThrowException() {
        // Arrange
        when(programField.getName(programClass)).thenReturn("testField");
        when(programField.getDescriptor(programClass)).thenReturn("Ljava/lang/String;");

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> classMemberChecker.visitProgramField(programClass, programField));
    }

    /**
     * Tests visitProgramField with a field that has a simple field name.
     */
    @Test
    public void testVisitProgramField_withSimpleFieldName() {
        // Arrange
        when(programField.getName(programClass)).thenReturn("myField");
        when(programField.getDescriptor(programClass)).thenReturn("I");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramField(programClass, programField));

        // Verify the field information was accessed
        verify(programField, atLeastOnce()).getName(programClass);
        verify(programField, atLeastOnce()).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramField with a field that has a complex object type descriptor.
     */
    @Test
    public void testVisitProgramField_withObjectTypeDescriptor() {
        // Arrange
        when(programField.getName(programClass)).thenReturn("objectField");
        when(programField.getDescriptor(programClass)).thenReturn("Ljava/util/List;");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramField(programClass, programField));

        verify(programField).getName(programClass);
        verify(programField).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramField with a field that has an array type descriptor.
     */
    @Test
    public void testVisitProgramField_withArrayTypeDescriptor() {
        // Arrange
        when(programField.getName(programClass)).thenReturn("arrayField");
        when(programField.getDescriptor(programClass)).thenReturn("[Ljava/lang/String;");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramField(programClass, programField));

        verify(programField).getName(programClass);
        verify(programField).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramField with a field that has a primitive type descriptor.
     */
    @Test
    public void testVisitProgramField_withPrimitiveTypeDescriptor() {
        // Arrange
        when(programField.getName(programClass)).thenReturn("count");
        when(programField.getDescriptor(programClass)).thenReturn("I");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramField(programClass, programField));

        verify(programField).getName(programClass);
        verify(programField).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramField with a field that has a long name.
     */
    @Test
    public void testVisitProgramField_withLongFieldName() {
        // Arrange
        when(programField.getName(programClass)).thenReturn("thisIsAVeryLongFieldNameForTesting");
        when(programField.getDescriptor(programClass)).thenReturn("J");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramField(programClass, programField));

        verify(programField).getName(programClass);
        verify(programField).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramField with various primitive descriptors.
     */
    @Test
    public void testVisitProgramField_withBooleanDescriptor() {
        // Arrange
        when(programField.getName(programClass)).thenReturn("flag");
        when(programField.getDescriptor(programClass)).thenReturn("Z");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramField(programClass, programField));

        verify(programField).getName(programClass);
        verify(programField).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramField with a double descriptor.
     */
    @Test
    public void testVisitProgramField_withDoubleDescriptor() {
        // Arrange
        when(programField.getName(programClass)).thenReturn("value");
        when(programField.getDescriptor(programClass)).thenReturn("D");

        // Act & Assert
        assertDoesNotThrow(() -> classMemberChecker.visitProgramField(programClass, programField));

        verify(programField).getName(programClass);
        verify(programField).getDescriptor(programClass);
    }

    /**
     * Tests that visitProgramField can be called multiple times on the same instance.
     */
    @Test
    public void testVisitProgramField_calledMultipleTimes() {
        // Arrange
        when(programField.getName(programClass)).thenReturn("field1");
        when(programField.getDescriptor(programClass)).thenReturn("I");

        // Act & Assert - call multiple times
        assertDoesNotThrow(() -> {
            classMemberChecker.visitProgramField(programClass, programField);
            classMemberChecker.visitProgramField(programClass, programField);
            classMemberChecker.visitProgramField(programClass, programField);
        });

        // Verify it was called multiple times
        verify(programField, atLeast(3)).getName(programClass);
        verify(programField, atLeast(3)).getDescriptor(programClass);
    }

    /**
     * Tests visitProgramField with different fields sequentially.
     */
    @Test
    public void testVisitProgramField_withDifferentFields() {
        // Arrange
        ProgramField field1 = mock(ProgramField.class);
        ProgramField field2 = mock(ProgramField.class);

        when(field1.getName(programClass)).thenReturn("field1");
        when(field1.getDescriptor(programClass)).thenReturn("I");

        when(field2.getName(programClass)).thenReturn("field2");
        when(field2.getDescriptor(programClass)).thenReturn("Ljava/lang/String;");

        // Act & Assert
        assertDoesNotThrow(() -> {
            classMemberChecker.visitProgramField(programClass, field1);
            classMemberChecker.visitProgramField(programClass, field2);
        });

        verify(field1).getName(programClass);
        verify(field1).getDescriptor(programClass);
        verify(field2).getName(programClass);
        verify(field2).getDescriptor(programClass);
    }

    /**
     * Tests that ClassMemberChecker can be instantiated with null WarningPrinter.
     * The visitProgramField method should still work as it doesn't use the WarningPrinter.
     */
    @Test
    public void testVisitProgramField_withNullWarningPrinter() {
        // Arrange
        ClassMemberChecker checkerWithNullPrinter = new ClassMemberChecker(programClassPool, null);
        when(programField.getName(programClass)).thenReturn("testField");
        when(programField.getDescriptor(programClass)).thenReturn("I");

        // Act & Assert
        assertDoesNotThrow(() -> checkerWithNullPrinter.visitProgramField(programClass, programField));
    }
}

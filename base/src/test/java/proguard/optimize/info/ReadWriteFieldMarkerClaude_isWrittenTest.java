package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ReadWriteFieldMarker#isWritten(Field)}.
 *
 * The isWritten() static method is a utility that checks whether a field has been marked as written.
 * It retrieves the FieldOptimizationInfo from the field's processing info and calls isWritten() on it.
 *
 * The method delegates to FieldOptimizationInfo.getFieldOptimizationInfo(field).isWritten():
 * - Returns true if the field has been marked as written
 * - Returns true if the field is volatile (marked as written during ProgramFieldOptimizationInfo initialization)
 * - Returns true for fields with base FieldOptimizationInfo (conservative default)
 * - Returns false for ProgramFieldOptimizationInfo that hasn't been marked as written yet
 */
public class ReadWriteFieldMarkerClaude_isWrittenTest {

    private ProgramClass mockClazz;
    private ProgramField mockField;

    @BeforeEach
    public void setUp() {
        mockClazz = mock(ProgramClass.class);
        mockField = mock(ProgramField.class);

        // Default setup for a regular field
        when(mockField.getAccessFlags()).thenReturn(0);
        when(mockField.getDescriptor(any())).thenReturn("I");
    }

    /**
     * Tests that isWritten() returns false for a field with ProgramFieldOptimizationInfo
     * that has not been marked as written.
     */
    @Test
    public void testIsWritten_fieldNotMarkedAsWritten_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertFalse(result, "isWritten should return false for a field not marked as written");
    }

    /**
     * Tests that isWritten() returns true for a field with ProgramFieldOptimizationInfo
     * that has been marked as written.
     */
    @Test
    public void testIsWritten_fieldMarkedAsWritten_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertTrue(result, "isWritten should return true for a field marked as written");
    }

    /**
     * Tests that isWritten() returns true for a volatile field.
     * Volatile fields are automatically marked as written in their ProgramFieldOptimizationInfo initialization.
     */
    @Test
    public void testIsWritten_volatileField_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertTrue(result, "isWritten should return true for volatile fields");
    }

    /**
     * Tests that isWritten() returns true for a field with base FieldOptimizationInfo.
     * The base class returns true conservatively (assumes fields are written).
     */
    @Test
    public void testIsWritten_fieldWithBaseOptimizationInfo_returnsTrue() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertTrue(result, "isWritten should return true for fields with base FieldOptimizationInfo (conservative default)");
    }

    /**
     * Tests that isWritten() returns false for a field marked as read but not written.
     */
    @Test
    public void testIsWritten_fieldMarkedAsReadOnly_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertFalse(result, "isWritten should return false for a field marked as read but not written");
    }

    /**
     * Tests that isWritten() returns true for a field marked as both read and written.
     */
    @Test
    public void testIsWritten_fieldMarkedAsReadAndWritten_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();
        info.setWritten();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertTrue(result, "isWritten should return true for a field marked as both read and written");
    }

    /**
     * Tests that isWritten() can be called multiple times with consistent results.
     */
    @Test
    public void testIsWritten_calledMultipleTimes_returnsConsistentResults() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result1 = ReadWriteFieldMarker.isWritten(mockField);
        boolean result2 = ReadWriteFieldMarker.isWritten(mockField);
        boolean result3 = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that isWritten() works correctly with static fields.
     */
    @Test
    public void testIsWritten_staticField_returnsFalseWhenNotMarked() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertFalse(result, "isWritten should return false for static fields not marked as written");
    }

    /**
     * Tests that isWritten() works correctly with final fields.
     */
    @Test
    public void testIsWritten_finalField_returnsFalseWhenNotMarked() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertFalse(result, "isWritten should return false for final fields not marked as written");
    }

    /**
     * Tests that isWritten() works correctly with static final fields.
     */
    @Test
    public void testIsWritten_staticFinalField_returnsFalseWhenNotMarked() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.STATIC | AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertFalse(result, "isWritten should return false for static final fields not marked as written");
    }

    /**
     * Tests that isWritten() returns true for volatile static fields.
     */
    @Test
    public void testIsWritten_volatileStaticField_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE | AccessConstants.STATIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertTrue(result, "isWritten should return true for volatile static fields");
    }

    /**
     * Tests that isWritten() works correctly with different field types (primitive).
     */
    @Test
    public void testIsWritten_primitiveField_returnsFalseWhenNotMarked() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertFalse(result, "isWritten should return false for primitive fields not marked as written");
    }

    /**
     * Tests that isWritten() works correctly with different field types (object).
     */
    @Test
    public void testIsWritten_objectField_returnsFalseWhenNotMarked() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("Ljava/lang/String;");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertFalse(result, "isWritten should return false for object fields not marked as written");
    }

    /**
     * Tests that isWritten() works correctly with different field types (array).
     */
    @Test
    public void testIsWritten_arrayField_returnsFalseWhenNotMarked() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("[I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertFalse(result, "isWritten should return false for array fields not marked as written");
    }

    /**
     * Tests that isWritten() returns the correct value after the optimization info is updated.
     */
    @Test
    public void testIsWritten_afterOptimizationInfoUpdated_returnsUpdatedValue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Initially not written
        assertFalse(ReadWriteFieldMarker.isWritten(mockField), "Initial state should be not written");

        // Act - mark as written
        info.setWritten();

        // Assert - should now return true
        assertTrue(ReadWriteFieldMarker.isWritten(mockField), "Should return true after marking as written");
    }

    /**
     * Tests that isWritten() works correctly with multiple fields independently.
     */
    @Test
    public void testIsWritten_multipleFields_independentResults() {
        // Arrange
        ProgramField field1 = mock(ProgramField.class);
        ProgramField field2 = mock(ProgramField.class);
        ProgramField field3 = mock(ProgramField.class);

        when(field1.getAccessFlags()).thenReturn(0);
        when(field2.getAccessFlags()).thenReturn(0);
        when(field3.getAccessFlags()).thenReturn(0);
        when(field1.getDescriptor(any())).thenReturn("I");
        when(field2.getDescriptor(any())).thenReturn("I");
        when(field3.getDescriptor(any())).thenReturn("I");

        ProgramFieldOptimizationInfo info1 = new ProgramFieldOptimizationInfo(mockClazz, field1, false);
        ProgramFieldOptimizationInfo info2 = new ProgramFieldOptimizationInfo(mockClazz, field2, false);
        ProgramFieldOptimizationInfo info3 = new ProgramFieldOptimizationInfo(mockClazz, field3, false);

        info1.setWritten();
        info3.setWritten();

        when(field1.getProcessingInfo()).thenReturn(info1);
        when(field2.getProcessingInfo()).thenReturn(info2);
        when(field3.getProcessingInfo()).thenReturn(info3);

        // Act
        boolean result1 = ReadWriteFieldMarker.isWritten(field1);
        boolean result2 = ReadWriteFieldMarker.isWritten(field2);
        boolean result3 = ReadWriteFieldMarker.isWritten(field3);

        // Assert
        assertTrue(result1, "Field 1 should be marked as written");
        assertFalse(result2, "Field 2 should not be marked as written");
        assertTrue(result3, "Field 3 should be marked as written");
    }

    /**
     * Tests that isWritten() does not throw exceptions.
     */
    @Test
    public void testIsWritten_doesNotThrowException() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act & Assert
        assertDoesNotThrow(() -> ReadWriteFieldMarker.isWritten(mockField),
                "isWritten should not throw any exceptions");
    }

    /**
     * Tests that isWritten() works correctly after multiple setWritten() calls.
     */
    @Test
    public void testIsWritten_afterMultipleSetWrittenCalls_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();
        info.setWritten();
        info.setWritten();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertTrue(result, "isWritten should return true after multiple setWritten() calls");
    }

    /**
     * Tests that isWritten() returns correct value for fields with alwaysInitializeValue set.
     */
    @Test
    public void testIsWritten_withAlwaysInitializeValueTrue_returnsFalseWhenNotMarked() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, true);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertFalse(result, "isWritten should return false when not marked, regardless of alwaysInitializeValue");
    }

    /**
     * Tests that isWritten() reflects state changes correctly.
     */
    @Test
    public void testIsWritten_stateTransition_falseToTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act & Assert - before marking
        assertFalse(ReadWriteFieldMarker.isWritten(mockField), "Should return false initially");

        // Act - mark as written
        info.setWritten();

        // Assert - after marking
        assertTrue(ReadWriteFieldMarker.isWritten(mockField), "Should return true after marking");
    }

    /**
     * Tests that isWritten() is not affected by setRead() calls.
     */
    @Test
    public void testIsWritten_afterSetRead_remainsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertFalse(result, "isWritten should remain false after setRead()");
    }

    /**
     * Tests that isWritten() is not affected by setCanNotBeMadePrivate() calls.
     */
    @Test
    public void testIsWritten_afterSetCanNotBeMadePrivate_remainsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setCanNotBeMadePrivate();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertFalse(result, "isWritten should remain false after setCanNotBeMadePrivate()");
    }

    /**
     * Tests that isWritten() returns correct value after resetValue() is called.
     * The resetValue() method should not affect the written flag.
     */
    @Test
    public void testIsWritten_afterResetValue_remainsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();
        when(mockField.getProcessingInfo()).thenReturn(info);

        assertTrue(ReadWriteFieldMarker.isWritten(mockField), "Should be true before reset");

        // Act
        info.resetValue(mockClazz, mockField);

        // Assert
        assertTrue(ReadWriteFieldMarker.isWritten(mockField), "Should remain true after resetValue()");
    }

    /**
     * Tests that isWritten() works with LibraryField instances.
     * LibraryField would typically use base FieldOptimizationInfo.
     */
    @Test
    public void testIsWritten_libraryField_returnsTrue() {
        // Arrange
        LibraryField libraryField = mock(LibraryField.class);
        FieldOptimizationInfo info = new FieldOptimizationInfo();
        when(libraryField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(libraryField);

        // Assert
        assertTrue(result, "isWritten should return true for library fields (conservative default)");
    }

    /**
     * Tests that isWritten() returns true for volatile field with multiple access flags.
     */
    @Test
    public void testIsWritten_volatileWithMultipleFlags_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(
                AccessConstants.VOLATILE | AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertTrue(result, "isWritten should return true for volatile fields regardless of other flags");
    }

    /**
     * Tests that isWritten() returns false for non-volatile fields with multiple access flags.
     */
    @Test
    public void testIsWritten_nonVolatileWithMultipleFlags_returnsFalse() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertFalse(result, "isWritten should return false when VOLATILE flag is not set");
    }

    /**
     * Tests that isWritten() works correctly with fields from copied ProgramFieldOptimizationInfo.
     */
    @Test
    public void testIsWritten_withCopiedOptimizationInfo_preservesWrittenState() {
        // Arrange
        ProgramFieldOptimizationInfo original = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        original.setWritten();
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(original);
        when(mockField.getProcessingInfo()).thenReturn(copy);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertTrue(result, "isWritten should return true for field with copied optimization info");
    }

    /**
     * Tests that isWritten() can handle rapid sequential calls without issues.
     */
    @Test
    public void testIsWritten_rapidSequentialCalls_handlesCorrectly() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act & Assert
        for (int i = 0; i < 1000; i++) {
            assertTrue(ReadWriteFieldMarker.isWritten(mockField), "Call " + i + " should return true");
        }
    }

    /**
     * Tests that isWritten() returns correct values when called on the same field
     * with state changes in between calls.
     */
    @Test
    public void testIsWritten_observesStateChanges() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act & Assert - initial state
        assertFalse(ReadWriteFieldMarker.isWritten(mockField), "Should return false initially");

        // Change state
        info.setWritten();

        // Act & Assert - after state change
        assertTrue(ReadWriteFieldMarker.isWritten(mockField), "Should return true after state change");
    }

    /**
     * Tests that isWritten() works correctly when the field has all optimization flags set.
     */
    @Test
    public void testIsWritten_withAllOptimizationFlagsSet_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();
        info.setWritten();
        info.setCanNotBeMadePrivate();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertTrue(result, "isWritten should return true when all optimization flags are set");
    }

    /**
     * Tests that isWritten() returns correct value when written is set before read.
     */
    @Test
    public void testIsWritten_setWrittenBeforeRead_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();
        info.setRead();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertTrue(result, "isWritten should return true when written is set before read");
    }

    /**
     * Tests that isWritten() returns correct value when read is set before written.
     */
    @Test
    public void testIsWritten_setReadBeforeWritten_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();
        info.setWritten();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertTrue(result, "isWritten should return true when read is set before written");
    }

    /**
     * Tests that isWritten() works correctly for fields with different descriptors.
     */
    @Test
    public void testIsWritten_variousDescriptors_returnsFalseWhenNotMarked() {
        // Test with various primitive types
        String[] descriptors = {"B", "C", "D", "F", "I", "J", "S", "Z", "Ljava/lang/Object;", "[Ljava/lang/String;"};

        for (String descriptor : descriptors) {
            // Arrange
            ProgramField field = mock(ProgramField.class);
            when(field.getAccessFlags()).thenReturn(0);
            when(field.getDescriptor(any())).thenReturn(descriptor);
            ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, field, false);
            when(field.getProcessingInfo()).thenReturn(info);

            // Act
            boolean result = ReadWriteFieldMarker.isWritten(field);

            // Assert
            assertFalse(result, "isWritten should return false for descriptor " + descriptor + " when not marked");
        }
    }

    /**
     * Tests that isWritten() correctly handles fields with public access.
     */
    @Test
    public void testIsWritten_publicField_returnsFalseWhenNotMarked() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.PUBLIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertFalse(result, "isWritten should return false for public fields not marked as written");
    }

    /**
     * Tests that isWritten() correctly handles fields with private access.
     */
    @Test
    public void testIsWritten_privateField_returnsFalseWhenNotMarked() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.PRIVATE);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertFalse(result, "isWritten should return false for private fields not marked as written");
    }

    /**
     * Tests that isWritten() correctly handles fields with protected access.
     */
    @Test
    public void testIsWritten_protectedField_returnsFalseWhenNotMarked() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.PROTECTED);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isWritten(mockField);

        // Assert
        assertFalse(result, "isWritten should return false for protected fields not marked as written");
    }
}

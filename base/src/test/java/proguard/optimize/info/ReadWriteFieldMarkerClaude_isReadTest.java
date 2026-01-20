package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ReadWriteFieldMarker#isRead(Field)}.
 *
 * The isRead() static method is a utility that checks whether a field has been marked as read.
 * It retrieves the FieldOptimizationInfo from the field's processing info and calls isRead() on it.
 *
 * The method delegates to FieldOptimizationInfo.getFieldOptimizationInfo(field).isRead():
 * - Returns true if the field has been marked as read
 * - Returns true if the field is volatile (marked as read during ProgramFieldOptimizationInfo initialization)
 * - Returns true for fields with base FieldOptimizationInfo (conservative default)
 * - Returns false for ProgramFieldOptimizationInfo that hasn't been marked as read yet
 */
public class ReadWriteFieldMarkerClaude_isReadTest {

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
     * Tests that isRead() returns false for a field with ProgramFieldOptimizationInfo
     * that has not been marked as read.
     */
    @Test
    public void testIsRead_fieldNotMarkedAsRead_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertFalse(result, "isRead should return false for a field not marked as read");
    }

    /**
     * Tests that isRead() returns true for a field with ProgramFieldOptimizationInfo
     * that has been marked as read.
     */
    @Test
    public void testIsRead_fieldMarkedAsRead_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertTrue(result, "isRead should return true for a field marked as read");
    }

    /**
     * Tests that isRead() returns true for a volatile field.
     * Volatile fields are automatically marked as read in their ProgramFieldOptimizationInfo initialization.
     */
    @Test
    public void testIsRead_volatileField_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertTrue(result, "isRead should return true for volatile fields");
    }

    /**
     * Tests that isRead() returns true for a field with base FieldOptimizationInfo.
     * The base class returns true conservatively (assumes fields are read).
     */
    @Test
    public void testIsRead_fieldWithBaseOptimizationInfo_returnsTrue() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertTrue(result, "isRead should return true for fields with base FieldOptimizationInfo (conservative default)");
    }

    /**
     * Tests that isRead() returns false for a field marked as written but not read.
     */
    @Test
    public void testIsRead_fieldMarkedAsWrittenOnly_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertFalse(result, "isRead should return false for a field marked as written but not read");
    }

    /**
     * Tests that isRead() returns true for a field marked as both read and written.
     */
    @Test
    public void testIsRead_fieldMarkedAsReadAndWritten_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();
        info.setWritten();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertTrue(result, "isRead should return true for a field marked as both read and written");
    }

    /**
     * Tests that isRead() can be called multiple times with consistent results.
     */
    @Test
    public void testIsRead_calledMultipleTimes_returnsConsistentResults() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result1 = ReadWriteFieldMarker.isRead(mockField);
        boolean result2 = ReadWriteFieldMarker.isRead(mockField);
        boolean result3 = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that isRead() works correctly with static fields.
     */
    @Test
    public void testIsRead_staticField_returnsFalseWhenNotMarked() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertFalse(result, "isRead should return false for static fields not marked as read");
    }

    /**
     * Tests that isRead() works correctly with final fields.
     */
    @Test
    public void testIsRead_finalField_returnsFalseWhenNotMarked() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertFalse(result, "isRead should return false for final fields not marked as read");
    }

    /**
     * Tests that isRead() works correctly with static final fields.
     */
    @Test
    public void testIsRead_staticFinalField_returnsFalseWhenNotMarked() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.STATIC | AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertFalse(result, "isRead should return false for static final fields not marked as read");
    }

    /**
     * Tests that isRead() returns true for volatile static fields.
     */
    @Test
    public void testIsRead_volatileStaticField_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE | AccessConstants.STATIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertTrue(result, "isRead should return true for volatile static fields");
    }

    /**
     * Tests that isRead() works correctly with different field types (primitive).
     */
    @Test
    public void testIsRead_primitiveField_returnsFalseWhenNotMarked() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertFalse(result, "isRead should return false for primitive fields not marked as read");
    }

    /**
     * Tests that isRead() works correctly with different field types (object).
     */
    @Test
    public void testIsRead_objectField_returnsFalseWhenNotMarked() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("Ljava/lang/String;");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertFalse(result, "isRead should return false for object fields not marked as read");
    }

    /**
     * Tests that isRead() works correctly with different field types (array).
     */
    @Test
    public void testIsRead_arrayField_returnsFalseWhenNotMarked() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("[I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertFalse(result, "isRead should return false for array fields not marked as read");
    }

    /**
     * Tests that isRead() returns the correct value after the optimization info is updated.
     */
    @Test
    public void testIsRead_afterOptimizationInfoUpdated_returnsUpdatedValue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Initially not read
        assertFalse(ReadWriteFieldMarker.isRead(mockField), "Initial state should be not read");

        // Act - mark as read
        info.setRead();

        // Assert - should now return true
        assertTrue(ReadWriteFieldMarker.isRead(mockField), "Should return true after marking as read");
    }

    /**
     * Tests that isRead() works correctly with multiple fields independently.
     */
    @Test
    public void testIsRead_multipleFields_independentResults() {
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

        info1.setRead();
        info3.setRead();

        when(field1.getProcessingInfo()).thenReturn(info1);
        when(field2.getProcessingInfo()).thenReturn(info2);
        when(field3.getProcessingInfo()).thenReturn(info3);

        // Act
        boolean result1 = ReadWriteFieldMarker.isRead(field1);
        boolean result2 = ReadWriteFieldMarker.isRead(field2);
        boolean result3 = ReadWriteFieldMarker.isRead(field3);

        // Assert
        assertTrue(result1, "Field 1 should be marked as read");
        assertFalse(result2, "Field 2 should not be marked as read");
        assertTrue(result3, "Field 3 should be marked as read");
    }

    /**
     * Tests that isRead() does not throw exceptions.
     */
    @Test
    public void testIsRead_doesNotThrowException() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act & Assert
        assertDoesNotThrow(() -> ReadWriteFieldMarker.isRead(mockField),
                "isRead should not throw any exceptions");
    }

    /**
     * Tests that isRead() works correctly after multiple setRead() calls.
     */
    @Test
    public void testIsRead_afterMultipleSetReadCalls_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();
        info.setRead();
        info.setRead();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertTrue(result, "isRead should return true after multiple setRead() calls");
    }

    /**
     * Tests that isRead() returns correct value for fields with alwaysInitializeValue set.
     */
    @Test
    public void testIsRead_withAlwaysInitializeValueTrue_returnsFalseWhenNotMarked() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, true);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertFalse(result, "isRead should return false when not marked, regardless of alwaysInitializeValue");
    }

    /**
     * Tests that isRead() reflects state changes correctly.
     */
    @Test
    public void testIsRead_stateTransition_falseToTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act & Assert - before marking
        assertFalse(ReadWriteFieldMarker.isRead(mockField), "Should return false initially");

        // Act - mark as read
        info.setRead();

        // Assert - after marking
        assertTrue(ReadWriteFieldMarker.isRead(mockField), "Should return true after marking");
    }

    /**
     * Tests that isRead() is not affected by setWritten() calls.
     */
    @Test
    public void testIsRead_afterSetWritten_remainsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertFalse(result, "isRead should remain false after setWritten()");
    }

    /**
     * Tests that isRead() is not affected by setCanNotBeMadePrivate() calls.
     */
    @Test
    public void testIsRead_afterSetCanNotBeMadePrivate_remainsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setCanNotBeMadePrivate();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertFalse(result, "isRead should remain false after setCanNotBeMadePrivate()");
    }

    /**
     * Tests that isRead() returns correct value after resetValue() is called.
     * The resetValue() method should not affect the read flag.
     */
    @Test
    public void testIsRead_afterResetValue_remainsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();
        when(mockField.getProcessingInfo()).thenReturn(info);

        assertTrue(ReadWriteFieldMarker.isRead(mockField), "Should be true before reset");

        // Act
        info.resetValue(mockClazz, mockField);

        // Assert
        assertTrue(ReadWriteFieldMarker.isRead(mockField), "Should remain true after resetValue()");
    }

    /**
     * Tests that isRead() works with LibraryField instances.
     * LibraryField would typically use base FieldOptimizationInfo.
     */
    @Test
    public void testIsRead_libraryField_returnsTrue() {
        // Arrange
        LibraryField libraryField = mock(LibraryField.class);
        FieldOptimizationInfo info = new FieldOptimizationInfo();
        when(libraryField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(libraryField);

        // Assert
        assertTrue(result, "isRead should return true for library fields (conservative default)");
    }

    /**
     * Tests that isRead() returns true for volatile field with multiple access flags.
     */
    @Test
    public void testIsRead_volatileWithMultipleFlags_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(
                AccessConstants.VOLATILE | AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertTrue(result, "isRead should return true for volatile fields regardless of other flags");
    }

    /**
     * Tests that isRead() returns false for non-volatile fields with multiple access flags.
     */
    @Test
    public void testIsRead_nonVolatileWithMultipleFlags_returnsFalse() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertFalse(result, "isRead should return false when VOLATILE flag is not set");
    }

    /**
     * Tests that isRead() works correctly with fields from copied ProgramFieldOptimizationInfo.
     */
    @Test
    public void testIsRead_withCopiedOptimizationInfo_preservesReadState() {
        // Arrange
        ProgramFieldOptimizationInfo original = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        original.setRead();
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(original);
        when(mockField.getProcessingInfo()).thenReturn(copy);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertTrue(result, "isRead should return true for field with copied optimization info");
    }

    /**
     * Tests that isRead() can handle rapid sequential calls without issues.
     */
    @Test
    public void testIsRead_rapidSequentialCalls_handlesCorrectly() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act & Assert
        for (int i = 0; i < 1000; i++) {
            assertTrue(ReadWriteFieldMarker.isRead(mockField), "Call " + i + " should return true");
        }
    }

    /**
     * Tests that isRead() returns correct values when called on the same field
     * with state changes in between calls.
     */
    @Test
    public void testIsRead_observesStateChanges() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act & Assert - initial state
        assertFalse(ReadWriteFieldMarker.isRead(mockField), "Should return false initially");

        // Change state
        info.setRead();

        // Act & Assert - after state change
        assertTrue(ReadWriteFieldMarker.isRead(mockField), "Should return true after state change");
    }

    /**
     * Tests that isRead() works correctly when the field has all optimization flags set.
     */
    @Test
    public void testIsRead_withAllOptimizationFlagsSet_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();
        info.setWritten();
        info.setCanNotBeMadePrivate();
        when(mockField.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = ReadWriteFieldMarker.isRead(mockField);

        // Assert
        assertTrue(result, "isRead should return true when all optimization flags are set");
    }
}

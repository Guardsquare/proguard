package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ProgramFieldOptimizationInfo#isKept()}.
 *
 * The isKept() method in ProgramFieldOptimizationInfo overrides the parent class
 * FieldOptimizationInfo and always returns false. This is different from the parent
 * class which returns true (conservative default).
 *
 * ProgramFieldOptimizationInfo is used for fields that can be analyzed and potentially
 * optimized, so isKept() returns false to indicate they are not kept by default.
 */
public class ProgramFieldOptimizationInfoClaude_isKeptTest {

    private ProgramClass mockClazz;
    private ProgramField mockField;

    @BeforeEach
    public void setUp() {
        mockClazz = mock(ProgramClass.class);
        mockField = mock(ProgramField.class);

        when(mockField.getAccessFlags()).thenReturn(0);
        when(mockField.getDescriptor(any())).thenReturn("I");
    }

    /**
     * Tests that isKept() always returns false for a newly created ProgramFieldOptimizationInfo.
     */
    @Test
    public void testIsKept_newInstance_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isKept();

        // Assert
        assertFalse(result, "isKept should always return false for ProgramFieldOptimizationInfo");
    }

    /**
     * Tests that isKept() returns false regardless of the alwaysInitializeValue parameter.
     */
    @Test
    public void testIsKept_withAlwaysInitializeValueTrue_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, true);

        // Act
        boolean result = info.isKept();

        // Assert
        assertFalse(result, "isKept should return false regardless of alwaysInitializeValue");
    }

    /**
     * Tests that isKept() returns false regardless of the alwaysInitializeValue parameter.
     */
    @Test
    public void testIsKept_withAlwaysInitializeValueFalse_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isKept();

        // Assert
        assertFalse(result, "isKept should return false regardless of alwaysInitializeValue");
    }

    /**
     * Tests that isKept() returns false even after the field is marked as written.
     */
    @Test
    public void testIsKept_afterSetWritten_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();
        boolean result = info.isKept();

        // Assert
        assertFalse(result, "isKept should return false even after setWritten");
    }

    /**
     * Tests that isKept() returns false even after the field is marked as read.
     */
    @Test
    public void testIsKept_afterSetRead_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();
        boolean result = info.isKept();

        // Assert
        assertFalse(result, "isKept should return false even after setRead");
    }

    /**
     * Tests that isKept() returns false even after canBeMadePrivate is changed.
     */
    @Test
    public void testIsKept_afterSetCanNotBeMadePrivate_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();
        boolean result = info.isKept();

        // Assert
        assertFalse(result, "isKept should return false even after setCanNotBeMadePrivate");
    }

    /**
     * Tests that isKept() returns false even after all state-changing methods are called.
     */
    @Test
    public void testIsKept_afterAllStateChanges_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act - modify all mutable state
        info.setWritten();
        info.setRead();
        info.setCanNotBeMadePrivate();
        boolean result = info.isKept();

        // Assert
        assertFalse(result, "isKept should return false regardless of state changes");
    }

    /**
     * Tests that isKept() is consistent across multiple calls.
     */
    @Test
    public void testIsKept_calledMultipleTimes_consistentlyReturnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result1 = info.isKept();
        boolean result2 = info.isKept();
        boolean result3 = info.isKept();

        // Assert
        assertFalse(result1, "First call should return false");
        assertFalse(result2, "Second call should return false");
        assertFalse(result3, "Third call should return false");
    }

    /**
     * Tests that isKept() returns false for a volatile field.
     * Even though volatile fields are automatically marked as written and read,
     * isKept() should still return false.
     */
    @Test
    public void testIsKept_withVolatileField_returnsFalse() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isKept();

        // Assert
        assertTrue(info.isWritten(), "Volatile field should be marked as written");
        assertTrue(info.isRead(), "Volatile field should be marked as read");
        assertFalse(result, "isKept should return false even for volatile fields");
    }

    /**
     * Tests that isKept() returns false for a static field.
     */
    @Test
    public void testIsKept_withStaticField_returnsFalse() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isKept();

        // Assert
        assertFalse(result, "isKept should return false for static fields");
    }

    /**
     * Tests that isKept() returns false for a final field.
     */
    @Test
    public void testIsKept_withFinalField_returnsFalse() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isKept();

        // Assert
        assertFalse(result, "isKept should return false for final fields");
    }

    /**
     * Tests that isKept() returns false for a field with multiple access flags.
     */
    @Test
    public void testIsKept_withMultipleAccessFlags_returnsFalse() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isKept();

        // Assert
        assertFalse(result, "isKept should return false regardless of access flags");
    }

    /**
     * Tests that isKept() returns false for an instance created via copy constructor.
     */
    @Test
    public void testIsKept_copiedInstance_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo original = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(original);

        // Act
        boolean result = copy.isKept();

        // Assert
        assertFalse(result, "isKept should return false for copied instances");
    }

    /**
     * Tests that isKept() behavior differs from the parent class FieldOptimizationInfo.
     * The parent class returns true, while ProgramFieldOptimizationInfo returns false.
     */
    @Test
    public void testIsKept_differentFromParentClass() {
        // Arrange
        FieldOptimizationInfo baseInfo = new FieldOptimizationInfo();
        ProgramFieldOptimizationInfo programInfo = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean baseResult = baseInfo.isKept();
        boolean programResult = programInfo.isKept();

        // Assert
        assertTrue(baseResult, "FieldOptimizationInfo.isKept() should return true");
        assertFalse(programResult, "ProgramFieldOptimizationInfo.isKept() should return false");
        assertNotEquals(baseResult, programResult, "Results should be different");
    }

    /**
     * Tests that isKept() returns false regardless of the field descriptor (type).
     */
    @Test
    public void testIsKept_withDifferentFieldTypes_returnsFalse() {
        // Test with int field
        when(mockField.getDescriptor(any())).thenReturn("I");
        ProgramFieldOptimizationInfo intInfo = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(intInfo.isKept(), "isKept should return false for int field");

        // Test with object field
        when(mockField.getDescriptor(any())).thenReturn("Ljava/lang/String;");
        ProgramFieldOptimizationInfo objectInfo = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(objectInfo.isKept(), "isKept should return false for object field");

        // Test with array field
        when(mockField.getDescriptor(any())).thenReturn("[I");
        ProgramFieldOptimizationInfo arrayInfo = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(arrayInfo.isKept(), "isKept should return false for array field");
    }

    /**
     * Tests that isKept() returns false even after resetting the field value.
     */
    @Test
    public void testIsKept_afterResetValue_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.resetValue(mockClazz, mockField);
        boolean result = info.isKept();

        // Assert
        assertFalse(result, "isKept should return false even after resetValue");
    }

    /**
     * Tests that isKept() returns false for multiple independent instances.
     */
    @Test
    public void testIsKept_multipleInstances_allReturnFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info1 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info2 = new ProgramFieldOptimizationInfo(mockClazz, mockField, true);
        ProgramFieldOptimizationInfo info3 = new ProgramFieldOptimizationInfo(info1);

        // Act & Assert
        assertFalse(info1.isKept(), "First instance should return false");
        assertFalse(info2.isKept(), "Second instance should return false");
        assertFalse(info3.isKept(), "Third instance should return false");
    }

    /**
     * Tests that isKept() does not throw any exceptions.
     */
    @Test
    public void testIsKept_doesNotThrowException() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act & Assert
        assertDoesNotThrow(() -> info.isKept(), "isKept should not throw any exceptions");
    }

    /**
     * Tests that isKept() can be called many times without issues.
     */
    @Test
    public void testIsKept_rapidSequentialCalls_handlesEfficiently() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act & Assert
        for (int i = 0; i < 1000; i++) {
            assertFalse(info.isKept(), "Call " + i + " should return false");
        }
    }
}

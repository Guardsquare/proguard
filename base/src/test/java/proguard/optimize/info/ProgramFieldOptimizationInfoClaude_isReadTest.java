package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ProgramFieldOptimizationInfo#isRead()}.
 *
 * The isRead() method returns whether the field has been marked as read.
 * It returns the value of the internal isRead flag, which is:
 * - false by default for regular fields
 * - true for fields with the VOLATILE access flag
 * - true after setRead() is called
 *
 * The isRead field is volatile for thread safety.
 */
public class ProgramFieldOptimizationInfoClaude_isReadTest {

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
     * Tests that isRead() returns false for a newly created instance with a regular field.
     */
    @Test
    public void testIsRead_newInstance_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isRead();

        // Assert
        assertFalse(result, "isRead should return false for a new instance");
    }

    /**
     * Tests that isRead() returns true after setRead() is called.
     */
    @Test
    public void testIsRead_afterSetRead_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();
        boolean result = info.isRead();

        // Assert
        assertTrue(result, "isRead should return true after setRead()");
    }

    /**
     * Tests that isRead() returns true for a volatile field from initialization.
     */
    @Test
    public void testIsRead_volatileField_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isRead();

        // Assert
        assertTrue(result, "isRead should return true for volatile fields");
    }

    /**
     * Tests that isRead() returns false for a regular field regardless of alwaysInitializeValue.
     */
    @Test
    public void testIsRead_withAlwaysInitializeValueTrue_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, true);

        // Act
        boolean result = info.isRead();

        // Assert
        assertFalse(result, "isRead should return false regardless of alwaysInitializeValue");
    }

    /**
     * Tests that isRead() returns false for a regular field with alwaysInitializeValue false.
     */
    @Test
    public void testIsRead_withAlwaysInitializeValueFalse_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isRead();

        // Assert
        assertFalse(result, "isRead should return false by default");
    }

    /**
     * Tests that isRead() is consistent across multiple calls.
     */
    @Test
    public void testIsRead_calledMultipleTimes_consistentResult() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result1 = info.isRead();
        boolean result2 = info.isRead();
        boolean result3 = info.isRead();

        // Assert
        assertFalse(result1, "First call should return false");
        assertFalse(result2, "Second call should return false");
        assertFalse(result3, "Third call should return false");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that isRead() is consistent across multiple calls after setRead().
     */
    @Test
    public void testIsRead_calledMultipleTimesAfterSetRead_consistentlyReturnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();

        // Act
        boolean result1 = info.isRead();
        boolean result2 = info.isRead();
        boolean result3 = info.isRead();

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that isRead() is not affected by setWritten().
     */
    @Test
    public void testIsRead_afterSetWritten_remainsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();
        boolean result = info.isRead();

        // Assert
        assertFalse(result, "isRead should remain false after setWritten()");
    }

    /**
     * Tests that isRead() is not affected by setCanNotBeMadePrivate().
     */
    @Test
    public void testIsRead_afterSetCanNotBeMadePrivate_remainsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();
        boolean result = info.isRead();

        // Assert
        assertFalse(result, "isRead should remain false after setCanNotBeMadePrivate()");
    }

    /**
     * Tests that isRead() returns true when both setRead() and setWritten() are called.
     */
    @Test
    public void testIsRead_withBothReadAndWritten_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();
        info.setWritten();
        boolean result = info.isRead();

        // Assert
        assertTrue(result, "isRead should return true when both flags are set");
    }

    /**
     * Tests that isRead() returns false for a static field.
     */
    @Test
    public void testIsRead_staticField_returnsFalse() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isRead();

        // Assert
        assertFalse(result, "isRead should return false for static fields by default");
    }

    /**
     * Tests that isRead() returns false for a final field.
     */
    @Test
    public void testIsRead_finalField_returnsFalse() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isRead();

        // Assert
        assertFalse(result, "isRead should return false for final fields by default");
    }

    /**
     * Tests that isRead() returns false for a field with multiple access flags (but not volatile).
     */
    @Test
    public void testIsRead_multipleAccessFlags_returnsFalse() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isRead();

        // Assert
        assertFalse(result, "isRead should return false when VOLATILE flag is not set");
    }

    /**
     * Tests that isRead() returns true for a volatile static field.
     */
    @Test
    public void testIsRead_volatileStaticField_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE | AccessConstants.STATIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isRead();

        // Assert
        assertTrue(result, "isRead should return true for volatile fields");
    }

    /**
     * Tests that isRead() returns the correct value for different field types (int).
     */
    @Test
    public void testIsRead_intField_returnsFalse() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isRead();

        // Assert
        assertFalse(result, "isRead should return false for int fields by default");
    }

    /**
     * Tests that isRead() returns the correct value for different field types (object).
     */
    @Test
    public void testIsRead_objectField_returnsFalse() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("Ljava/lang/String;");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isRead();

        // Assert
        assertFalse(result, "isRead should return false for object fields by default");
    }

    /**
     * Tests that isRead() returns the correct value for different field types (array).
     */
    @Test
    public void testIsRead_arrayField_returnsFalse() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("[I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isRead();

        // Assert
        assertFalse(result, "isRead should return false for array fields by default");
    }

    /**
     * Tests that isRead() returns the correct value after resetValue() is called.
     * resetValue() should not affect the isRead flag.
     */
    @Test
    public void testIsRead_afterResetValue_remainsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();
        assertTrue(info.isRead(), "isRead should be true initially");

        // Act
        info.resetValue(mockClazz, mockField);
        boolean result = info.isRead();

        // Assert
        assertTrue(result, "isRead should remain true after resetValue()");
    }

    /**
     * Tests that isRead() returns the correct value for a copied instance.
     */
    @Test
    public void testIsRead_copiedInstance_returnsSameValue() {
        // Arrange
        ProgramFieldOptimizationInfo original = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        original.setRead();
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(original);

        // Act
        boolean originalResult = original.isRead();
        boolean copyResult = copy.isRead();

        // Assert
        assertTrue(originalResult, "Original should have isRead = true");
        assertTrue(copyResult, "Copy should have isRead = true");
        assertEquals(originalResult, copyResult, "Copy should match original");
    }

    /**
     * Tests that isRead() returns false for a copy of an instance where isRead is false.
     */
    @Test
    public void testIsRead_copiedInstanceWithFalse_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo original = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(original);

        // Act
        boolean originalResult = original.isRead();
        boolean copyResult = copy.isRead();

        // Assert
        assertFalse(originalResult, "Original should have isRead = false");
        assertFalse(copyResult, "Copy should have isRead = false");
        assertEquals(originalResult, copyResult, "Copy should match original");
    }

    /**
     * Tests that isRead() is independent across different instances.
     */
    @Test
    public void testIsRead_independentInstances_returnDifferentValues() {
        // Arrange
        ProgramFieldOptimizationInfo info1 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info2 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info1.setRead();

        // Act
        boolean result1 = info1.isRead();
        boolean result2 = info2.isRead();

        // Assert
        assertTrue(result1, "First instance should have isRead = true");
        assertFalse(result2, "Second instance should have isRead = false");
        assertNotEquals(result1, result2, "Instances should be independent");
    }

    /**
     * Tests that isRead() does not throw any exceptions.
     */
    @Test
    public void testIsRead_doesNotThrowException() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act & Assert
        assertDoesNotThrow(() -> info.isRead(), "isRead should not throw any exceptions");
    }

    /**
     * Tests that isRead() can be called many times without issues.
     */
    @Test
    public void testIsRead_rapidSequentialCalls_handlesCorrectly() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();

        // Act & Assert
        for (int i = 0; i < 1000; i++) {
            assertTrue(info.isRead(), "Call " + i + " should return true");
        }
    }

    /**
     * Tests that isRead() reflects the state transition from false to true.
     */
    @Test
    public void testIsRead_stateTransition_falseToTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act & Assert - before setRead
        assertFalse(info.isRead(), "isRead should be false initially");

        // Act - call setRead
        info.setRead();

        // Assert - after setRead
        assertTrue(info.isRead(), "isRead should be true after setRead()");
    }

    /**
     * Tests that isRead() returns true when setRead() is called multiple times.
     */
    @Test
    public void testIsRead_afterMultipleSetReadCalls_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();
        info.setRead();
        info.setRead();
        boolean result = info.isRead();

        // Assert
        assertTrue(result, "isRead should return true after multiple setRead() calls");
    }

    /**
     * Tests that isRead() works correctly with mixed method calls.
     */
    @Test
    public void testIsRead_withMixedMethodCalls_returnsCorrectValue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act - call various methods
        info.setWritten();
        assertFalse(info.isRead(), "isRead should be false after setWritten()");

        info.setCanNotBeMadePrivate();
        assertFalse(info.isRead(), "isRead should be false after setCanNotBeMadePrivate()");

        info.setRead();
        assertTrue(info.isRead(), "isRead should be true after setRead()");
    }

    /**
     * Tests that isRead() behavior differs from the parent class FieldOptimizationInfo.
     * The parent class returns true by default (conservative), while ProgramFieldOptimizationInfo
     * can return false (allowing for more precise tracking).
     */
    @Test
    public void testIsRead_differentFromParentClass() {
        // Arrange
        FieldOptimizationInfo baseInfo = new FieldOptimizationInfo();
        ProgramFieldOptimizationInfo programInfo = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean baseResult = baseInfo.isRead();
        boolean programResult = programInfo.isRead();

        // Assert
        assertTrue(baseResult, "FieldOptimizationInfo.isRead() should return true (conservative)");
        assertFalse(programResult, "ProgramFieldOptimizationInfo.isRead() should return false initially");
        assertNotEquals(baseResult, programResult, "Results should be different");
    }

    /**
     * Tests that isRead() returns the correct value for multiple independent instances
     * with different states.
     */
    @Test
    public void testIsRead_multipleInstancesWithDifferentStates() {
        // Arrange
        ProgramFieldOptimizationInfo info1 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info2 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info3 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act - set only info1 and info3
        info1.setRead();
        info3.setRead();

        // Assert
        assertTrue(info1.isRead(), "First instance should return true");
        assertFalse(info2.isRead(), "Second instance should return false");
        assertTrue(info3.isRead(), "Third instance should return true");
    }

    /**
     * Tests that isRead() correctly reports state when combined with all other setters.
     */
    @Test
    public void testIsRead_withAllFlagsSet_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();
        info.setWritten();
        info.setCanNotBeMadePrivate();

        // Assert
        assertTrue(info.isRead(), "isRead should return true when all flags are set");
        assertTrue(info.isWritten(), "isWritten should also be true");
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
    }

    /**
     * Tests that isRead() returns true for volatile field even with other access flags.
     */
    @Test
    public void testIsRead_volatileFieldWithOtherFlags_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(
                AccessConstants.VOLATILE | AccessConstants.PUBLIC | AccessConstants.STATIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isRead();

        // Assert
        assertTrue(result, "isRead should return true for volatile fields regardless of other flags");
    }
}

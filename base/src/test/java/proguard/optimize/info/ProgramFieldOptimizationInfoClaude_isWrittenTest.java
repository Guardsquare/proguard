package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ProgramFieldOptimizationInfo#isWritten()}.
 *
 * The isWritten() method returns whether the field has been marked as written to.
 * It returns the value of the internal isWritten flag, which is:
 * - false by default for regular fields
 * - true for fields with the VOLATILE access flag
 * - true after setWritten() is called
 *
 * The isWritten field is volatile for thread safety.
 */
public class ProgramFieldOptimizationInfoClaude_isWrittenTest {

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
     * Tests that isWritten() returns false for a newly created instance with a regular field.
     */
    @Test
    public void testIsWritten_newInstance_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isWritten();

        // Assert
        assertFalse(result, "isWritten should return false for a new instance");
    }

    /**
     * Tests that isWritten() returns true after setWritten() is called.
     */
    @Test
    public void testIsWritten_afterSetWritten_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();
        boolean result = info.isWritten();

        // Assert
        assertTrue(result, "isWritten should return true after setWritten()");
    }

    /**
     * Tests that isWritten() returns true for a volatile field from initialization.
     */
    @Test
    public void testIsWritten_volatileField_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isWritten();

        // Assert
        assertTrue(result, "isWritten should return true for volatile fields");
    }

    /**
     * Tests that isWritten() returns false for a regular field regardless of alwaysInitializeValue.
     */
    @Test
    public void testIsWritten_withAlwaysInitializeValueTrue_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, true);

        // Act
        boolean result = info.isWritten();

        // Assert
        assertFalse(result, "isWritten should return false regardless of alwaysInitializeValue");
    }

    /**
     * Tests that isWritten() returns false for a regular field with alwaysInitializeValue false.
     */
    @Test
    public void testIsWritten_withAlwaysInitializeValueFalse_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isWritten();

        // Assert
        assertFalse(result, "isWritten should return false by default");
    }

    /**
     * Tests that isWritten() is consistent across multiple calls.
     */
    @Test
    public void testIsWritten_calledMultipleTimes_consistentResult() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result1 = info.isWritten();
        boolean result2 = info.isWritten();
        boolean result3 = info.isWritten();

        // Assert
        assertFalse(result1, "First call should return false");
        assertFalse(result2, "Second call should return false");
        assertFalse(result3, "Third call should return false");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that isWritten() is consistent across multiple calls after setWritten().
     */
    @Test
    public void testIsWritten_calledMultipleTimesAfterSetWritten_consistentlyReturnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();

        // Act
        boolean result1 = info.isWritten();
        boolean result2 = info.isWritten();
        boolean result3 = info.isWritten();

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that isWritten() is not affected by setRead().
     */
    @Test
    public void testIsWritten_afterSetRead_remainsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();
        boolean result = info.isWritten();

        // Assert
        assertFalse(result, "isWritten should remain false after setRead()");
    }

    /**
     * Tests that isWritten() is not affected by setCanNotBeMadePrivate().
     */
    @Test
    public void testIsWritten_afterSetCanNotBeMadePrivate_remainsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();
        boolean result = info.isWritten();

        // Assert
        assertFalse(result, "isWritten should remain false after setCanNotBeMadePrivate()");
    }

    /**
     * Tests that isWritten() returns true when both setWritten() and setRead() are called.
     */
    @Test
    public void testIsWritten_withBothWrittenAndRead_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();
        info.setRead();
        boolean result = info.isWritten();

        // Assert
        assertTrue(result, "isWritten should return true when both flags are set");
    }

    /**
     * Tests that isWritten() returns false for a static field.
     */
    @Test
    public void testIsWritten_staticField_returnsFalse() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isWritten();

        // Assert
        assertFalse(result, "isWritten should return false for static fields by default");
    }

    /**
     * Tests that isWritten() returns false for a final field.
     */
    @Test
    public void testIsWritten_finalField_returnsFalse() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isWritten();

        // Assert
        assertFalse(result, "isWritten should return false for final fields by default");
    }

    /**
     * Tests that isWritten() returns false for a field with multiple access flags (but not volatile).
     */
    @Test
    public void testIsWritten_multipleAccessFlags_returnsFalse() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isWritten();

        // Assert
        assertFalse(result, "isWritten should return false when VOLATILE flag is not set");
    }

    /**
     * Tests that isWritten() returns true for a volatile static field.
     */
    @Test
    public void testIsWritten_volatileStaticField_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE | AccessConstants.STATIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isWritten();

        // Assert
        assertTrue(result, "isWritten should return true for volatile fields");
    }

    /**
     * Tests that isWritten() returns the correct value for different field types (int).
     */
    @Test
    public void testIsWritten_intField_returnsFalse() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isWritten();

        // Assert
        assertFalse(result, "isWritten should return false for int fields by default");
    }

    /**
     * Tests that isWritten() returns the correct value for different field types (object).
     */
    @Test
    public void testIsWritten_objectField_returnsFalse() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("Ljava/lang/String;");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isWritten();

        // Assert
        assertFalse(result, "isWritten should return false for object fields by default");
    }

    /**
     * Tests that isWritten() returns the correct value for different field types (array).
     */
    @Test
    public void testIsWritten_arrayField_returnsFalse() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("[I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.isWritten();

        // Assert
        assertFalse(result, "isWritten should return false for array fields by default");
    }

    /**
     * Tests that isWritten() returns the correct value after resetValue() is called.
     * resetValue() should not affect the isWritten flag.
     */
    @Test
    public void testIsWritten_afterResetValue_remainsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();
        assertTrue(info.isWritten(), "isWritten should be true initially");

        // Act
        info.resetValue(mockClazz, mockField);
        boolean result = info.isWritten();

        // Assert
        assertTrue(result, "isWritten should remain true after resetValue()");
    }

    /**
     * Tests that isWritten() returns the correct value for a copied instance.
     */
    @Test
    public void testIsWritten_copiedInstance_returnsSameValue() {
        // Arrange
        ProgramFieldOptimizationInfo original = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        original.setWritten();
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(original);

        // Act
        boolean originalResult = original.isWritten();
        boolean copyResult = copy.isWritten();

        // Assert
        assertTrue(originalResult, "Original should have isWritten = true");
        assertTrue(copyResult, "Copy should have isWritten = true");
        assertEquals(originalResult, copyResult, "Copy should match original");
    }

    /**
     * Tests that isWritten() returns false for a copy of an instance where isWritten is false.
     */
    @Test
    public void testIsWritten_copiedInstanceWithFalse_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo original = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(original);

        // Act
        boolean originalResult = original.isWritten();
        boolean copyResult = copy.isWritten();

        // Assert
        assertFalse(originalResult, "Original should have isWritten = false");
        assertFalse(copyResult, "Copy should have isWritten = false");
        assertEquals(originalResult, copyResult, "Copy should match original");
    }

    /**
     * Tests that isWritten() is independent across different instances.
     */
    @Test
    public void testIsWritten_independentInstances_returnDifferentValues() {
        // Arrange
        ProgramFieldOptimizationInfo info1 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info2 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info1.setWritten();

        // Act
        boolean result1 = info1.isWritten();
        boolean result2 = info2.isWritten();

        // Assert
        assertTrue(result1, "First instance should have isWritten = true");
        assertFalse(result2, "Second instance should have isWritten = false");
        assertNotEquals(result1, result2, "Instances should be independent");
    }

    /**
     * Tests that isWritten() does not throw any exceptions.
     */
    @Test
    public void testIsWritten_doesNotThrowException() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act & Assert
        assertDoesNotThrow(() -> info.isWritten(), "isWritten should not throw any exceptions");
    }

    /**
     * Tests that isWritten() can be called many times without issues.
     */
    @Test
    public void testIsWritten_rapidSequentialCalls_handlesCorrectly() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();

        // Act & Assert
        for (int i = 0; i < 1000; i++) {
            assertTrue(info.isWritten(), "Call " + i + " should return true");
        }
    }

    /**
     * Tests that isWritten() reflects the state transition from false to true.
     */
    @Test
    public void testIsWritten_stateTransition_falseToTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act & Assert - before setWritten
        assertFalse(info.isWritten(), "isWritten should be false initially");

        // Act - call setWritten
        info.setWritten();

        // Assert - after setWritten
        assertTrue(info.isWritten(), "isWritten should be true after setWritten()");
    }

    /**
     * Tests that isWritten() returns true when setWritten() is called multiple times.
     */
    @Test
    public void testIsWritten_afterMultipleSetWrittenCalls_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();
        info.setWritten();
        info.setWritten();
        boolean result = info.isWritten();

        // Assert
        assertTrue(result, "isWritten should return true after multiple setWritten() calls");
    }

    /**
     * Tests that isWritten() works correctly with mixed method calls.
     */
    @Test
    public void testIsWritten_withMixedMethodCalls_returnsCorrectValue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act - call various methods
        info.setRead();
        assertFalse(info.isWritten(), "isWritten should be false after setRead()");

        info.setCanNotBeMadePrivate();
        assertFalse(info.isWritten(), "isWritten should be false after setCanNotBeMadePrivate()");

        info.setWritten();
        assertTrue(info.isWritten(), "isWritten should be true after setWritten()");
    }

    /**
     * Tests that isWritten() behavior differs from the parent class FieldOptimizationInfo.
     * The parent class returns true by default (conservative), while ProgramFieldOptimizationInfo
     * can return false (allowing for more precise tracking).
     */
    @Test
    public void testIsWritten_differentFromParentClass() {
        // Arrange
        FieldOptimizationInfo baseInfo = new FieldOptimizationInfo();
        ProgramFieldOptimizationInfo programInfo = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean baseResult = baseInfo.isWritten();
        boolean programResult = programInfo.isWritten();

        // Assert
        assertTrue(baseResult, "FieldOptimizationInfo.isWritten() should return true (conservative)");
        assertFalse(programResult, "ProgramFieldOptimizationInfo.isWritten() should return false initially");
        assertNotEquals(baseResult, programResult, "Results should be different");
    }

    /**
     * Tests that isWritten() returns the correct value for multiple independent instances
     * with different states.
     */
    @Test
    public void testIsWritten_multipleInstancesWithDifferentStates() {
        // Arrange
        ProgramFieldOptimizationInfo info1 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info2 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info3 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act - set only info1 and info3
        info1.setWritten();
        info3.setWritten();

        // Assert
        assertTrue(info1.isWritten(), "First instance should return true");
        assertFalse(info2.isWritten(), "Second instance should return false");
        assertTrue(info3.isWritten(), "Third instance should return true");
    }

    /**
     * Tests that isWritten() correctly reports state when combined with all other setters.
     */
    @Test
    public void testIsWritten_withAllFlagsSet_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();
        info.setRead();
        info.setCanNotBeMadePrivate();

        // Assert
        assertTrue(info.isWritten(), "isWritten should return true when all flags are set");
        assertTrue(info.isRead(), "isRead should also be true");
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
    }
}

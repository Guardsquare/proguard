package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ProgramFieldOptimizationInfo#canBeMadePrivate()}.
 *
 * The canBeMadePrivate() method returns whether the field can be made private.
 * It returns the value of the internal canBeMadePrivate flag, which is:
 * - true by default (optimistic assumption that field can be made private)
 * - false after setCanNotBeMadePrivate() is called
 *
 * The canBeMadePrivate field is volatile for thread safety.
 */
public class ProgramFieldOptimizationInfoClaude_canBeMadePrivateTest {

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
     * Tests that canBeMadePrivate() returns true for a newly created instance.
     */
    @Test
    public void testCanBeMadePrivate_newInstance_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.canBeMadePrivate();

        // Assert
        assertTrue(result, "canBeMadePrivate should return true for a new instance");
    }

    /**
     * Tests that canBeMadePrivate() returns false after setCanNotBeMadePrivate() is called.
     */
    @Test
    public void testCanBeMadePrivate_afterSetCanNotBeMadePrivate_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();
        boolean result = info.canBeMadePrivate();

        // Assert
        assertFalse(result, "canBeMadePrivate should return false after setCanNotBeMadePrivate()");
    }

    /**
     * Tests that canBeMadePrivate() returns true regardless of alwaysInitializeValue.
     */
    @Test
    public void testCanBeMadePrivate_withAlwaysInitializeValueTrue_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, true);

        // Act
        boolean result = info.canBeMadePrivate();

        // Assert
        assertTrue(result, "canBeMadePrivate should return true regardless of alwaysInitializeValue");
    }

    /**
     * Tests that canBeMadePrivate() returns true with alwaysInitializeValue false.
     */
    @Test
    public void testCanBeMadePrivate_withAlwaysInitializeValueFalse_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.canBeMadePrivate();

        // Assert
        assertTrue(result, "canBeMadePrivate should return true by default");
    }

    /**
     * Tests that canBeMadePrivate() is consistent across multiple calls.
     */
    @Test
    public void testCanBeMadePrivate_calledMultipleTimes_consistentResult() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result1 = info.canBeMadePrivate();
        boolean result2 = info.canBeMadePrivate();
        boolean result3 = info.canBeMadePrivate();

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that canBeMadePrivate() is consistent across multiple calls after setCanNotBeMadePrivate().
     */
    @Test
    public void testCanBeMadePrivate_calledMultipleTimesAfterSet_consistentlyReturnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setCanNotBeMadePrivate();

        // Act
        boolean result1 = info.canBeMadePrivate();
        boolean result2 = info.canBeMadePrivate();
        boolean result3 = info.canBeMadePrivate();

        // Assert
        assertFalse(result1, "First call should return false");
        assertFalse(result2, "Second call should return false");
        assertFalse(result3, "Third call should return false");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that canBeMadePrivate() is not affected by setWritten().
     */
    @Test
    public void testCanBeMadePrivate_afterSetWritten_remainsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();
        boolean result = info.canBeMadePrivate();

        // Assert
        assertTrue(result, "canBeMadePrivate should remain true after setWritten()");
    }

    /**
     * Tests that canBeMadePrivate() is not affected by setRead().
     */
    @Test
    public void testCanBeMadePrivate_afterSetRead_remainsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();
        boolean result = info.canBeMadePrivate();

        // Assert
        assertTrue(result, "canBeMadePrivate should remain true after setRead()");
    }

    /**
     * Tests that canBeMadePrivate() returns false when setCanNotBeMadePrivate() is called
     * along with other setters.
     */
    @Test
    public void testCanBeMadePrivate_withMultipleSetters_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();
        info.setRead();
        info.setCanNotBeMadePrivate();
        boolean result = info.canBeMadePrivate();

        // Assert
        assertFalse(result, "canBeMadePrivate should return false when setCanNotBeMadePrivate is called");
    }

    /**
     * Tests that canBeMadePrivate() returns true for a static field.
     */
    @Test
    public void testCanBeMadePrivate_staticField_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.canBeMadePrivate();

        // Assert
        assertTrue(result, "canBeMadePrivate should return true for static fields by default");
    }

    /**
     * Tests that canBeMadePrivate() returns true for a final field.
     */
    @Test
    public void testCanBeMadePrivate_finalField_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.canBeMadePrivate();

        // Assert
        assertTrue(result, "canBeMadePrivate should return true for final fields by default");
    }

    /**
     * Tests that canBeMadePrivate() returns true for a volatile field.
     */
    @Test
    public void testCanBeMadePrivate_volatileField_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.canBeMadePrivate();

        // Assert
        assertTrue(result, "canBeMadePrivate should return true for volatile fields by default");
    }

    /**
     * Tests that canBeMadePrivate() returns true for a field with multiple access flags.
     */
    @Test
    public void testCanBeMadePrivate_multipleAccessFlags_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.canBeMadePrivate();

        // Assert
        assertTrue(result, "canBeMadePrivate should return true by default");
    }

    /**
     * Tests that canBeMadePrivate() returns the correct value for different field types (int).
     */
    @Test
    public void testCanBeMadePrivate_intField_returnsTrue() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.canBeMadePrivate();

        // Assert
        assertTrue(result, "canBeMadePrivate should return true for int fields by default");
    }

    /**
     * Tests that canBeMadePrivate() returns the correct value for different field types (object).
     */
    @Test
    public void testCanBeMadePrivate_objectField_returnsTrue() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("Ljava/lang/String;");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.canBeMadePrivate();

        // Assert
        assertTrue(result, "canBeMadePrivate should return true for object fields by default");
    }

    /**
     * Tests that canBeMadePrivate() returns the correct value for different field types (array).
     */
    @Test
    public void testCanBeMadePrivate_arrayField_returnsTrue() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("[I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.canBeMadePrivate();

        // Assert
        assertTrue(result, "canBeMadePrivate should return true for array fields by default");
    }

    /**
     * Tests that canBeMadePrivate() returns the correct value after resetValue() is called.
     * resetValue() should not affect the canBeMadePrivate flag.
     */
    @Test
    public void testCanBeMadePrivate_afterResetValue_remainsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setCanNotBeMadePrivate();
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false initially");

        // Act
        info.resetValue(mockClazz, mockField);
        boolean result = info.canBeMadePrivate();

        // Assert
        assertFalse(result, "canBeMadePrivate should remain false after resetValue()");
    }

    /**
     * Tests that canBeMadePrivate() returns the correct value for a copied instance.
     */
    @Test
    public void testCanBeMadePrivate_copiedInstance_returnsSameValue() {
        // Arrange
        ProgramFieldOptimizationInfo original = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        original.setCanNotBeMadePrivate();
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(original);

        // Act
        boolean originalResult = original.canBeMadePrivate();
        boolean copyResult = copy.canBeMadePrivate();

        // Assert
        assertFalse(originalResult, "Original should have canBeMadePrivate = false");
        assertFalse(copyResult, "Copy should have canBeMadePrivate = false");
        assertEquals(originalResult, copyResult, "Copy should match original");
    }

    /**
     * Tests that canBeMadePrivate() returns true for a copy of an instance where canBeMadePrivate is true.
     */
    @Test
    public void testCanBeMadePrivate_copiedInstanceWithTrue_returnsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo original = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(original);

        // Act
        boolean originalResult = original.canBeMadePrivate();
        boolean copyResult = copy.canBeMadePrivate();

        // Assert
        assertTrue(originalResult, "Original should have canBeMadePrivate = true");
        assertTrue(copyResult, "Copy should have canBeMadePrivate = true");
        assertEquals(originalResult, copyResult, "Copy should match original");
    }

    /**
     * Tests that canBeMadePrivate() is independent across different instances.
     */
    @Test
    public void testCanBeMadePrivate_independentInstances_returnDifferentValues() {
        // Arrange
        ProgramFieldOptimizationInfo info1 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info2 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info1.setCanNotBeMadePrivate();

        // Act
        boolean result1 = info1.canBeMadePrivate();
        boolean result2 = info2.canBeMadePrivate();

        // Assert
        assertFalse(result1, "First instance should have canBeMadePrivate = false");
        assertTrue(result2, "Second instance should have canBeMadePrivate = true");
        assertNotEquals(result1, result2, "Instances should be independent");
    }

    /**
     * Tests that canBeMadePrivate() does not throw any exceptions.
     */
    @Test
    public void testCanBeMadePrivate_doesNotThrowException() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act & Assert
        assertDoesNotThrow(() -> info.canBeMadePrivate(), "canBeMadePrivate should not throw any exceptions");
    }

    /**
     * Tests that canBeMadePrivate() can be called many times without issues.
     */
    @Test
    public void testCanBeMadePrivate_rapidSequentialCalls_handlesCorrectly() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setCanNotBeMadePrivate();

        // Act & Assert
        for (int i = 0; i < 1000; i++) {
            assertFalse(info.canBeMadePrivate(), "Call " + i + " should return false");
        }
    }

    /**
     * Tests that canBeMadePrivate() reflects the state transition from true to false.
     */
    @Test
    public void testCanBeMadePrivate_stateTransition_trueToFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act & Assert - before setCanNotBeMadePrivate
        assertTrue(info.canBeMadePrivate(), "canBeMadePrivate should be true initially");

        // Act - call setCanNotBeMadePrivate
        info.setCanNotBeMadePrivate();

        // Assert - after setCanNotBeMadePrivate
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false after setCanNotBeMadePrivate()");
    }

    /**
     * Tests that canBeMadePrivate() returns false when setCanNotBeMadePrivate() is called multiple times.
     */
    @Test
    public void testCanBeMadePrivate_afterMultipleSetCalls_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();
        info.setCanNotBeMadePrivate();
        info.setCanNotBeMadePrivate();
        boolean result = info.canBeMadePrivate();

        // Assert
        assertFalse(result, "canBeMadePrivate should return false after multiple setCanNotBeMadePrivate() calls");
    }

    /**
     * Tests that canBeMadePrivate() works correctly with mixed method calls.
     */
    @Test
    public void testCanBeMadePrivate_withMixedMethodCalls_returnsCorrectValue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act - call various methods
        info.setWritten();
        assertTrue(info.canBeMadePrivate(), "canBeMadePrivate should be true after setWritten()");

        info.setRead();
        assertTrue(info.canBeMadePrivate(), "canBeMadePrivate should be true after setRead()");

        info.setCanNotBeMadePrivate();
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false after setCanNotBeMadePrivate()");
    }

    /**
     * Tests that canBeMadePrivate() behavior differs from the parent class FieldOptimizationInfo.
     * The parent class returns false by default (conservative), while ProgramFieldOptimizationInfo
     * returns true (optimistic).
     */
    @Test
    public void testCanBeMadePrivate_differentFromParentClass() {
        // Arrange
        FieldOptimizationInfo baseInfo = new FieldOptimizationInfo();
        ProgramFieldOptimizationInfo programInfo = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean baseResult = baseInfo.canBeMadePrivate();
        boolean programResult = programInfo.canBeMadePrivate();

        // Assert
        assertFalse(baseResult, "FieldOptimizationInfo.canBeMadePrivate() should return false (conservative)");
        assertTrue(programResult, "ProgramFieldOptimizationInfo.canBeMadePrivate() should return true initially");
        assertNotEquals(baseResult, programResult, "Results should be different");
    }

    /**
     * Tests that canBeMadePrivate() returns the correct value for multiple independent instances
     * with different states.
     */
    @Test
    public void testCanBeMadePrivate_multipleInstancesWithDifferentStates() {
        // Arrange
        ProgramFieldOptimizationInfo info1 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info2 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info3 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act - set only info1 and info3
        info1.setCanNotBeMadePrivate();
        info3.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info1.canBeMadePrivate(), "First instance should return false");
        assertTrue(info2.canBeMadePrivate(), "Second instance should return true");
        assertFalse(info3.canBeMadePrivate(), "Third instance should return false");
    }

    /**
     * Tests that canBeMadePrivate() correctly reports state when combined with all other setters.
     */
    @Test
    public void testCanBeMadePrivate_withAllFlagsSet_returnsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();
        info.setRead();
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should return false when setCanNotBeMadePrivate is called");
        assertTrue(info.isWritten(), "isWritten should be true");
        assertTrue(info.isRead(), "isRead should be true");
    }

    /**
     * Tests that canBeMadePrivate() returns true initially for volatile field.
     */
    @Test
    public void testCanBeMadePrivate_volatileFieldBeforeSet_returnsTrue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        boolean result = info.canBeMadePrivate();

        // Assert
        assertTrue(result, "canBeMadePrivate should return true initially even for volatile fields");
    }

    /**
     * Tests that canBeMadePrivate() returns false after setCanNotBeMadePrivate() for volatile field.
     */
    @Test
    public void testCanBeMadePrivate_volatileFieldAfterSet_returnsFalse() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();
        boolean result = info.canBeMadePrivate();

        // Assert
        assertFalse(result, "canBeMadePrivate should return false after setCanNotBeMadePrivate()");
    }
}

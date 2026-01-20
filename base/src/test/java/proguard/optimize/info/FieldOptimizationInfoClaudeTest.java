package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Field;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.evaluation.value.ReferenceValue;
import proguard.evaluation.value.Value;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link FieldOptimizationInfo}.
 *
 * This class stores optimization information that can be attached to a field during
 * ProGuard's optimization process. The tests cover all public methods including:
 * - Constructor and initialization
 * - Field state queries (kept, written, read, private)
 * - Value management (setValue, getValue)
 * - Referenced class tracking
 * - Static helper methods for setting and retrieving optimization info
 */
public class FieldOptimizationInfoClaudeTest {

    // =========================================================================
    // Constructor Tests
    // =========================================================================

    /**
     * Tests that the default constructor successfully creates a FieldOptimizationInfo instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        FieldOptimizationInfo info = new FieldOptimizationInfo();

        // Assert
        assertNotNull(info, "FieldOptimizationInfo instance should be created");
    }

    /**
     * Tests that the constructor initializes value to null by default.
     */
    @Test
    public void testConstructor_initializesValueToNull() {
        // Act
        FieldOptimizationInfo info = new FieldOptimizationInfo();

        // Assert
        assertNull(info.getValue(), "value should be null by default");
    }

    /**
     * Tests that the constructor initializes all conservative defaults correctly.
     */
    @Test
    public void testConstructor_initializesConservativeDefaults() {
        // Act
        FieldOptimizationInfo info = new FieldOptimizationInfo();

        // Assert - conservative defaults (assume everything is true for safety)
        assertTrue(info.isKept(), "isKept should return true by default");
        assertTrue(info.isWritten(), "isWritten should return true by default");
        assertTrue(info.isRead(), "isRead should return true by default");

        // Non-conservative defaults
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should return false by default");
        assertNull(info.getReferencedClass(), "getReferencedClass should return null by default");
        assertNull(info.getValue(), "getValue should return null by default");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        FieldOptimizationInfo info1 = new FieldOptimizationInfo();
        FieldOptimizationInfo info2 = new FieldOptimizationInfo();
        FieldOptimizationInfo info3 = new FieldOptimizationInfo();

        // Assert
        assertNotNull(info1, "First info should be created");
        assertNotNull(info2, "Second info should be created");
        assertNotNull(info3, "Third info should be created");
        assertNotSame(info1, info2, "First and second info should be different instances");
        assertNotSame(info2, info3, "Second and third info should be different instances");
        assertNotSame(info1, info3, "First and third info should be different instances");
    }

    // =========================================================================
    // Field State Query Tests
    // =========================================================================

    /**
     * Tests isKept always returns true (conservative default).
     */
    @Test
    public void testIsKept_alwaysReturnsTrue() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();

        // Act & Assert
        assertTrue(info.isKept(), "isKept should always return true");

        // Even after setting a value
        Value mockValue = mock(Value.class);
        info.setValue(mockValue);
        assertTrue(info.isKept(), "isKept should still return true after setValue");
    }

    /**
     * Tests isWritten always returns true (conservative default).
     */
    @Test
    public void testIsWritten_alwaysReturnsTrue() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();

        // Act & Assert
        assertTrue(info.isWritten(), "isWritten should always return true");

        // Even after setting a value
        Value mockValue = mock(Value.class);
        info.setValue(mockValue);
        assertTrue(info.isWritten(), "isWritten should still return true after setValue");
    }

    /**
     * Tests isRead always returns true (conservative default).
     */
    @Test
    public void testIsRead_alwaysReturnsTrue() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();

        // Act & Assert
        assertTrue(info.isRead(), "isRead should always return true");

        // Even after setting a value
        Value mockValue = mock(Value.class);
        info.setValue(mockValue);
        assertTrue(info.isRead(), "isRead should still return true after setValue");
    }

    /**
     * Tests canBeMadePrivate always returns false.
     */
    @Test
    public void testCanBeMadePrivate_alwaysReturnsFalse() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();

        // Act & Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should always return false");

        // Even after setting a value
        Value mockValue = mock(Value.class);
        info.setValue(mockValue);
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should still return false after setValue");
    }

    /**
     * Tests getReferencedClass always returns null.
     */
    @Test
    public void testGetReferencedClass_alwaysReturnsNull() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();

        // Act & Assert
        assertNull(info.getReferencedClass(), "getReferencedClass should always return null");

        // Even after setting a value
        Value mockValue = mock(Value.class);
        info.setValue(mockValue);
        assertNull(info.getReferencedClass(), "getReferencedClass should still return null after setValue");
    }

    // =========================================================================
    // Value Management Tests
    // =========================================================================

    /**
     * Tests setValue method sets the value field.
     */
    @Test
    public void testSetValue_setsValue() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();
        Value mockValue = mock(Value.class);
        assertNull(info.getValue(), "Value should initially be null");

        // Act
        info.setValue(mockValue);

        // Assert
        assertNotNull(info.getValue(), "Value should not be null after setValue");
        assertSame(mockValue, info.getValue(), "getValue should return the same value that was set");
    }

    /**
     * Tests setValue can be called with null.
     */
    @Test
    public void testSetValue_canSetNull() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();
        Value mockValue = mock(Value.class);
        info.setValue(mockValue);
        assertNotNull(info.getValue(), "Value should be set initially");

        // Act
        info.setValue(null);

        // Assert
        assertNull(info.getValue(), "Value should be null after setting to null");
    }

    /**
     * Tests setValue can be called multiple times (replaces previous value).
     */
    @Test
    public void testSetValue_canBeCalledMultipleTimes() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();
        Value mockValue1 = mock(Value.class);
        Value mockValue2 = mock(Value.class);
        Value mockValue3 = mock(Value.class);

        // Act & Assert - set first value
        info.setValue(mockValue1);
        assertSame(mockValue1, info.getValue(), "Should return first value");

        // Act & Assert - set second value
        info.setValue(mockValue2);
        assertSame(mockValue2, info.getValue(), "Should return second value");

        // Act & Assert - set third value
        info.setValue(mockValue3);
        assertSame(mockValue3, info.getValue(), "Should return third value");
    }

    /**
     * Tests setValue with different Value implementations.
     */
    @Test
    public void testSetValue_withDifferentValueTypes() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();
        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        Value nonParticularValue = mock(Value.class);
        when(nonParticularValue.isParticular()).thenReturn(false);

        // Act & Assert - particular value
        info.setValue(particularValue);
        assertSame(particularValue, info.getValue(), "Should store particular value");
        assertTrue(info.getValue().isParticular(), "Stored value should be particular");

        // Act & Assert - non-particular value
        info.setValue(nonParticularValue);
        assertSame(nonParticularValue, info.getValue(), "Should store non-particular value");
        assertFalse(info.getValue().isParticular(), "Stored value should not be particular");
    }

    /**
     * Tests getValue returns null initially.
     */
    @Test
    public void testGetValue_returnsNullInitially() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();

        // Act & Assert
        assertNull(info.getValue(), "getValue should return null initially");
    }

    /**
     * Tests getValue returns the value that was set.
     */
    @Test
    public void testGetValue_returnsSetValue() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();
        Value mockValue = mock(Value.class);
        info.setValue(mockValue);

        // Act
        Value retrievedValue = info.getValue();

        // Assert
        assertNotNull(retrievedValue, "Retrieved value should not be null");
        assertSame(mockValue, retrievedValue, "Retrieved value should be the same as the set value");
    }

    /**
     * Tests getValue returns null after setting to null.
     */
    @Test
    public void testGetValue_returnsNullAfterSettingToNull() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();
        Value mockValue = mock(Value.class);
        info.setValue(mockValue);
        assertNotNull(info.getValue(), "Value should be set initially");

        // Act
        info.setValue(null);

        // Assert
        assertNull(info.getValue(), "getValue should return null after setting to null");
    }

    // =========================================================================
    // Static Helper Methods Tests
    // =========================================================================

    /**
     * Tests setFieldOptimizationInfo creates and attaches info to a ProgramField.
     */
    @Test
    public void testSetFieldOptimizationInfo_attachesToProgramField() {
        // Arrange
        Clazz clazz = new ProgramClass();
        Field field = new ProgramField();

        // Act
        FieldOptimizationInfo.setFieldOptimizationInfo(clazz, field);

        // Assert
        Object processingInfo = field.getProcessingInfo();
        assertNotNull(processingInfo, "Processing info should not be null");
        assertInstanceOf(FieldOptimizationInfo.class, processingInfo, "Processing info should be FieldOptimizationInfo");
    }

    /**
     * Tests setFieldOptimizationInfo creates and attaches info with default values.
     */
    @Test
    public void testSetFieldOptimizationInfo_createsDefaultInfo() {
        // Arrange
        Clazz clazz = new ProgramClass();
        Field field = new ProgramField();

        // Act
        FieldOptimizationInfo.setFieldOptimizationInfo(clazz, field);

        // Assert
        FieldOptimizationInfo info = FieldOptimizationInfo.getFieldOptimizationInfo(field);
        assertNotNull(info, "Retrieved info should not be null");
        assertTrue(info.isKept(), "New info should return true for isKept");
        assertTrue(info.isWritten(), "New info should return true for isWritten");
        assertTrue(info.isRead(), "New info should return true for isRead");
        assertFalse(info.canBeMadePrivate(), "New info should return false for canBeMadePrivate");
        assertNull(info.getValue(), "New info should have null value");
        assertNull(info.getReferencedClass(), "New info should have null referenced class");
    }

    /**
     * Tests setFieldOptimizationInfo can be called multiple times (replaces previous info).
     */
    @Test
    public void testSetFieldOptimizationInfo_canBeCalledMultipleTimes() {
        // Arrange
        Clazz clazz = new ProgramClass();
        Field field = new ProgramField();

        // Act - call multiple times
        FieldOptimizationInfo.setFieldOptimizationInfo(clazz, field);
        FieldOptimizationInfo info1 = FieldOptimizationInfo.getFieldOptimizationInfo(field);
        Value mockValue1 = mock(Value.class);
        info1.setValue(mockValue1);

        FieldOptimizationInfo.setFieldOptimizationInfo(clazz, field);
        FieldOptimizationInfo info2 = FieldOptimizationInfo.getFieldOptimizationInfo(field);

        // Assert - should get different instances
        assertNotNull(info1, "First info should not be null");
        assertNotNull(info2, "Second info should not be null");
        assertNotSame(info1, info2, "Multiple calls should create new instances");
        assertNull(info2.getValue(), "New info should have null value (not carry over from previous)");
    }

    /**
     * Tests setFieldOptimizationInfo with null clazz parameter.
     */
    @Test
    public void testSetFieldOptimizationInfo_withNullClazz() {
        // Arrange
        Field field = new ProgramField();

        // Act - pass null clazz (should not throw, clazz is not used in base implementation)
        assertDoesNotThrow(() -> {
            FieldOptimizationInfo.setFieldOptimizationInfo(null, field);
        }, "Should not throw exception when clazz is null");

        // Assert
        Object processingInfo = field.getProcessingInfo();
        assertNotNull(processingInfo, "Processing info should still be set");
        assertInstanceOf(FieldOptimizationInfo.class, processingInfo, "Processing info should be FieldOptimizationInfo");
    }

    /**
     * Tests getFieldOptimizationInfo retrieves the info from a ProgramField.
     */
    @Test
    public void testGetFieldOptimizationInfo_retrievesFromProgramField() {
        // Arrange
        Clazz clazz = new ProgramClass();
        Field field = new ProgramField();
        FieldOptimizationInfo.setFieldOptimizationInfo(clazz, field);

        // Act
        FieldOptimizationInfo info = FieldOptimizationInfo.getFieldOptimizationInfo(field);

        // Assert
        assertNotNull(info, "Retrieved info should not be null");
        assertInstanceOf(FieldOptimizationInfo.class, info, "Retrieved info should be FieldOptimizationInfo");
    }

    /**
     * Tests getFieldOptimizationInfo returns the same instance that was set.
     */
    @Test
    public void testGetFieldOptimizationInfo_returnsSameInstance() {
        // Arrange
        Clazz clazz = new ProgramClass();
        Field field = new ProgramField();
        FieldOptimizationInfo.setFieldOptimizationInfo(clazz, field);

        // Act
        FieldOptimizationInfo info1 = FieldOptimizationInfo.getFieldOptimizationInfo(field);
        FieldOptimizationInfo info2 = FieldOptimizationInfo.getFieldOptimizationInfo(field);

        // Assert
        assertSame(info1, info2, "Multiple retrievals should return the same instance");
    }

    /**
     * Tests that modifications to retrieved info are reflected in subsequent retrievals.
     */
    @Test
    public void testGetFieldOptimizationInfo_modificationsArePersisted() {
        // Arrange
        Clazz clazz = new ProgramClass();
        Field field = new ProgramField();
        FieldOptimizationInfo.setFieldOptimizationInfo(clazz, field);

        // Act - retrieve and modify
        FieldOptimizationInfo info1 = FieldOptimizationInfo.getFieldOptimizationInfo(field);
        assertNull(info1.getValue(), "Should initially have null value");
        Value mockValue = mock(Value.class);
        info1.setValue(mockValue);

        // Retrieve again
        FieldOptimizationInfo info2 = FieldOptimizationInfo.getFieldOptimizationInfo(field);

        // Assert - modification should be visible
        assertNotNull(info2.getValue(), "Modification should be visible in subsequent retrieval");
        assertSame(mockValue, info2.getValue(), "Should retrieve the same value that was set");
    }

    /**
     * Tests getFieldOptimizationInfo with a field that has no optimization info set.
     */
    @Test
    public void testGetFieldOptimizationInfo_withNoInfoSet() {
        // Arrange
        Field field = new ProgramField();
        // Note: No optimization info is set

        // Act
        FieldOptimizationInfo info = FieldOptimizationInfo.getFieldOptimizationInfo(field);

        // Assert - should return null or handle gracefully
        assertNull(info, "Should return null when no optimization info is set");
    }

    /**
     * Tests integration of setFieldOptimizationInfo and getFieldOptimizationInfo.
     */
    @Test
    public void testSetAndGetFieldOptimizationInfo_integration() {
        // Arrange
        Clazz clazz = new ProgramClass();
        Field field = new ProgramField();

        // Act - set and get
        FieldOptimizationInfo.setFieldOptimizationInfo(clazz, field);
        FieldOptimizationInfo info = FieldOptimizationInfo.getFieldOptimizationInfo(field);

        // Assert - verify it's a fresh instance with default values
        assertNotNull(info, "Retrieved info should not be null");
        assertTrue(info.isKept(), "New info should return true for isKept");
        assertTrue(info.isWritten(), "New info should return true for isWritten");
        assertTrue(info.isRead(), "New info should return true for isRead");
        assertFalse(info.canBeMadePrivate(), "New info should return false for canBeMadePrivate");
        assertNull(info.getValue(), "New info should have null value");
        assertNull(info.getReferencedClass(), "New info should have null referenced class");

        // Act - modify and verify
        Value mockValue = mock(Value.class);
        info.setValue(mockValue);
        assertSame(mockValue, info.getValue(), "Modification should be applied");
    }

    // =========================================================================
    // Edge Cases and Integration Tests
    // =========================================================================

    /**
     * Tests that FieldOptimizationInfo works correctly when attached to multiple fields.
     */
    @Test
    public void testFieldOptimizationInfo_worksWithMultipleFields() {
        // Arrange
        Clazz clazz = new ProgramClass();
        Field field1 = new ProgramField();
        Field field2 = new ProgramField();
        Field field3 = new ProgramField();

        // Act - set optimization info on all fields
        FieldOptimizationInfo.setFieldOptimizationInfo(clazz, field1);
        FieldOptimizationInfo.setFieldOptimizationInfo(clazz, field2);
        FieldOptimizationInfo.setFieldOptimizationInfo(clazz, field3);

        // Act - modify one
        FieldOptimizationInfo info1 = FieldOptimizationInfo.getFieldOptimizationInfo(field1);
        Value mockValue1 = mock(Value.class);
        info1.setValue(mockValue1);

        // Retrieve all
        FieldOptimizationInfo retrievedInfo1 = FieldOptimizationInfo.getFieldOptimizationInfo(field1);
        FieldOptimizationInfo retrievedInfo2 = FieldOptimizationInfo.getFieldOptimizationInfo(field2);
        FieldOptimizationInfo retrievedInfo3 = FieldOptimizationInfo.getFieldOptimizationInfo(field3);

        // Assert - each field has independent info
        assertNotNull(retrievedInfo1.getValue(), "Field1 should have a value");
        assertSame(mockValue1, retrievedInfo1.getValue(), "Field1 should have the correct value");
        assertNull(retrievedInfo2.getValue(), "Field2 should still have null value");
        assertNull(retrievedInfo3.getValue(), "Field3 should still have null value");
    }

    /**
     * Tests that all methods can be called without throwing exceptions.
     */
    @Test
    public void testAllMethods_noExceptions() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();
        Value mockValue = mock(Value.class);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> {
            info.isKept();
            info.isWritten();
            info.isRead();
            info.canBeMadePrivate();
            info.getReferencedClass();
            info.setValue(mockValue);
            info.getValue();
            info.setValue(null);
            info.getValue();
        }, "All methods should execute without exceptions");
    }

    /**
     * Tests thread safety of creating multiple FieldOptimizationInfo instances.
     */
    @Test
    public void testConstructor_threadSafe() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final int instancesPerThread = 100;
        Thread[] threads = new Thread[threadCount];
        final FieldOptimizationInfo[][] results = new FieldOptimizationInfo[threadCount][instancesPerThread];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < instancesPerThread; j++) {
                    results[threadIndex][j] = new FieldOptimizationInfo();
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify all instances were created successfully
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < instancesPerThread; j++) {
                assertNotNull(results[i][j], "Instance [" + i + "][" + j + "] should be created");
                assertNull(results[i][j].getValue(), "Instance should have default state");
            }
        }
    }

    /**
     * Tests that FieldOptimizationInfo instances are independent.
     */
    @Test
    public void testInstances_areIndependent() {
        // Arrange
        FieldOptimizationInfo info1 = new FieldOptimizationInfo();
        FieldOptimizationInfo info2 = new FieldOptimizationInfo();
        Value mockValue1 = mock(Value.class);
        Value mockValue2 = mock(Value.class);

        // Act - modify first instance
        info1.setValue(mockValue1);

        // Assert - second instance should not be affected
        assertSame(mockValue1, info1.getValue(), "First instance should have the first value");
        assertNull(info2.getValue(), "Second instance should still have null value");

        // Act - modify second instance
        info2.setValue(mockValue2);

        // Assert - first instance should not be affected
        assertSame(mockValue1, info1.getValue(), "First instance should still have the first value");
        assertSame(mockValue2, info2.getValue(), "Second instance should have the second value");
    }

    /**
     * Tests that state query methods are consistent across multiple calls.
     */
    @Test
    public void testStateQueryMethods_areConsistent() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();

        // Act & Assert - call multiple times
        for (int i = 0; i < 10; i++) {
            assertTrue(info.isKept(), "isKept should consistently return true");
            assertTrue(info.isWritten(), "isWritten should consistently return true");
            assertTrue(info.isRead(), "isRead should consistently return true");
            assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should consistently return false");
            assertNull(info.getReferencedClass(), "getReferencedClass should consistently return null");
        }
    }

    /**
     * Tests setValue and getValue integration with complex scenarios.
     */
    @Test
    public void testSetValueGetValue_integration() {
        // Arrange
        FieldOptimizationInfo info = new FieldOptimizationInfo();
        Value value1 = mock(Value.class);
        Value value2 = mock(Value.class);
        Value value3 = mock(Value.class);

        // Act & Assert - initial state
        assertNull(info.getValue(), "Initially should be null");

        // Set and verify first value
        info.setValue(value1);
        assertSame(value1, info.getValue(), "Should return first value");

        // Replace with second value
        info.setValue(value2);
        assertSame(value2, info.getValue(), "Should return second value");
        assertNotSame(value1, info.getValue(), "Should not return first value anymore");

        // Replace with third value
        info.setValue(value3);
        assertSame(value3, info.getValue(), "Should return third value");

        // Set to null
        info.setValue(null);
        assertNull(info.getValue(), "Should return null after setting to null");

        // Set a value again
        info.setValue(value1);
        assertSame(value1, info.getValue(), "Should return value after setting again");
    }

    /**
     * Tests static helper methods work correctly with multiple fields and clazzes.
     */
    @Test
    public void testStaticHelpers_multipleFieldsAndClazzes() {
        // Arrange
        Clazz clazz1 = new ProgramClass();
        Clazz clazz2 = new ProgramClass();
        Field field1 = new ProgramField();
        Field field2 = new ProgramField();
        Field field3 = new ProgramField();

        // Act - set info on different fields with different clazzes
        FieldOptimizationInfo.setFieldOptimizationInfo(clazz1, field1);
        FieldOptimizationInfo.setFieldOptimizationInfo(clazz1, field2);
        FieldOptimizationInfo.setFieldOptimizationInfo(clazz2, field3);

        // Modify each independently
        FieldOptimizationInfo info1 = FieldOptimizationInfo.getFieldOptimizationInfo(field1);
        FieldOptimizationInfo info2 = FieldOptimizationInfo.getFieldOptimizationInfo(field2);
        FieldOptimizationInfo info3 = FieldOptimizationInfo.getFieldOptimizationInfo(field3);

        Value value1 = mock(Value.class);
        Value value2 = mock(Value.class);
        Value value3 = mock(Value.class);

        info1.setValue(value1);
        info2.setValue(value2);
        info3.setValue(value3);

        // Assert - each field maintains its own info
        assertSame(value1, FieldOptimizationInfo.getFieldOptimizationInfo(field1).getValue());
        assertSame(value2, FieldOptimizationInfo.getFieldOptimizationInfo(field2).getValue());
        assertSame(value3, FieldOptimizationInfo.getFieldOptimizationInfo(field3).getValue());
    }
}

package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.evaluation.value.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ProgramFieldOptimizationInfo#generalizeReferencedClass(ReferenceValue)}.
 *
 * The generalizeReferencedClass() method maintains a representation of the class through which
 * a field is accessed. When called multiple times, it generalizes the current referencedClass
 * with the new one by calling the generalize() method on ReferenceValue.
 *
 * The method is synchronized for thread safety and handles the case where referencedClass
 * is initially null.
 */
public class ProgramFieldOptimizationInfoClaude_generalizeReferencedClassTest {

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
     * Tests that generalizeReferencedClass sets the referencedClass when it is initially null.
     */
    @Test
    public void testGeneralizeReferencedClass_whenNull_setsValue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue refValue = mock(ReferenceValue.class);
        assertNull(info.getReferencedClass(), "referencedClass should be null initially");

        // Act
        info.generalizeReferencedClass(refValue);

        // Assert
        assertEquals(refValue, info.getReferencedClass(), "referencedClass should be set to the provided value");
    }

    /**
     * Tests that generalizeReferencedClass generalizes when referencedClass is not null.
     */
    @Test
    public void testGeneralizeReferencedClass_whenNotNull_generalizesValue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        ReferenceValue firstValue = mock(ReferenceValue.class);
        ReferenceValue secondValue = mock(ReferenceValue.class);
        ReferenceValue generalizedValue = mock(ReferenceValue.class);

        when(firstValue.generalize(secondValue)).thenReturn(generalizedValue);

        // Act
        info.generalizeReferencedClass(firstValue);
        info.generalizeReferencedClass(secondValue);

        // Assert
        assertEquals(generalizedValue, info.getReferencedClass(),
            "referencedClass should be the generalized result");
        verify(firstValue).generalize(secondValue);
    }

    /**
     * Tests that generalizeReferencedClass is idempotent when called with the same value.
     */
    @Test
    public void testGeneralizeReferencedClass_calledTwiceWithSameValue_generalizes() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue refValue = mock(ReferenceValue.class);
        ReferenceValue generalizedValue = mock(ReferenceValue.class);

        when(refValue.generalize(refValue)).thenReturn(generalizedValue);

        // Act
        info.generalizeReferencedClass(refValue);
        info.generalizeReferencedClass(refValue);

        // Assert
        assertEquals(generalizedValue, info.getReferencedClass(),
            "referencedClass should be the generalized result");
        verify(refValue).generalize(refValue);
    }

    /**
     * Tests that generalizeReferencedClass works correctly when called multiple times
     * with different values.
     */
    @Test
    public void testGeneralizeReferencedClass_multipleValues_chainsGeneralization() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        ReferenceValue value1 = mock(ReferenceValue.class);
        ReferenceValue value2 = mock(ReferenceValue.class);
        ReferenceValue value3 = mock(ReferenceValue.class);
        ReferenceValue generalized1_2 = mock(ReferenceValue.class);
        ReferenceValue generalized_all = mock(ReferenceValue.class);

        when(value1.generalize(value2)).thenReturn(generalized1_2);
        when(generalized1_2.generalize(value3)).thenReturn(generalized_all);

        // Act
        info.generalizeReferencedClass(value1);
        info.generalizeReferencedClass(value2);
        info.generalizeReferencedClass(value3);

        // Assert
        assertEquals(generalized_all, info.getReferencedClass(),
            "referencedClass should be the final generalized result");
        verify(value1).generalize(value2);
        verify(generalized1_2).generalize(value3);
    }

    /**
     * Tests that generalizeReferencedClass does not affect other fields.
     */
    @Test
    public void testGeneralizeReferencedClass_doesNotAffectOtherFields() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();
        info.setRead();

        ReferenceValue refValue = mock(ReferenceValue.class);

        // Act
        info.generalizeReferencedClass(refValue);

        // Assert
        assertTrue(info.isWritten(), "isWritten should remain true");
        assertTrue(info.isRead(), "isRead should remain true");
        assertTrue(info.canBeMadePrivate(), "canBeMadePrivate should remain true");
    }

    /**
     * Tests that generalizeReferencedClass works correctly with different field types.
     */
    @Test
    public void testGeneralizeReferencedClass_withObjectField() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("Ljava/lang/String;");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue refValue = mock(ReferenceValue.class);

        // Act
        info.generalizeReferencedClass(refValue);

        // Assert
        assertEquals(refValue, info.getReferencedClass(),
            "referencedClass should be set for object field");
    }

    /**
     * Tests that generalizeReferencedClass works correctly with static fields.
     */
    @Test
    public void testGeneralizeReferencedClass_withStaticField() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue refValue = mock(ReferenceValue.class);

        // Act
        info.generalizeReferencedClass(refValue);

        // Assert
        assertEquals(refValue, info.getReferencedClass(),
            "referencedClass should be set for static field");
    }

    /**
     * Tests that generalizeReferencedClass works correctly with volatile fields.
     */
    @Test
    public void testGeneralizeReferencedClass_withVolatileField() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue refValue = mock(ReferenceValue.class);

        // Act
        info.generalizeReferencedClass(refValue);

        // Assert
        assertEquals(refValue, info.getReferencedClass(),
            "referencedClass should be set for volatile field");
    }

    /**
     * Tests that generalizeReferencedClass works correctly with final fields.
     */
    @Test
    public void testGeneralizeReferencedClass_withFinalField() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue refValue = mock(ReferenceValue.class);

        // Act
        info.generalizeReferencedClass(refValue);

        // Assert
        assertEquals(refValue, info.getReferencedClass(),
            "referencedClass should be set for final field");
    }

    /**
     * Tests that generalizeReferencedClass preserves referencedClass in copy constructor.
     */
    @Test
    public void testGeneralizeReferencedClass_preservedInCopy() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue refValue = mock(ReferenceValue.class);
        info.generalizeReferencedClass(refValue);

        // Act
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(info);

        // Assert
        assertEquals(refValue, copy.getReferencedClass(),
            "referencedClass should be preserved in copy");
    }

    /**
     * Tests that generalizeReferencedClass on a copy does not affect the original.
     */
    @Test
    public void testGeneralizeReferencedClass_onCopy_doesNotAffectOriginal() {
        // Arrange
        ProgramFieldOptimizationInfo original = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue originalValue = mock(ReferenceValue.class);
        original.generalizeReferencedClass(originalValue);

        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(original);
        ReferenceValue copyValue = mock(ReferenceValue.class);
        ReferenceValue generalizedValue = mock(ReferenceValue.class);

        when(originalValue.generalize(copyValue)).thenReturn(generalizedValue);

        // Act
        copy.generalizeReferencedClass(copyValue);

        // Assert
        assertEquals(originalValue, original.getReferencedClass(),
            "Original referencedClass should remain unchanged");
        assertEquals(generalizedValue, copy.getReferencedClass(),
            "Copy referencedClass should be generalized");
    }

    /**
     * Tests that generalizeReferencedClass works correctly with alwaysInitializeValue = true.
     */
    @Test
    public void testGeneralizeReferencedClass_withAlwaysInitializeValueTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, true);
        ReferenceValue refValue = mock(ReferenceValue.class);

        // Act
        info.generalizeReferencedClass(refValue);

        // Assert
        assertEquals(refValue, info.getReferencedClass(),
            "referencedClass should be set with alwaysInitializeValue = true");
    }

    /**
     * Tests that generalizeReferencedClass works correctly with alwaysInitializeValue = false.
     */
    @Test
    public void testGeneralizeReferencedClass_withAlwaysInitializeValueFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue refValue = mock(ReferenceValue.class);

        // Act
        info.generalizeReferencedClass(refValue);

        // Assert
        assertEquals(refValue, info.getReferencedClass(),
            "referencedClass should be set with alwaysInitializeValue = false");
    }

    /**
     * Tests that generalizeReferencedClass does not affect the value field.
     */
    @Test
    public void testGeneralizeReferencedClass_doesNotAffectValue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, true);
        Value originalValue = info.getValue();
        ReferenceValue refValue = mock(ReferenceValue.class);

        // Act
        info.generalizeReferencedClass(refValue);

        // Assert
        assertEquals(originalValue, info.getValue(),
            "Value field should not be affected");
        assertEquals(refValue, info.getReferencedClass(),
            "referencedClass should be set");
    }

    /**
     * Tests that generalizeReferencedClass is independent across multiple instances.
     */
    @Test
    public void testGeneralizeReferencedClass_instancesAreIndependent() {
        // Arrange
        ProgramFieldOptimizationInfo info1 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info2 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        ReferenceValue refValue1 = mock(ReferenceValue.class);
        ReferenceValue refValue2 = mock(ReferenceValue.class);

        // Act
        info1.generalizeReferencedClass(refValue1);
        info2.generalizeReferencedClass(refValue2);

        // Assert
        assertEquals(refValue1, info1.getReferencedClass(),
            "First instance should have its own referencedClass");
        assertEquals(refValue2, info2.getReferencedClass(),
            "Second instance should have its own referencedClass");
        assertNotEquals(info1.getReferencedClass(), info2.getReferencedClass(),
            "Instances should be independent");
    }

    /**
     * Tests that generalizeReferencedClass can be called many times sequentially.
     */
    @Test
    public void testGeneralizeReferencedClass_manySequentialCalls() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        ReferenceValue initial = mock(ReferenceValue.class, "initial");
        ReferenceValue current = initial;

        // Create a chain of generalized values
        for (int i = 0; i < 10; i++) {
            ReferenceValue next = mock(ReferenceValue.class, "value" + i);
            ReferenceValue generalized = mock(ReferenceValue.class, "generalized" + i);
            when(current.generalize(next)).thenReturn(generalized);
            current = generalized;
        }

        // Act & Assert - call 10 times and verify last result
        info.generalizeReferencedClass(initial);
        ReferenceValue lastAdded = null;

        for (int i = 0; i < 10; i++) {
            lastAdded = mock(ReferenceValue.class, "value" + i);
            info.generalizeReferencedClass(lastAdded);
        }

        // Assert that referencedClass was updated (not null)
        assertNotNull(info.getReferencedClass(),
            "referencedClass should be set after many calls");
    }

    /**
     * Tests that generalizeReferencedClass does not throw when given a mocked ReferenceValue.
     */
    @Test
    public void testGeneralizeReferencedClass_doesNotThrowException() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue refValue = mock(ReferenceValue.class);

        // Act & Assert
        assertDoesNotThrow(() -> info.generalizeReferencedClass(refValue),
            "generalizeReferencedClass should not throw any exceptions");
    }

    /**
     * Tests that generalizeReferencedClass persists across multiple getReferencedClass calls.
     */
    @Test
    public void testGeneralizeReferencedClass_persistsAcrossMultipleGets() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue refValue = mock(ReferenceValue.class);

        // Act
        info.generalizeReferencedClass(refValue);

        // Assert - query multiple times
        assertEquals(refValue, info.getReferencedClass(), "First get should return the value");
        assertEquals(refValue, info.getReferencedClass(), "Second get should return the value");
        assertEquals(refValue, info.getReferencedClass(), "Third get should return the value");
    }

    /**
     * Tests that generalizeReferencedClass works correctly after resetValue is called.
     */
    @Test
    public void testGeneralizeReferencedClass_afterResetValue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue refValue = mock(ReferenceValue.class);
        info.generalizeReferencedClass(refValue);

        // Act
        info.resetValue(mockClazz, mockField);

        // Assert - resetValue does not affect referencedClass
        assertEquals(refValue, info.getReferencedClass(),
            "referencedClass should persist after resetValue");
    }

    /**
     * Tests that generalizeReferencedClass with array field type.
     */
    @Test
    public void testGeneralizeReferencedClass_withArrayField() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("[Ljava/lang/Object;");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue refValue = mock(ReferenceValue.class);

        // Act
        info.generalizeReferencedClass(refValue);

        // Assert
        assertEquals(refValue, info.getReferencedClass(),
            "referencedClass should be set for array field");
    }

    /**
     * Tests that generalizeReferencedClass returns the correct result when generalize
     * returns the same reference.
     */
    @Test
    public void testGeneralizeReferencedClass_whenGeneralizeReturnsSameReference() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue refValue1 = mock(ReferenceValue.class);
        ReferenceValue refValue2 = mock(ReferenceValue.class);

        // When generalize returns the first value unchanged
        when(refValue1.generalize(refValue2)).thenReturn(refValue1);

        // Act
        info.generalizeReferencedClass(refValue1);
        info.generalizeReferencedClass(refValue2);

        // Assert
        assertEquals(refValue1, info.getReferencedClass(),
            "referencedClass should be the same reference when generalize returns it");
    }

    /**
     * Tests that generalizeReferencedClass works with all access flags combined.
     */
    @Test
    public void testGeneralizeReferencedClass_withMultipleAccessFlags() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(
            AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue refValue = mock(ReferenceValue.class);

        // Act
        info.generalizeReferencedClass(refValue);

        // Assert
        assertEquals(refValue, info.getReferencedClass(),
            "referencedClass should be set with multiple access flags");
    }

    /**
     * Tests that getReferencedClass returns null when generalizeReferencedClass has not been called.
     */
    @Test
    public void testGetReferencedClass_whenNeverSet_returnsNull() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act & Assert
        assertNull(info.getReferencedClass(),
            "getReferencedClass should return null when never set");
    }
}

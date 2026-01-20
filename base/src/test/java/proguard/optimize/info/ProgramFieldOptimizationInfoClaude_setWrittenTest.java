package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ProgramFieldOptimizationInfo#setWritten()}.
 *
 * The setWritten() method marks that the field has been written to.
 * It sets the internal isWritten flag to true, which can be queried via isWritten().
 *
 * The isWritten field is volatile for thread safety and is initially:
 * - false for regular fields
 * - true for fields with the VOLATILE access flag
 */
public class ProgramFieldOptimizationInfoClaude_setWrittenTest {

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
     * Tests that setWritten() sets the isWritten flag to true.
     */
    @Test
    public void testSetWritten_setsIsWrittenToTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(info.isWritten(), "isWritten should be false initially");

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true after setWritten()");
    }

    /**
     * Tests that setWritten() works correctly when called on a newly created instance.
     */
    @Test
    public void testSetWritten_onNewInstance_changesState() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "setWritten should change isWritten from false to true");
    }

    /**
     * Tests that setWritten() is idempotent - calling it multiple times has the same effect.
     */
    @Test
    public void testSetWritten_calledMultipleTimes_remainsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();
        assertTrue(info.isWritten(), "isWritten should be true after first call");

        info.setWritten();
        assertTrue(info.isWritten(), "isWritten should still be true after second call");

        info.setWritten();
        assertTrue(info.isWritten(), "isWritten should still be true after third call");
    }

    /**
     * Tests that setWritten() works correctly when isWritten is already true (volatile field).
     */
    @Test
    public void testSetWritten_whenAlreadyTrue_remainsTrue() {
        // Arrange - volatile field starts with isWritten = true
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertTrue(info.isWritten(), "Volatile field should have isWritten = true initially");

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should remain true");
    }

    /**
     * Tests that setWritten() does not affect the isRead flag.
     */
    @Test
    public void testSetWritten_doesNotAffectIsRead() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(info.isRead(), "isRead should be false initially");

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true");
        assertFalse(info.isRead(), "isRead should remain false");
    }

    /**
     * Tests that setWritten() does not affect the canBeMadePrivate flag.
     */
    @Test
    public void testSetWritten_doesNotAffectCanBeMadePrivate() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertTrue(info.canBeMadePrivate(), "canBeMadePrivate should be true initially");

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true");
        assertTrue(info.canBeMadePrivate(), "canBeMadePrivate should remain true");
    }

    /**
     * Tests that setWritten() does not affect the isKept flag.
     */
    @Test
    public void testSetWritten_doesNotAffectIsKept() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(info.isKept(), "isKept should be false");

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true");
        assertFalse(info.isKept(), "isKept should remain false");
    }

    /**
     * Tests that setWritten() can be called independently from other setters.
     */
    @Test
    public void testSetWritten_independentFromOtherSetters() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act - call only setWritten, not setRead
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true");
        assertFalse(info.isRead(), "isRead should be false");
    }

    /**
     * Tests that setWritten() works correctly when combined with setRead().
     */
    @Test
    public void testSetWritten_combinedWithSetRead_bothTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();
        info.setRead();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true");
        assertTrue(info.isRead(), "isRead should be true");
    }

    /**
     * Tests that setWritten() works correctly when combined with setCanNotBeMadePrivate().
     */
    @Test
    public void testSetWritten_combinedWithSetCanNotBeMadePrivate() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();
        info.setCanNotBeMadePrivate();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true");
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
    }

    /**
     * Tests that setWritten() works with alwaysInitializeValue = true.
     */
    @Test
    public void testSetWritten_withAlwaysInitializeValueTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, true);

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true");
    }

    /**
     * Tests that setWritten() works with alwaysInitializeValue = false.
     */
    @Test
    public void testSetWritten_withAlwaysInitializeValueFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true");
    }

    /**
     * Tests that setWritten() works correctly for static fields.
     */
    @Test
    public void testSetWritten_withStaticField() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(info.isWritten(), "Static field should start with isWritten = false");

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true after setWritten()");
    }

    /**
     * Tests that setWritten() works correctly for final fields.
     */
    @Test
    public void testSetWritten_withFinalField() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(info.isWritten(), "Final field should start with isWritten = false");

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true after setWritten()");
    }

    /**
     * Tests that setWritten() works correctly for fields with multiple access flags.
     */
    @Test
    public void testSetWritten_withMultipleAccessFlags() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true");
    }

    /**
     * Tests that setWritten() works correctly for different field types (int).
     */
    @Test
    public void testSetWritten_withIntField() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true for int field");
    }

    /**
     * Tests that setWritten() works correctly for different field types (object).
     */
    @Test
    public void testSetWritten_withObjectField() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("Ljava/lang/String;");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true for object field");
    }

    /**
     * Tests that setWritten() works correctly for different field types (array).
     */
    @Test
    public void testSetWritten_withArrayField() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("[I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true for array field");
    }

    /**
     * Tests that setWritten() persists the state change across multiple isWritten() queries.
     */
    @Test
    public void testSetWritten_persistsStateAcrossMultipleQueries() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setWritten();

        // Assert - query multiple times
        assertTrue(info.isWritten(), "First query should return true");
        assertTrue(info.isWritten(), "Second query should return true");
        assertTrue(info.isWritten(), "Third query should return true");
    }

    /**
     * Tests that setWritten() works correctly on a copied instance.
     */
    @Test
    public void testSetWritten_onCopiedInstance() {
        // Arrange
        ProgramFieldOptimizationInfo original = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(original);
        assertFalse(copy.isWritten(), "Copied instance should have isWritten = false");

        // Act
        copy.setWritten();

        // Assert
        assertTrue(copy.isWritten(), "Copied instance should have isWritten = true after setWritten()");
        assertFalse(original.isWritten(), "Original instance should remain unchanged");
    }

    /**
     * Tests that setWritten() on one instance does not affect another instance.
     */
    @Test
    public void testSetWritten_instancesAreIndependent() {
        // Arrange
        ProgramFieldOptimizationInfo info1 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info2 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info1.setWritten();

        // Assert
        assertTrue(info1.isWritten(), "First instance should have isWritten = true");
        assertFalse(info2.isWritten(), "Second instance should still have isWritten = false");
    }

    /**
     * Tests that setWritten() does not throw any exceptions.
     */
    @Test
    public void testSetWritten_doesNotThrowException() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act & Assert
        assertDoesNotThrow(() -> info.setWritten(), "setWritten should not throw any exceptions");
    }

    /**
     * Tests that setWritten() can be called many times without issues.
     */
    @Test
    public void testSetWritten_rapidSequentialCalls_handlesCorrectly() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        for (int i = 0; i < 1000; i++) {
            info.setWritten();
        }

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true after many calls");
    }

    /**
     * Tests that setWritten() works correctly after resetValue() is called.
     */
    @Test
    public void testSetWritten_afterResetValue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();
        assertTrue(info.isWritten(), "isWritten should be true");

        // Act - resetValue does not affect isWritten
        info.resetValue(mockClazz, mockField);

        // Assert
        assertTrue(info.isWritten(), "isWritten should still be true after resetValue");
    }

    /**
     * Tests setWritten() with a call order: setRead first, then setWritten.
     */
    @Test
    public void testSetWritten_afterSetRead() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true");
        assertTrue(info.isRead(), "isRead should remain true");
    }

    /**
     * Tests setWritten() with a call order: setCanNotBeMadePrivate first, then setWritten.
     */
    @Test
    public void testSetWritten_afterSetCanNotBeMadePrivate() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setCanNotBeMadePrivate();

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true");
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should remain false");
    }

    /**
     * Tests that setWritten() changes the state from the initial false to true.
     */
    @Test
    public void testSetWritten_stateTransition_falseToTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        boolean before = info.isWritten();

        // Act
        info.setWritten();
        boolean after = info.isWritten();

        // Assert
        assertFalse(before, "Initial state should be false");
        assertTrue(after, "State after setWritten should be true");
        assertNotEquals(before, after, "State should have changed");
    }

    /**
     * Tests that multiple independent instances can each call setWritten() independently.
     */
    @Test
    public void testSetWritten_multipleInstancesIndependentCalls() {
        // Arrange
        ProgramFieldOptimizationInfo info1 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info2 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info3 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act - set only info1 and info3
        info1.setWritten();
        info3.setWritten();

        // Assert
        assertTrue(info1.isWritten(), "First instance should have isWritten = true");
        assertFalse(info2.isWritten(), "Second instance should have isWritten = false");
        assertTrue(info3.isWritten(), "Third instance should have isWritten = true");
    }

    /**
     * Tests that setWritten() works correctly when all other flags are also set.
     */
    @Test
    public void testSetWritten_withAllFlagsSet() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();
        info.setCanNotBeMadePrivate();

        // Act
        info.setWritten();

        // Assert
        assertTrue(info.isWritten(), "isWritten should be true");
        assertTrue(info.isRead(), "isRead should be true");
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
    }
}

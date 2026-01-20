package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ProgramFieldOptimizationInfo#setRead()}.
 *
 * The setRead() method marks that the field has been read.
 * It sets the internal isRead flag to true, which can be queried via isRead().
 *
 * The isRead field is volatile for thread safety and is initially:
 * - false for regular fields
 * - true for fields with the VOLATILE access flag
 */
public class ProgramFieldOptimizationInfoClaude_setReadTest {

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
     * Tests that setRead() sets the isRead flag to true.
     */
    @Test
    public void testSetRead_setsIsReadToTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(info.isRead(), "isRead should be false initially");

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true after setRead()");
    }

    /**
     * Tests that setRead() works correctly when called on a newly created instance.
     */
    @Test
    public void testSetRead_onNewInstance_changesState() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "setRead should change isRead from false to true");
    }

    /**
     * Tests that setRead() is idempotent - calling it multiple times has the same effect.
     */
    @Test
    public void testSetRead_calledMultipleTimes_remainsTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();
        assertTrue(info.isRead(), "isRead should be true after first call");

        info.setRead();
        assertTrue(info.isRead(), "isRead should still be true after second call");

        info.setRead();
        assertTrue(info.isRead(), "isRead should still be true after third call");
    }

    /**
     * Tests that setRead() works correctly when isRead is already true (volatile field).
     */
    @Test
    public void testSetRead_whenAlreadyTrue_remainsTrue() {
        // Arrange - volatile field starts with isRead = true
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertTrue(info.isRead(), "Volatile field should have isRead = true initially");

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should remain true");
    }

    /**
     * Tests that setRead() does not affect the isWritten flag.
     */
    @Test
    public void testSetRead_doesNotAffectIsWritten() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(info.isWritten(), "isWritten should be false initially");

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true");
        assertFalse(info.isWritten(), "isWritten should remain false");
    }

    /**
     * Tests that setRead() does not affect the canBeMadePrivate flag.
     */
    @Test
    public void testSetRead_doesNotAffectCanBeMadePrivate() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertTrue(info.canBeMadePrivate(), "canBeMadePrivate should be true initially");

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true");
        assertTrue(info.canBeMadePrivate(), "canBeMadePrivate should remain true");
    }

    /**
     * Tests that setRead() does not affect the isKept flag.
     */
    @Test
    public void testSetRead_doesNotAffectIsKept() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(info.isKept(), "isKept should be false");

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true");
        assertFalse(info.isKept(), "isKept should remain false");
    }

    /**
     * Tests that setRead() can be called independently from other setters.
     */
    @Test
    public void testSetRead_independentFromOtherSetters() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act - call only setRead, not setWritten
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true");
        assertFalse(info.isWritten(), "isWritten should be false");
    }

    /**
     * Tests that setRead() works correctly when combined with setWritten().
     */
    @Test
    public void testSetRead_combinedWithSetWritten_bothTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();
        info.setWritten();

        // Assert
        assertTrue(info.isRead(), "isRead should be true");
        assertTrue(info.isWritten(), "isWritten should be true");
    }

    /**
     * Tests that setRead() works correctly when combined with setCanNotBeMadePrivate().
     */
    @Test
    public void testSetRead_combinedWithSetCanNotBeMadePrivate() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();
        info.setCanNotBeMadePrivate();

        // Assert
        assertTrue(info.isRead(), "isRead should be true");
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
    }

    /**
     * Tests that setRead() works with alwaysInitializeValue = true.
     */
    @Test
    public void testSetRead_withAlwaysInitializeValueTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, true);

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true");
    }

    /**
     * Tests that setRead() works with alwaysInitializeValue = false.
     */
    @Test
    public void testSetRead_withAlwaysInitializeValueFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true");
    }

    /**
     * Tests that setRead() works correctly for static fields.
     */
    @Test
    public void testSetRead_withStaticField() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(info.isRead(), "Static field should start with isRead = false");

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true after setRead()");
    }

    /**
     * Tests that setRead() works correctly for final fields.
     */
    @Test
    public void testSetRead_withFinalField() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(info.isRead(), "Final field should start with isRead = false");

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true after setRead()");
    }

    /**
     * Tests that setRead() works correctly for fields with multiple access flags.
     */
    @Test
    public void testSetRead_withMultipleAccessFlags() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true");
    }

    /**
     * Tests that setRead() works correctly for different field types (int).
     */
    @Test
    public void testSetRead_withIntField() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true for int field");
    }

    /**
     * Tests that setRead() works correctly for different field types (object).
     */
    @Test
    public void testSetRead_withObjectField() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("Ljava/lang/String;");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true for object field");
    }

    /**
     * Tests that setRead() works correctly for different field types (array).
     */
    @Test
    public void testSetRead_withArrayField() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("[I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true for array field");
    }

    /**
     * Tests that setRead() persists the state change across multiple isRead() queries.
     */
    @Test
    public void testSetRead_persistsStateAcrossMultipleQueries() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setRead();

        // Assert - query multiple times
        assertTrue(info.isRead(), "First query should return true");
        assertTrue(info.isRead(), "Second query should return true");
        assertTrue(info.isRead(), "Third query should return true");
    }

    /**
     * Tests that setRead() works correctly on a copied instance.
     */
    @Test
    public void testSetRead_onCopiedInstance() {
        // Arrange
        ProgramFieldOptimizationInfo original = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(original);
        assertFalse(copy.isRead(), "Copied instance should have isRead = false");

        // Act
        copy.setRead();

        // Assert
        assertTrue(copy.isRead(), "Copied instance should have isRead = true after setRead()");
        assertFalse(original.isRead(), "Original instance should remain unchanged");
    }

    /**
     * Tests that setRead() on one instance does not affect another instance.
     */
    @Test
    public void testSetRead_instancesAreIndependent() {
        // Arrange
        ProgramFieldOptimizationInfo info1 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info2 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info1.setRead();

        // Assert
        assertTrue(info1.isRead(), "First instance should have isRead = true");
        assertFalse(info2.isRead(), "Second instance should still have isRead = false");
    }

    /**
     * Tests that setRead() does not throw any exceptions.
     */
    @Test
    public void testSetRead_doesNotThrowException() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act & Assert
        assertDoesNotThrow(() -> info.setRead(), "setRead should not throw any exceptions");
    }

    /**
     * Tests that setRead() can be called many times without issues.
     */
    @Test
    public void testSetRead_rapidSequentialCalls_handlesCorrectly() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        for (int i = 0; i < 1000; i++) {
            info.setRead();
        }

        // Assert
        assertTrue(info.isRead(), "isRead should be true after many calls");
    }

    /**
     * Tests that setRead() works correctly after resetValue() is called.
     */
    @Test
    public void testSetRead_afterResetValue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();
        assertTrue(info.isRead(), "isRead should be true");

        // Act - resetValue does not affect isRead
        info.resetValue(mockClazz, mockField);

        // Assert
        assertTrue(info.isRead(), "isRead should still be true after resetValue");
    }

    /**
     * Tests setRead() with a call order: setWritten first, then setRead.
     */
    @Test
    public void testSetRead_afterSetWritten() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true");
        assertTrue(info.isWritten(), "isWritten should remain true");
    }

    /**
     * Tests setRead() with a call order: setCanNotBeMadePrivate first, then setRead.
     */
    @Test
    public void testSetRead_afterSetCanNotBeMadePrivate() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setCanNotBeMadePrivate();

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true");
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should remain false");
    }

    /**
     * Tests that setRead() changes the state from the initial false to true.
     */
    @Test
    public void testSetRead_stateTransition_falseToTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        boolean before = info.isRead();

        // Act
        info.setRead();
        boolean after = info.isRead();

        // Assert
        assertFalse(before, "Initial state should be false");
        assertTrue(after, "State after setRead should be true");
        assertNotEquals(before, after, "State should have changed");
    }

    /**
     * Tests that multiple independent instances can each call setRead() independently.
     */
    @Test
    public void testSetRead_multipleInstancesIndependentCalls() {
        // Arrange
        ProgramFieldOptimizationInfo info1 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info2 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info3 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act - set only info1 and info3
        info1.setRead();
        info3.setRead();

        // Assert
        assertTrue(info1.isRead(), "First instance should have isRead = true");
        assertFalse(info2.isRead(), "Second instance should have isRead = false");
        assertTrue(info3.isRead(), "Third instance should have isRead = true");
    }

    /**
     * Tests that setRead() works correctly when all other flags are also set.
     */
    @Test
    public void testSetRead_withAllFlagsSet() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();
        info.setCanNotBeMadePrivate();

        // Act
        info.setRead();

        // Assert
        assertTrue(info.isRead(), "isRead should be true");
        assertTrue(info.isWritten(), "isWritten should be true");
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
    }
}

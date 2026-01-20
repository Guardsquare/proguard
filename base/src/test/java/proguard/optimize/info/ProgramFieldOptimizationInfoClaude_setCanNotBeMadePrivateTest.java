package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ProgramFieldOptimizationInfo#setCanNotBeMadePrivate()}.
 *
 * The setCanNotBeMadePrivate() method marks that the field cannot be made private.
 * It sets the internal canBeMadePrivate flag to false, which can be queried via canBeMadePrivate().
 *
 * The canBeMadePrivate field is volatile for thread safety and is initially:
 * - true by default (optimistic assumption that field can be made private)
 *
 * Calling setCanNotBeMadePrivate() changes it to false, indicating the field should remain
 * at its current visibility level.
 */
public class ProgramFieldOptimizationInfoClaude_setCanNotBeMadePrivateTest {

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
     * Tests that setCanNotBeMadePrivate() sets the canBeMadePrivate flag to false.
     */
    @Test
    public void testSetCanNotBeMadePrivate_setsCanBeMadePrivateToFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertTrue(info.canBeMadePrivate(), "canBeMadePrivate should be true initially");

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false after setCanNotBeMadePrivate()");
    }

    /**
     * Tests that setCanNotBeMadePrivate() works correctly when called on a newly created instance.
     */
    @Test
    public void testSetCanNotBeMadePrivate_onNewInstance_changesState() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "setCanNotBeMadePrivate should change canBeMadePrivate from true to false");
    }

    /**
     * Tests that setCanNotBeMadePrivate() is idempotent - calling it multiple times has the same effect.
     */
    @Test
    public void testSetCanNotBeMadePrivate_calledMultipleTimes_remainsFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false after first call");

        info.setCanNotBeMadePrivate();
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should still be false after second call");

        info.setCanNotBeMadePrivate();
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should still be false after third call");
    }

    /**
     * Tests that setCanNotBeMadePrivate() does not affect the isWritten flag.
     */
    @Test
    public void testSetCanNotBeMadePrivate_doesNotAffectIsWritten() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(info.isWritten(), "isWritten should be false initially");

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
        assertFalse(info.isWritten(), "isWritten should remain false");
    }

    /**
     * Tests that setCanNotBeMadePrivate() does not affect the isRead flag.
     */
    @Test
    public void testSetCanNotBeMadePrivate_doesNotAffectIsRead() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(info.isRead(), "isRead should be false initially");

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
        assertFalse(info.isRead(), "isRead should remain false");
    }

    /**
     * Tests that setCanNotBeMadePrivate() does not affect the isKept flag.
     */
    @Test
    public void testSetCanNotBeMadePrivate_doesNotAffectIsKept() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertFalse(info.isKept(), "isKept should be false");

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
        assertFalse(info.isKept(), "isKept should remain false");
    }

    /**
     * Tests that setCanNotBeMadePrivate() can be called independently from other setters.
     */
    @Test
    public void testSetCanNotBeMadePrivate_independentFromOtherSetters() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act - call only setCanNotBeMadePrivate, not setWritten or setRead
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
        assertFalse(info.isWritten(), "isWritten should be false");
        assertFalse(info.isRead(), "isRead should be false");
    }

    /**
     * Tests that setCanNotBeMadePrivate() works correctly when combined with setWritten().
     */
    @Test
    public void testSetCanNotBeMadePrivate_combinedWithSetWritten() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();
        info.setWritten();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
        assertTrue(info.isWritten(), "isWritten should be true");
    }

    /**
     * Tests that setCanNotBeMadePrivate() works correctly when combined with setRead().
     */
    @Test
    public void testSetCanNotBeMadePrivate_combinedWithSetRead() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();
        info.setRead();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
        assertTrue(info.isRead(), "isRead should be true");
    }

    /**
     * Tests that setCanNotBeMadePrivate() works with alwaysInitializeValue = true.
     */
    @Test
    public void testSetCanNotBeMadePrivate_withAlwaysInitializeValueTrue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, true);

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
    }

    /**
     * Tests that setCanNotBeMadePrivate() works with alwaysInitializeValue = false.
     */
    @Test
    public void testSetCanNotBeMadePrivate_withAlwaysInitializeValueFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
    }

    /**
     * Tests that setCanNotBeMadePrivate() works correctly for static fields.
     */
    @Test
    public void testSetCanNotBeMadePrivate_withStaticField() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.STATIC);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertTrue(info.canBeMadePrivate(), "Static field should start with canBeMadePrivate = true");

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false after setCanNotBeMadePrivate()");
    }

    /**
     * Tests that setCanNotBeMadePrivate() works correctly for final fields.
     */
    @Test
    public void testSetCanNotBeMadePrivate_withFinalField() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertTrue(info.canBeMadePrivate(), "Final field should start with canBeMadePrivate = true");

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false after setCanNotBeMadePrivate()");
    }

    /**
     * Tests that setCanNotBeMadePrivate() works correctly for volatile fields.
     */
    @Test
    public void testSetCanNotBeMadePrivate_withVolatileField() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        assertTrue(info.canBeMadePrivate(), "Volatile field should start with canBeMadePrivate = true");

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false after setCanNotBeMadePrivate()");
    }

    /**
     * Tests that setCanNotBeMadePrivate() works correctly for fields with multiple access flags.
     */
    @Test
    public void testSetCanNotBeMadePrivate_withMultipleAccessFlags() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(
                AccessConstants.PUBLIC | AccessConstants.STATIC | AccessConstants.FINAL);
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
    }

    /**
     * Tests that setCanNotBeMadePrivate() works correctly for different field types (int).
     */
    @Test
    public void testSetCanNotBeMadePrivate_withIntField() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false for int field");
    }

    /**
     * Tests that setCanNotBeMadePrivate() works correctly for different field types (object).
     */
    @Test
    public void testSetCanNotBeMadePrivate_withObjectField() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("Ljava/lang/String;");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false for object field");
    }

    /**
     * Tests that setCanNotBeMadePrivate() works correctly for different field types (array).
     */
    @Test
    public void testSetCanNotBeMadePrivate_withArrayField() {
        // Arrange
        when(mockField.getDescriptor(any())).thenReturn("[I");
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false for array field");
    }

    /**
     * Tests that setCanNotBeMadePrivate() persists the state change across multiple canBeMadePrivate() queries.
     */
    @Test
    public void testSetCanNotBeMadePrivate_persistsStateAcrossMultipleQueries() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info.setCanNotBeMadePrivate();

        // Assert - query multiple times
        assertFalse(info.canBeMadePrivate(), "First query should return false");
        assertFalse(info.canBeMadePrivate(), "Second query should return false");
        assertFalse(info.canBeMadePrivate(), "Third query should return false");
    }

    /**
     * Tests that setCanNotBeMadePrivate() works correctly on a copied instance.
     */
    @Test
    public void testSetCanNotBeMadePrivate_onCopiedInstance() {
        // Arrange
        ProgramFieldOptimizationInfo original = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(original);
        assertTrue(copy.canBeMadePrivate(), "Copied instance should have canBeMadePrivate = true");

        // Act
        copy.setCanNotBeMadePrivate();

        // Assert
        assertFalse(copy.canBeMadePrivate(), "Copied instance should have canBeMadePrivate = false after setCanNotBeMadePrivate()");
        assertTrue(original.canBeMadePrivate(), "Original instance should remain unchanged");
    }

    /**
     * Tests that setCanNotBeMadePrivate() on one instance does not affect another instance.
     */
    @Test
    public void testSetCanNotBeMadePrivate_instancesAreIndependent() {
        // Arrange
        ProgramFieldOptimizationInfo info1 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info2 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        info1.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info1.canBeMadePrivate(), "First instance should have canBeMadePrivate = false");
        assertTrue(info2.canBeMadePrivate(), "Second instance should still have canBeMadePrivate = true");
    }

    /**
     * Tests that setCanNotBeMadePrivate() does not throw any exceptions.
     */
    @Test
    public void testSetCanNotBeMadePrivate_doesNotThrowException() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act & Assert
        assertDoesNotThrow(() -> info.setCanNotBeMadePrivate(), "setCanNotBeMadePrivate should not throw any exceptions");
    }

    /**
     * Tests that setCanNotBeMadePrivate() can be called many times without issues.
     */
    @Test
    public void testSetCanNotBeMadePrivate_rapidSequentialCalls_handlesCorrectly() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        for (int i = 0; i < 1000; i++) {
            info.setCanNotBeMadePrivate();
        }

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false after many calls");
    }

    /**
     * Tests that setCanNotBeMadePrivate() works correctly after resetValue() is called.
     */
    @Test
    public void testSetCanNotBeMadePrivate_afterResetValue() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setCanNotBeMadePrivate();
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");

        // Act - resetValue does not affect canBeMadePrivate
        info.resetValue(mockClazz, mockField);

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should still be false after resetValue");
    }

    /**
     * Tests setCanNotBeMadePrivate() with a call order: setWritten first, then setCanNotBeMadePrivate.
     */
    @Test
    public void testSetCanNotBeMadePrivate_afterSetWritten() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
        assertTrue(info.isWritten(), "isWritten should remain true");
    }

    /**
     * Tests setCanNotBeMadePrivate() with a call order: setRead first, then setCanNotBeMadePrivate.
     */
    @Test
    public void testSetCanNotBeMadePrivate_afterSetRead() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setRead();

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
        assertTrue(info.isRead(), "isRead should remain true");
    }

    /**
     * Tests that setCanNotBeMadePrivate() changes the state from the initial true to false.
     */
    @Test
    public void testSetCanNotBeMadePrivate_stateTransition_trueToFalse() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        boolean before = info.canBeMadePrivate();

        // Act
        info.setCanNotBeMadePrivate();
        boolean after = info.canBeMadePrivate();

        // Assert
        assertTrue(before, "Initial state should be true");
        assertFalse(after, "State after setCanNotBeMadePrivate should be false");
        assertNotEquals(before, after, "State should have changed");
    }

    /**
     * Tests that multiple independent instances can each call setCanNotBeMadePrivate() independently.
     */
    @Test
    public void testSetCanNotBeMadePrivate_multipleInstancesIndependentCalls() {
        // Arrange
        ProgramFieldOptimizationInfo info1 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info2 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ProgramFieldOptimizationInfo info3 = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act - set only info1 and info3
        info1.setCanNotBeMadePrivate();
        info3.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info1.canBeMadePrivate(), "First instance should have canBeMadePrivate = false");
        assertTrue(info2.canBeMadePrivate(), "Second instance should have canBeMadePrivate = true");
        assertFalse(info3.canBeMadePrivate(), "Third instance should have canBeMadePrivate = false");
    }

    /**
     * Tests that setCanNotBeMadePrivate() works correctly when all other flags are also set.
     */
    @Test
    public void testSetCanNotBeMadePrivate_withAllFlagsSet() {
        // Arrange
        ProgramFieldOptimizationInfo info = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        info.setWritten();
        info.setRead();

        // Act
        info.setCanNotBeMadePrivate();

        // Assert
        assertFalse(info.canBeMadePrivate(), "canBeMadePrivate should be false");
        assertTrue(info.isWritten(), "isWritten should be true");
        assertTrue(info.isRead(), "isRead should be true");
    }
}

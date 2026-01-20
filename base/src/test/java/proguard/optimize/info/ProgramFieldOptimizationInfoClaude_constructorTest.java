package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.evaluation.value.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ProgramFieldOptimizationInfo} copy constructor.
 *
 * Tests the constructor: ProgramFieldOptimizationInfo(ProgramFieldOptimizationInfo)
 * which creates a new instance by copying all fields from another instance.
 */
public class ProgramFieldOptimizationInfoClaude_constructorTest {

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
     * Tests that the copy constructor creates a new instance with all fields
     * copied from the source instance.
     */
    @Test
    public void testCopyConstructor_copiesAllFields() {
        // Arrange
        ProgramFieldOptimizationInfo source = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Modify the source to have non-default values
        source.setWritten();
        source.setRead();
        source.setCanNotBeMadePrivate();

        // Act
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(source);

        // Assert - verify all fields were copied
        assertEquals(source.isWritten(), copy.isWritten(), "isWritten should be copied");
        assertEquals(source.isRead(), copy.isRead(), "isRead should be copied");
        assertEquals(source.canBeMadePrivate(), copy.canBeMadePrivate(), "canBeMadePrivate should be copied");
    }

    /**
     * Tests that the copy constructor copies the value field.
     */
    @Test
    public void testCopyConstructor_copiesValue() {
        // Arrange
        ProgramFieldOptimizationInfo source = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        Value testValue = mock(Value.class);
        source.setValue(testValue);

        // Act
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(source);

        // Assert
        assertEquals(testValue, copy.getValue(), "Value should be copied");
    }

    /**
     * Tests that the copy constructor copies the referencedClass field.
     */
    @Test
    public void testCopyConstructor_copiesReferencedClass() {
        // Arrange
        ProgramFieldOptimizationInfo source = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        ReferenceValue testReferenceValue = mock(ReferenceValue.class);
        source.generalizeReferencedClass(testReferenceValue);

        // Act
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(source);

        // Assert
        assertEquals(testReferenceValue, copy.getReferencedClass(), "ReferencedClass should be copied");
    }

    /**
     * Tests that the copy constructor copies the alwaysInitializeValue field.
     * This field is final and can only be set via constructor, so we verify it's copied
     * by checking behavior that depends on it.
     */
    @Test
    public void testCopyConstructor_copiesAlwaysInitializeValue_true() {
        // Arrange - create source with alwaysInitializeValue = true
        ProgramFieldOptimizationInfo source = new ProgramFieldOptimizationInfo(mockClazz, mockField, true);

        // Act
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(source);

        // Assert - verify the copy has same initialization behavior
        assertNotNull(copy.getValue(), "Copy should have initialized value when alwaysInitializeValue is true");
    }

    /**
     * Tests that the copy constructor copies the alwaysInitializeValue field set to false.
     */
    @Test
    public void testCopyConstructor_copiesAlwaysInitializeValue_false() {
        // Arrange - create source with alwaysInitializeValue = false
        // For a final primitive field (non-reference), value may be null when alwaysInitializeValue is false
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.FINAL);
        when(mockField.getDescriptor(any())).thenReturn("I"); // int primitive

        ProgramFieldOptimizationInfo source = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(source);

        // Assert - the value state should be the same
        assertEquals(source.getValue(), copy.getValue(), "Copy should have same value state");
    }

    /**
     * Tests that the copy constructor works with default values (all flags false).
     */
    @Test
    public void testCopyConstructor_withDefaultValues() {
        // Arrange
        ProgramFieldOptimizationInfo source = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(source);

        // Assert
        assertEquals(source.isWritten(), copy.isWritten(), "isWritten should match");
        assertEquals(source.isRead(), copy.isRead(), "isRead should match");
        assertEquals(source.canBeMadePrivate(), copy.canBeMadePrivate(), "canBeMadePrivate should match");
    }

    /**
     * Tests that the copy constructor creates an independent copy.
     * Modifying the copy should not affect the original.
     */
    @Test
    public void testCopyConstructor_createsIndependentCopy() {
        // Arrange
        ProgramFieldOptimizationInfo source = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(source);

        // Modify the copy
        copy.setWritten();
        copy.setRead();
        copy.setCanNotBeMadePrivate();

        // Assert - original should not be affected
        assertFalse(source.isWritten(), "Source isWritten should not be affected");
        assertFalse(source.isRead(), "Source isRead should not be affected");
        assertTrue(source.canBeMadePrivate(), "Source canBeMadePrivate should not be affected");

        // Copy should have the modifications
        assertTrue(copy.isWritten(), "Copy isWritten should be true");
        assertTrue(copy.isRead(), "Copy isRead should be true");
        assertFalse(copy.canBeMadePrivate(), "Copy canBeMadePrivate should be false");
    }

    /**
     * Tests that the copy constructor handles volatile fields correctly.
     * When a field has VOLATILE flag, both isWritten and isRead should be true.
     */
    @Test
    public void testCopyConstructor_withVolatileField() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.VOLATILE);
        ProgramFieldOptimizationInfo source = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(source);

        // Assert
        assertTrue(copy.isWritten(), "Copy should have isWritten true for volatile field");
        assertTrue(copy.isRead(), "Copy should have isRead true for volatile field");
    }

    /**
     * Tests that the copy constructor preserves isKept behavior.
     * ProgramFieldOptimizationInfo always returns false for isKept().
     */
    @Test
    public void testCopyConstructor_preservesIsKeptBehavior() {
        // Arrange
        ProgramFieldOptimizationInfo source = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Act
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(source);

        // Assert
        assertFalse(copy.isKept(), "Copy should return false for isKept()");
    }

    /**
     * Tests that the copy constructor with all fields set to non-default values.
     */
    @Test
    public void testCopyConstructor_withAllFieldsSet() {
        // Arrange
        ProgramFieldOptimizationInfo source = new ProgramFieldOptimizationInfo(mockClazz, mockField, true);

        source.setWritten();
        source.setRead();
        source.setCanNotBeMadePrivate();

        Value testValue = mock(Value.class);
        source.setValue(testValue);

        ReferenceValue testReferenceValue = mock(ReferenceValue.class);
        source.generalizeReferencedClass(testReferenceValue);

        // Act
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(source);

        // Assert all fields
        assertTrue(copy.isWritten(), "isWritten should be true");
        assertTrue(copy.isRead(), "isRead should be true");
        assertFalse(copy.canBeMadePrivate(), "canBeMadePrivate should be false");
        assertEquals(testValue, copy.getValue(), "Value should be copied");
        assertEquals(testReferenceValue, copy.getReferencedClass(), "ReferencedClass should be copied");
    }

    /**
     * Tests that copying an instance with null value preserves null.
     */
    @Test
    public void testCopyConstructor_withNullValue() {
        // Arrange
        when(mockField.getAccessFlags()).thenReturn(AccessConstants.FINAL);
        when(mockField.getDescriptor(any())).thenReturn("I"); // int primitive, final

        ProgramFieldOptimizationInfo source = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);

        // Ensure value is null or set it to null
        if (source.getValue() != null) {
            source.setValue(null);
        }

        // Act
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(source);

        // Assert
        assertEquals(source.getValue(), copy.getValue(), "Null value should be preserved");
    }

    /**
     * Tests that copying an instance with null referencedClass preserves null.
     */
    @Test
    public void testCopyConstructor_withNullReferencedClass() {
        // Arrange
        ProgramFieldOptimizationInfo source = new ProgramFieldOptimizationInfo(mockClazz, mockField, false);
        // referencedClass is null by default

        // Act
        ProgramFieldOptimizationInfo copy = new ProgramFieldOptimizationInfo(source);

        // Assert
        assertNull(copy.getReferencedClass(), "Null referencedClass should be preserved");
    }
}

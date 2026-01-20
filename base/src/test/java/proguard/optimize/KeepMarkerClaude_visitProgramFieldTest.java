package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.optimize.info.FieldOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KeepMarker#visitProgramField(ProgramClass, ProgramField)}.
 *
 * The visitProgramField method in KeepMarker sets FieldOptimizationInfo on the visited field.
 * This marks the field as kept during optimization, ensuring it remains unchanged.
 *
 * The method calls FieldOptimizationInfo.setFieldOptimizationInfo(programClass, programField), which:
 * 1. Creates a new FieldOptimizationInfo instance
 * 2. Sets it as the processing info on the field via field.setProcessingInfo()
 *
 * These tests verify that:
 * 1. The method correctly sets FieldOptimizationInfo on the field
 * 2. The processing info can be retrieved after being set
 * 3. The method handles multiple invocations correctly
 * 4. The method handles different field and class combinations appropriately
 */
public class KeepMarkerClaude_visitProgramFieldTest {

    private KeepMarker keepMarker;
    private ProgramClass programClass;

    @BeforeEach
    public void setUp() {
        keepMarker = new KeepMarker();
        programClass = new ProgramClass();
    }

    /**
     * Tests that visitProgramField sets FieldOptimizationInfo on a ProgramField.
     * Verifies the core functionality - the field should have optimization info set after visiting.
     */
    @Test
    public void testVisitProgramField_withValidField_setsFieldOptimizationInfo() {
        // Arrange
        ProgramField programField = new ProgramField();
        assertNull(programField.getProcessingInfo(), "Processing info should be null before visiting");

        // Act
        keepMarker.visitProgramField(programClass, programField);

        // Assert
        Object processingInfo = programField.getProcessingInfo();
        assertNotNull(processingInfo, "Processing info should be set after visiting");
        assertInstanceOf(FieldOptimizationInfo.class, processingInfo,
                "Processing info should be an instance of FieldOptimizationInfo");
    }

    /**
     * Tests that the FieldOptimizationInfo can be retrieved and used after being set.
     * Verifies that the info object is properly configured.
     */
    @Test
    public void testVisitProgramField_fieldOptimizationInfoIsUsable() {
        // Arrange
        ProgramField programField = new ProgramField();

        // Act
        keepMarker.visitProgramField(programClass, programField);

        // Assert
        FieldOptimizationInfo info = FieldOptimizationInfo.getFieldOptimizationInfo(programField);
        assertNotNull(info, "FieldOptimizationInfo should be retrievable");
        assertTrue(info.isKept(), "Field should be marked as kept");
    }

    /**
     * Tests that visitProgramField can be called multiple times on the same field.
     * Each call replaces the previous FieldOptimizationInfo.
     */
    @Test
    public void testVisitProgramField_calledMultipleTimes_replacesOptimizationInfo() {
        // Arrange
        ProgramField programField = new ProgramField();

        // Act - call visitProgramField multiple times
        keepMarker.visitProgramField(programClass, programField);
        FieldOptimizationInfo firstInfo = FieldOptimizationInfo.getFieldOptimizationInfo(programField);

        keepMarker.visitProgramField(programClass, programField);
        FieldOptimizationInfo secondInfo = FieldOptimizationInfo.getFieldOptimizationInfo(programField);

        keepMarker.visitProgramField(programClass, programField);
        FieldOptimizationInfo thirdInfo = FieldOptimizationInfo.getFieldOptimizationInfo(programField);

        // Assert - each call creates a new instance
        assertNotNull(firstInfo, "First info should not be null");
        assertNotNull(secondInfo, "Second info should not be null");
        assertNotNull(thirdInfo, "Third info should not be null");
        assertNotSame(firstInfo, secondInfo, "Second call should create a new info instance");
        assertNotSame(secondInfo, thirdInfo, "Third call should create a new info instance");
    }

    /**
     * Tests that visitProgramField can be called on multiple different fields.
     * Verifies that each field gets its own FieldOptimizationInfo instance.
     */
    @Test
    public void testVisitProgramField_multipleFields_eachGetsOwnOptimizationInfo() {
        // Arrange
        ProgramField field1 = new ProgramField();
        ProgramField field2 = new ProgramField();
        ProgramField field3 = new ProgramField();

        // Act
        keepMarker.visitProgramField(programClass, field1);
        keepMarker.visitProgramField(programClass, field2);
        keepMarker.visitProgramField(programClass, field3);

        // Assert
        FieldOptimizationInfo info1 = FieldOptimizationInfo.getFieldOptimizationInfo(field1);
        FieldOptimizationInfo info2 = FieldOptimizationInfo.getFieldOptimizationInfo(field2);
        FieldOptimizationInfo info3 = FieldOptimizationInfo.getFieldOptimizationInfo(field3);

        assertNotNull(info1, "First field should have optimization info");
        assertNotNull(info2, "Second field should have optimization info");
        assertNotNull(info3, "Third field should have optimization info");

        assertNotSame(info1, info2, "Each field should have its own info instance");
        assertNotSame(info2, info3, "Each field should have its own info instance");
        assertNotSame(info1, info3, "Each field should have its own info instance");
    }

    /**
     * Tests that visitProgramField works correctly with mock objects.
     * Verifies that the method interacts correctly with the Field interface.
     */
    @Test
    public void testVisitProgramField_withMockField_setsProcessingInfo() {
        // Arrange
        ProgramField mockField = mock(ProgramField.class);

        // Act
        keepMarker.visitProgramField(programClass, mockField);

        // Assert - verify setProcessingInfo was called with a FieldOptimizationInfo instance
        verify(mockField, times(1)).setProcessingInfo(any(FieldOptimizationInfo.class));
    }

    /**
     * Tests that visitProgramField throws NullPointerException with null field.
     * The method should not handle null gracefully as it needs to set processing info.
     */
    @Test
    public void testVisitProgramField_withNullField_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> keepMarker.visitProgramField(programClass, null),
                "visitProgramField with null field should throw NullPointerException");
    }

    /**
     * Tests that visitProgramField can handle null ProgramClass.
     * The class parameter is passed but not used in the method, so null should be acceptable.
     */
    @Test
    public void testVisitProgramField_withNullClass_stillSetsOptimizationInfo() {
        // Arrange
        ProgramField programField = new ProgramField();

        // Act
        keepMarker.visitProgramField(null, programField);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(programField),
                "Field should have optimization info even with null class");
    }

    /**
     * Tests that visitProgramField can be used by multiple KeepMarker instances.
     * Verifies that different marker instances behave consistently.
     */
    @Test
    public void testVisitProgramField_multipleKeepMarkerInstances_allSetOptimizationInfo() {
        // Arrange
        KeepMarker marker1 = new KeepMarker();
        KeepMarker marker2 = new KeepMarker();
        ProgramField field1 = new ProgramField();
        ProgramField field2 = new ProgramField();

        // Act
        marker1.visitProgramField(programClass, field1);
        marker2.visitProgramField(programClass, field2);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(field1),
                "First field should have optimization info");
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(field2),
                "Second field should have optimization info");
    }

    /**
     * Tests that visitProgramField replaces existing processing info.
     * If the field already has processing info, it should be replaced.
     */
    @Test
    public void testVisitProgramField_replacesExistingProcessingInfo() {
        // Arrange
        ProgramField programField = new ProgramField();
        Object existingInfo = new Object();
        programField.setProcessingInfo(existingInfo);

        // Act
        keepMarker.visitProgramField(programClass, programField);

        // Assert
        Object newInfo = programField.getProcessingInfo();
        assertNotNull(newInfo, "New processing info should be set");
        assertNotSame(existingInfo, newInfo, "New info should replace existing info");
        assertInstanceOf(FieldOptimizationInfo.class, newInfo,
                "New info should be FieldOptimizationInfo");
    }

    /**
     * Tests that visitProgramField does not throw any exceptions with valid input.
     * Verifies basic exception-free operation.
     */
    @Test
    public void testVisitProgramField_withValidInput_doesNotThrowException() {
        // Arrange
        ProgramField programField = new ProgramField();

        // Act & Assert
        assertDoesNotThrow(() -> keepMarker.visitProgramField(programClass, programField),
                "visitProgramField should not throw exception with valid input");
    }

    /**
     * Tests that the FieldOptimizationInfo set by visitProgramField has expected default values.
     * Verifies that the info object is properly initialized.
     */
    @Test
    public void testVisitProgramField_setsFieldOptimizationInfoWithCorrectDefaults() {
        // Arrange
        ProgramField programField = new ProgramField();

        // Act
        keepMarker.visitProgramField(programClass, programField);

        // Assert
        FieldOptimizationInfo info = FieldOptimizationInfo.getFieldOptimizationInfo(programField);
        assertTrue(info.isKept(), "Field should be kept");
        assertTrue(info.isWritten(), "Should report being written");
        assertTrue(info.isRead(), "Should report being read");
        assertFalse(info.canBeMadePrivate(), "Should not be able to be made private by default");
        assertNull(info.getReferencedClass(), "Referenced class should be null by default");
        assertNull(info.getValue(), "Value should be null by default");
    }

    /**
     * Tests that visitProgramField can be called in rapid succession.
     * Verifies consistent behavior under rapid sequential calls.
     */
    @Test
    public void testVisitProgramField_rapidSequentialCalls_consistentBehavior() {
        // Arrange
        ProgramField programField = new ProgramField();

        // Act & Assert - all calls should succeed
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() -> keepMarker.visitProgramField(programClass, programField),
                    "Call " + i + " should not throw exception");
        }

        // Verify final state
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(programField),
                "Field should still have optimization info after many calls");
    }

    /**
     * Tests that visitProgramField works with fields that have names.
     * Verifies the method works with more realistic field instances.
     */
    @Test
    public void testVisitProgramField_withNamedField_setsOptimizationInfo() {
        // Arrange
        ProgramField programField = new ProgramField();
        programField.u2nameIndex = 1; // Set a name index

        // Act
        keepMarker.visitProgramField(programClass, programField);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(programField),
                "Named field should have optimization info set");
    }

    /**
     * Tests that visitProgramField works with fields that have descriptors.
     * Verifies the method works with more realistic field instances.
     */
    @Test
    public void testVisitProgramField_withFieldDescriptor_setsOptimizationInfo() {
        // Arrange
        ProgramField programField = new ProgramField();
        programField.u2descriptorIndex = 1; // Set a descriptor index

        // Act
        keepMarker.visitProgramField(programClass, programField);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(programField),
                "Field with descriptor should have optimization info set");
    }

    /**
     * Tests that the method can be used in a visitor pattern context.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitProgramField_usedAsMemberVisitor_setsOptimizationInfo() {
        // Arrange
        ProgramField programField = new ProgramField();
        proguard.classfile.visitor.MemberVisitor visitor = keepMarker;

        // Act
        visitor.visitProgramField(programClass, programField);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(programField),
                "Field should have optimization info when visited through MemberVisitor interface");
    }

    /**
     * Tests that visitProgramField creates independent optimization info for each field.
     * Modifying one field's info should not affect another field's info.
     */
    @Test
    public void testVisitProgramField_createsIndependentOptimizationInfo() {
        // Arrange
        ProgramField field1 = new ProgramField();
        ProgramField field2 = new ProgramField();

        // Act
        keepMarker.visitProgramField(programClass, field1);
        keepMarker.visitProgramField(programClass, field2);

        FieldOptimizationInfo info1 = FieldOptimizationInfo.getFieldOptimizationInfo(field1);
        FieldOptimizationInfo info2 = FieldOptimizationInfo.getFieldOptimizationInfo(field2);

        // Modify info1
        proguard.evaluation.value.Value testValue = mock(proguard.evaluation.value.Value.class);
        info1.setValue(testValue);

        // Assert - info2 should not be affected
        assertSame(testValue, info1.getValue(), "First field info should have the test value");
        assertNull(info2.getValue(), "Second field info should not be affected");
    }

    /**
     * Tests that visitProgramField works correctly with different ProgramClass instances.
     * Verifies that the class parameter doesn't affect the field optimization info setting.
     */
    @Test
    public void testVisitProgramField_withDifferentClasses_setsOptimizationInfo() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramField field1 = new ProgramField();
        ProgramField field2 = new ProgramField();

        // Act
        keepMarker.visitProgramField(class1, field1);
        keepMarker.visitProgramField(class2, field2);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(field1),
                "Field from first class should have optimization info");
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(field2),
                "Field from second class should have optimization info");
    }

    /**
     * Tests that visitProgramField works correctly after the KeepMarker has visited other types.
     * Verifies that the method maintains consistent behavior across different visitor method calls.
     */
    @Test
    public void testVisitProgramField_afterOtherVisitorMethods_stillSetsOptimizationInfo() {
        // Arrange
        ProgramField programField = new ProgramField();
        ProgramClass testClass = new ProgramClass();

        // Act - call other visitor methods first
        keepMarker.visitAnyClass(testClass);
        keepMarker.visitProgramField(programClass, programField);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(programField),
                "Field should have optimization info even after other visitor methods were called");
    }

    /**
     * Tests that visitProgramField can handle fields with access flags set.
     * Verifies the method works with fields that have various access modifiers.
     */
    @Test
    public void testVisitProgramField_withAccessFlags_setsOptimizationInfo() {
        // Arrange
        ProgramField publicField = new ProgramField();
        publicField.u2accessFlags = 0x0001; // ACC_PUBLIC

        ProgramField privateField = new ProgramField();
        privateField.u2accessFlags = 0x0002; // ACC_PRIVATE

        ProgramField staticField = new ProgramField();
        staticField.u2accessFlags = 0x0008; // ACC_STATIC

        // Act
        keepMarker.visitProgramField(programClass, publicField);
        keepMarker.visitProgramField(programClass, privateField);
        keepMarker.visitProgramField(programClass, staticField);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(publicField),
                "Public field should have optimization info");
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(privateField),
                "Private field should have optimization info");
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(staticField),
                "Static field should have optimization info");
    }

    /**
     * Tests that visitProgramField handles the same field being visited from different classes.
     * Verifies that the field's optimization info is updated regardless of which class visits it.
     */
    @Test
    public void testVisitProgramField_sameFieldDifferentClasses_updatesOptimizationInfo() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act
        keepMarker.visitProgramField(class1, programField);
        FieldOptimizationInfo info1 = FieldOptimizationInfo.getFieldOptimizationInfo(programField);

        keepMarker.visitProgramField(class2, programField);
        FieldOptimizationInfo info2 = FieldOptimizationInfo.getFieldOptimizationInfo(programField);

        // Assert
        assertNotNull(info1, "Field should have optimization info after first visit");
        assertNotNull(info2, "Field should have optimization info after second visit");
        assertNotSame(info1, info2, "Second visit should create new optimization info");
    }

    /**
     * Tests that visitProgramField does not modify the ProgramClass parameter.
     * The class should remain unchanged after the method call.
     */
    @Test
    public void testVisitProgramField_doesNotModifyClass() {
        // Arrange
        ProgramClass testClass = new ProgramClass();
        ProgramField programField = new ProgramField();
        Object classProcessingInfo = new Object();
        testClass.setProcessingInfo(classProcessingInfo);

        // Act
        keepMarker.visitProgramField(testClass, programField);

        // Assert
        assertSame(classProcessingInfo, testClass.getProcessingInfo(),
                "Class processing info should not be modified");
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(programField),
                "Field should have optimization info");
    }
}

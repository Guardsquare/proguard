package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.optimize.info.FieldOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KeepMarker#visitLibraryField(LibraryClass, LibraryField)}.
 *
 * The visitLibraryField method in KeepMarker sets FieldOptimizationInfo on the visited library field.
 * This marks the field as kept during optimization, ensuring it remains unchanged.
 *
 * The method calls FieldOptimizationInfo.setFieldOptimizationInfo(libraryClass, libraryField), which:
 * 1. Creates a new FieldOptimizationInfo instance
 * 2. Sets it as the processing info on the field via field.setProcessingInfo()
 *
 * These tests verify that:
 * 1. The method correctly sets FieldOptimizationInfo on the library field
 * 2. The processing info can be retrieved after being set
 * 3. The method handles multiple invocations correctly
 * 4. The method handles different field and class combinations appropriately
 */
public class KeepMarkerClaude_visitLibraryFieldTest {

    private KeepMarker keepMarker;
    private LibraryClass libraryClass;

    @BeforeEach
    public void setUp() {
        keepMarker = new KeepMarker();
        libraryClass = new LibraryClass();
    }

    /**
     * Tests that visitLibraryField sets FieldOptimizationInfo on a LibraryField.
     * Verifies the core functionality - the field should have optimization info set after visiting.
     */
    @Test
    public void testVisitLibraryField_withValidField_setsFieldOptimizationInfo() {
        // Arrange
        LibraryField libraryField = new LibraryField();
        assertNull(libraryField.getProcessingInfo(), "Processing info should be null before visiting");

        // Act
        keepMarker.visitLibraryField(libraryClass, libraryField);

        // Assert
        Object processingInfo = libraryField.getProcessingInfo();
        assertNotNull(processingInfo, "Processing info should be set after visiting");
        assertInstanceOf(FieldOptimizationInfo.class, processingInfo,
                "Processing info should be an instance of FieldOptimizationInfo");
    }

    /**
     * Tests that the FieldOptimizationInfo can be retrieved and used after being set.
     * Verifies that the info object is properly configured.
     */
    @Test
    public void testVisitLibraryField_fieldOptimizationInfoIsUsable() {
        // Arrange
        LibraryField libraryField = new LibraryField();

        // Act
        keepMarker.visitLibraryField(libraryClass, libraryField);

        // Assert
        FieldOptimizationInfo info = FieldOptimizationInfo.getFieldOptimizationInfo(libraryField);
        assertNotNull(info, "FieldOptimizationInfo should be retrievable");
        assertTrue(info.isKept(), "Field should be marked as kept");
    }

    /**
     * Tests that visitLibraryField can be called multiple times on the same field.
     * Each call replaces the previous FieldOptimizationInfo.
     */
    @Test
    public void testVisitLibraryField_calledMultipleTimes_replacesOptimizationInfo() {
        // Arrange
        LibraryField libraryField = new LibraryField();

        // Act - call visitLibraryField multiple times
        keepMarker.visitLibraryField(libraryClass, libraryField);
        FieldOptimizationInfo firstInfo = FieldOptimizationInfo.getFieldOptimizationInfo(libraryField);

        keepMarker.visitLibraryField(libraryClass, libraryField);
        FieldOptimizationInfo secondInfo = FieldOptimizationInfo.getFieldOptimizationInfo(libraryField);

        keepMarker.visitLibraryField(libraryClass, libraryField);
        FieldOptimizationInfo thirdInfo = FieldOptimizationInfo.getFieldOptimizationInfo(libraryField);

        // Assert - each call creates a new instance
        assertNotNull(firstInfo, "First info should not be null");
        assertNotNull(secondInfo, "Second info should not be null");
        assertNotNull(thirdInfo, "Third info should not be null");
        assertNotSame(firstInfo, secondInfo, "Second call should create a new info instance");
        assertNotSame(secondInfo, thirdInfo, "Third call should create a new info instance");
    }

    /**
     * Tests that visitLibraryField can be called on multiple different fields.
     * Verifies that each field gets its own FieldOptimizationInfo instance.
     */
    @Test
    public void testVisitLibraryField_multipleFields_eachGetsOwnOptimizationInfo() {
        // Arrange
        LibraryField field1 = new LibraryField();
        LibraryField field2 = new LibraryField();
        LibraryField field3 = new LibraryField();

        // Act
        keepMarker.visitLibraryField(libraryClass, field1);
        keepMarker.visitLibraryField(libraryClass, field2);
        keepMarker.visitLibraryField(libraryClass, field3);

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
     * Tests that visitLibraryField works correctly with mock objects.
     * Verifies that the method interacts correctly with the Field interface.
     */
    @Test
    public void testVisitLibraryField_withMockField_setsProcessingInfo() {
        // Arrange
        LibraryField mockField = mock(LibraryField.class);

        // Act
        keepMarker.visitLibraryField(libraryClass, mockField);

        // Assert - verify setProcessingInfo was called with a FieldOptimizationInfo instance
        verify(mockField, times(1)).setProcessingInfo(any(FieldOptimizationInfo.class));
    }

    /**
     * Tests that visitLibraryField throws NullPointerException with null field.
     * The method should not handle null gracefully as it needs to set processing info.
     */
    @Test
    public void testVisitLibraryField_withNullField_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> keepMarker.visitLibraryField(libraryClass, null),
                "visitLibraryField with null field should throw NullPointerException");
    }

    /**
     * Tests that visitLibraryField can handle null LibraryClass.
     * The class parameter is passed but not used in the method, so null should be acceptable.
     */
    @Test
    public void testVisitLibraryField_withNullClass_stillSetsOptimizationInfo() {
        // Arrange
        LibraryField libraryField = new LibraryField();

        // Act
        keepMarker.visitLibraryField(null, libraryField);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(libraryField),
                "Field should have optimization info even with null class");
    }

    /**
     * Tests that visitLibraryField can be used by multiple KeepMarker instances.
     * Verifies that different marker instances behave consistently.
     */
    @Test
    public void testVisitLibraryField_multipleKeepMarkerInstances_allSetOptimizationInfo() {
        // Arrange
        KeepMarker marker1 = new KeepMarker();
        KeepMarker marker2 = new KeepMarker();
        LibraryField field1 = new LibraryField();
        LibraryField field2 = new LibraryField();

        // Act
        marker1.visitLibraryField(libraryClass, field1);
        marker2.visitLibraryField(libraryClass, field2);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(field1),
                "First field should have optimization info");
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(field2),
                "Second field should have optimization info");
    }

    /**
     * Tests that visitLibraryField replaces existing processing info.
     * If the field already has processing info, it should be replaced.
     */
    @Test
    public void testVisitLibraryField_replacesExistingProcessingInfo() {
        // Arrange
        LibraryField libraryField = new LibraryField();
        Object existingInfo = new Object();
        libraryField.setProcessingInfo(existingInfo);

        // Act
        keepMarker.visitLibraryField(libraryClass, libraryField);

        // Assert
        Object newInfo = libraryField.getProcessingInfo();
        assertNotNull(newInfo, "New processing info should be set");
        assertNotSame(existingInfo, newInfo, "New info should replace existing info");
        assertInstanceOf(FieldOptimizationInfo.class, newInfo,
                "New info should be FieldOptimizationInfo");
    }

    /**
     * Tests that visitLibraryField does not throw any exceptions with valid input.
     * Verifies basic exception-free operation.
     */
    @Test
    public void testVisitLibraryField_withValidInput_doesNotThrowException() {
        // Arrange
        LibraryField libraryField = new LibraryField();

        // Act & Assert
        assertDoesNotThrow(() -> keepMarker.visitLibraryField(libraryClass, libraryField),
                "visitLibraryField should not throw exception with valid input");
    }

    /**
     * Tests that the FieldOptimizationInfo set by visitLibraryField has expected default values.
     * Verifies that the info object is properly initialized.
     */
    @Test
    public void testVisitLibraryField_setsFieldOptimizationInfoWithCorrectDefaults() {
        // Arrange
        LibraryField libraryField = new LibraryField();

        // Act
        keepMarker.visitLibraryField(libraryClass, libraryField);

        // Assert
        FieldOptimizationInfo info = FieldOptimizationInfo.getFieldOptimizationInfo(libraryField);
        assertTrue(info.isKept(), "Field should be kept");
        assertTrue(info.isWritten(), "Should report being written");
        assertTrue(info.isRead(), "Should report being read");
        assertFalse(info.canBeMadePrivate(), "Should not be able to be made private by default");
        assertNull(info.getReferencedClass(), "Referenced class should be null by default");
        assertNull(info.getValue(), "Value should be null by default");
    }

    /**
     * Tests that visitLibraryField can be called in rapid succession.
     * Verifies consistent behavior under rapid sequential calls.
     */
    @Test
    public void testVisitLibraryField_rapidSequentialCalls_consistentBehavior() {
        // Arrange
        LibraryField libraryField = new LibraryField();

        // Act & Assert - all calls should succeed
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() -> keepMarker.visitLibraryField(libraryClass, libraryField),
                    "Call " + i + " should not throw exception");
        }

        // Verify final state
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(libraryField),
                "Field should still have optimization info after many calls");
    }

    /**
     * Tests that visitLibraryField works with fields that have names.
     * Verifies the method works with more realistic field instances.
     */
    @Test
    public void testVisitLibraryField_withNamedField_setsOptimizationInfo() {
        // Arrange
        LibraryField libraryField = new LibraryField();
        libraryField.name = "testField";

        // Act
        keepMarker.visitLibraryField(libraryClass, libraryField);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(libraryField),
                "Named field should have optimization info set");
    }

    /**
     * Tests that visitLibraryField works with fields that have descriptors.
     * Verifies the method works with more realistic field instances.
     */
    @Test
    public void testVisitLibraryField_withFieldDescriptor_setsOptimizationInfo() {
        // Arrange
        LibraryField libraryField = new LibraryField();
        libraryField.descriptor = "Ljava/lang/String;";

        // Act
        keepMarker.visitLibraryField(libraryClass, libraryField);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(libraryField),
                "Field with descriptor should have optimization info set");
    }

    /**
     * Tests that the method can be used in a visitor pattern context.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitLibraryField_usedAsMemberVisitor_setsOptimizationInfo() {
        // Arrange
        LibraryField libraryField = new LibraryField();
        proguard.classfile.visitor.MemberVisitor visitor = keepMarker;

        // Act
        visitor.visitLibraryField(libraryClass, libraryField);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(libraryField),
                "Field should have optimization info when visited through MemberVisitor interface");
    }

    /**
     * Tests that visitLibraryField creates independent optimization info for each field.
     * Modifying one field's info should not affect another field's info.
     */
    @Test
    public void testVisitLibraryField_createsIndependentOptimizationInfo() {
        // Arrange
        LibraryField field1 = new LibraryField();
        LibraryField field2 = new LibraryField();

        // Act
        keepMarker.visitLibraryField(libraryClass, field1);
        keepMarker.visitLibraryField(libraryClass, field2);

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
     * Tests that visitLibraryField works correctly with different LibraryClass instances.
     * Verifies that the class parameter doesn't affect the field optimization info setting.
     */
    @Test
    public void testVisitLibraryField_withDifferentClasses_setsOptimizationInfo() {
        // Arrange
        LibraryClass class1 = new LibraryClass();
        LibraryClass class2 = new LibraryClass();
        LibraryField field1 = new LibraryField();
        LibraryField field2 = new LibraryField();

        // Act
        keepMarker.visitLibraryField(class1, field1);
        keepMarker.visitLibraryField(class2, field2);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(field1),
                "Field from first class should have optimization info");
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(field2),
                "Field from second class should have optimization info");
    }

    /**
     * Tests that visitLibraryField works correctly after the KeepMarker has visited other types.
     * Verifies that the method maintains consistent behavior across different visitor method calls.
     */
    @Test
    public void testVisitLibraryField_afterOtherVisitorMethods_stillSetsOptimizationInfo() {
        // Arrange
        LibraryField libraryField = new LibraryField();
        proguard.classfile.ProgramClass testClass = new proguard.classfile.ProgramClass();
        proguard.classfile.ProgramField programField = new proguard.classfile.ProgramField();

        // Act - call other visitor methods first
        keepMarker.visitAnyClass(testClass);
        keepMarker.visitProgramField(testClass, programField);
        keepMarker.visitLibraryField(libraryClass, libraryField);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(libraryField),
                "Library field should have optimization info even after other visitor methods were called");
    }

    /**
     * Tests that visitLibraryField can handle fields with access flags set.
     * Verifies the method works with fields that have various access modifiers.
     */
    @Test
    public void testVisitLibraryField_withAccessFlags_setsOptimizationInfo() {
        // Arrange
        LibraryField publicField = new LibraryField();
        publicField.u2accessFlags = 0x0001; // ACC_PUBLIC

        LibraryField privateField = new LibraryField();
        privateField.u2accessFlags = 0x0002; // ACC_PRIVATE

        LibraryField staticField = new LibraryField();
        staticField.u2accessFlags = 0x0008; // ACC_STATIC

        LibraryField finalField = new LibraryField();
        finalField.u2accessFlags = 0x0010; // ACC_FINAL

        // Act
        keepMarker.visitLibraryField(libraryClass, publicField);
        keepMarker.visitLibraryField(libraryClass, privateField);
        keepMarker.visitLibraryField(libraryClass, staticField);
        keepMarker.visitLibraryField(libraryClass, finalField);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(publicField),
                "Public field should have optimization info");
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(privateField),
                "Private field should have optimization info");
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(staticField),
                "Static field should have optimization info");
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(finalField),
                "Final field should have optimization info");
    }

    /**
     * Tests that visitLibraryField handles the same field being visited from different classes.
     * Verifies that the field's optimization info is updated regardless of which class visits it.
     */
    @Test
    public void testVisitLibraryField_sameFieldDifferentClasses_updatesOptimizationInfo() {
        // Arrange
        LibraryClass class1 = new LibraryClass();
        LibraryClass class2 = new LibraryClass();
        LibraryField libraryField = new LibraryField();

        // Act
        keepMarker.visitLibraryField(class1, libraryField);
        FieldOptimizationInfo info1 = FieldOptimizationInfo.getFieldOptimizationInfo(libraryField);

        keepMarker.visitLibraryField(class2, libraryField);
        FieldOptimizationInfo info2 = FieldOptimizationInfo.getFieldOptimizationInfo(libraryField);

        // Assert
        assertNotNull(info1, "Field should have optimization info after first visit");
        assertNotNull(info2, "Field should have optimization info after second visit");
        assertNotSame(info1, info2, "Second visit should create new optimization info");
    }

    /**
     * Tests that visitLibraryField does not modify the LibraryClass parameter.
     * The class should remain unchanged after the method call.
     */
    @Test
    public void testVisitLibraryField_doesNotModifyClass() {
        // Arrange
        LibraryClass testClass = new LibraryClass();
        LibraryField libraryField = new LibraryField();
        Object classProcessingInfo = new Object();
        testClass.setProcessingInfo(classProcessingInfo);

        // Act
        keepMarker.visitLibraryField(testClass, libraryField);

        // Assert
        assertSame(classProcessingInfo, testClass.getProcessingInfo(),
                "Class processing info should not be modified");
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(libraryField),
                "Field should have optimization info");
    }

    /**
     * Tests that visitLibraryField works with fields from Java standard library.
     * Verifies the method handles fields from typical library classes.
     */
    @Test
    public void testVisitLibraryField_withStandardLibraryField_setsOptimizationInfo() {
        // Arrange
        LibraryClass javaLangString = new LibraryClass();
        javaLangString.thisClassName = "java/lang/String";

        LibraryField valueField = new LibraryField();
        valueField.name = "value";
        valueField.descriptor = "[C";

        // Act
        keepMarker.visitLibraryField(javaLangString, valueField);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(valueField),
                "Standard library field should have optimization info");
    }

    /**
     * Tests that visitLibraryField can handle fields with both name and descriptor set.
     * Verifies the method works with fully configured field instances.
     */
    @Test
    public void testVisitLibraryField_withCompleteFieldInfo_setsOptimizationInfo() {
        // Arrange
        LibraryField libraryField = new LibraryField();
        libraryField.name = "myField";
        libraryField.descriptor = "I"; // int
        libraryField.u2accessFlags = 0x0001; // ACC_PUBLIC

        // Act
        keepMarker.visitLibraryField(libraryClass, libraryField);

        // Assert
        assertNotNull(FieldOptimizationInfo.getFieldOptimizationInfo(libraryField),
                "Fully configured field should have optimization info");
    }
}

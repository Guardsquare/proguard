package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.LibraryField;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.LibraryMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.optimize.info.ClassOptimizationInfo;
import proguard.optimize.info.ProgramClassOptimizationInfo;
import proguard.optimize.info.FieldOptimizationInfo;
import proguard.optimize.info.ProgramFieldOptimizationInfo;
import proguard.optimize.info.MethodOptimizationInfo;
import proguard.optimize.info.ProgramMethodOptimizationInfo;
import proguard.optimize.info.CodeAttributeOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link KeepMarker#isKept(Clazz)}, {@link KeepMarker#isKept(proguard.classfile.Field)},
 * {@link KeepMarker#isKept(proguard.classfile.Method)}, and {@link KeepMarker#isKept(proguard.classfile.attribute.CodeAttribute)}.
 *
 * The isKept(Clazz) method is a static utility method that checks if a class is marked as kept
 * during optimization. It does so by:
 * 1. Retrieving the ClassOptimizationInfo from the class's processing info
 * 2. Checking if the info is not null and if info.isKept() returns true
 *
 * The isKept(Field) method is a static utility method that checks if a field is marked as kept
 * during optimization. It does so by:
 * 1. Retrieving the FieldOptimizationInfo from the field's processing info
 * 2. Checking if the info is not null and if info.isKept() returns true
 *
 * The isKept(Method) method is a static utility method that checks if a method is marked as kept
 * during optimization. It does so by:
 * 1. Retrieving the MethodOptimizationInfo from the method's processing info
 * 2. Checking if the info is not null and if info.isKept() returns true
 *
 * The isKept(CodeAttribute) method is a static utility method that checks if a code attribute is marked as kept
 * during optimization. It does so by:
 * 1. Retrieving the CodeAttributeOptimizationInfo from the code attribute's processing info
 * 2. Checking if the info is not null and if info.isKept() returns true
 *
 * These tests verify all four methods with their respective types and optimization info classes.
 */
public class KeepMarkerClaude_isKeptTest {

    /**
     * Tests that isKept returns true for a class with ClassOptimizationInfo set.
     * This is the standard case where a class has been visited by KeepMarker.
     */
    @Test
    public void testIsKept_withClassOptimizationInfo_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act
        boolean result = KeepMarker.isKept(programClass);

        // Assert
        assertTrue(result, "Class with ClassOptimizationInfo should be kept");
    }

    /**
     * Tests that isKept returns false for a class without any processing info.
     * When a class hasn't been visited by KeepMarker, it has no processing info.
     */
    @Test
    public void testIsKept_withNullProcessingInfo_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        assertNull(programClass.getProcessingInfo(), "Processing info should be null");

        // Act
        boolean result = KeepMarker.isKept(programClass);

        // Assert
        assertFalse(result, "Class without processing info should not be kept");
    }

    /**
     * Tests that isKept throws ClassCastException when processing info is not ClassOptimizationInfo.
     * The method attempts to cast processing info to ClassOptimizationInfo, which will fail
     * if the class has other types of processing info set.
     */
    @Test
    public void testIsKept_withNonClassOptimizationInfoProcessingInfo_throwsClassCastException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.setProcessingInfo("Some other processing info");

        // Act & Assert
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(programClass),
                "isKept should throw ClassCastException with non-ClassOptimizationInfo processing info");
    }

    /**
     * Tests that isKept returns true for a class visited by KeepMarker.
     * This verifies the integration with the visitor pattern.
     */
    @Test
    public void testIsKept_afterKeepMarkerVisit_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        KeepMarker keepMarker = new KeepMarker();

        // Act
        keepMarker.visitAnyClass(programClass);
        boolean result = KeepMarker.isKept(programClass);

        // Assert
        assertTrue(result, "Class visited by KeepMarker should be kept");
    }

    /**
     * Tests that isKept returns true for multiple classes each with ClassOptimizationInfo.
     * Verifies that each class's kept status is independent.
     */
    @Test
    public void testIsKept_multipleClasses_eachKeptIndependently() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ClassOptimizationInfo.setClassOptimizationInfo(class1);
        ClassOptimizationInfo.setClassOptimizationInfo(class2);
        ClassOptimizationInfo.setClassOptimizationInfo(class3);

        // Act & Assert
        assertTrue(KeepMarker.isKept(class1), "First class should be kept");
        assertTrue(KeepMarker.isKept(class2), "Second class should be kept");
        assertTrue(KeepMarker.isKept(class3), "Third class should be kept");
    }

    /**
     * Tests that isKept returns correct values for a mix of kept and non-kept classes.
     * Some classes have optimization info, others don't.
     */
    @Test
    public void testIsKept_mixedClasses_returnsCorrectStatusForEach() {
        // Arrange
        ProgramClass keptClass = new ProgramClass();
        ProgramClass notKeptClass = new ProgramClass();

        ClassOptimizationInfo.setClassOptimizationInfo(keptClass);
        // notKeptClass has no processing info

        // Act & Assert
        assertTrue(KeepMarker.isKept(keptClass), "Class with info should be kept");
        assertFalse(KeepMarker.isKept(notKeptClass), "Class without info should not be kept");
    }

    /**
     * Tests that isKept behavior is consistent across multiple calls.
     * Calling isKept multiple times should return the same result.
     */
    @Test
    public void testIsKept_calledMultipleTimes_consistentResult() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act
        boolean result1 = KeepMarker.isKept(programClass);
        boolean result2 = KeepMarker.isKept(programClass);
        boolean result3 = KeepMarker.isKept(programClass);

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that isKept returns false consistently for a class without optimization info.
     * Multiple calls should all return false.
     */
    @Test
    public void testIsKept_notKeptClass_consistentlyReturnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act
        boolean result1 = KeepMarker.isKept(programClass);
        boolean result2 = KeepMarker.isKept(programClass);
        boolean result3 = KeepMarker.isKept(programClass);

        // Assert
        assertFalse(result1, "First call should return false");
        assertFalse(result2, "Second call should return false");
        assertFalse(result3, "Third call should return false");
    }

    /**
     * Tests that isKept reflects changes when ClassOptimizationInfo is added to a class.
     * The kept status should change from false to true.
     */
    @Test
    public void testIsKept_beforeAndAfterSettingOptimizationInfo_changesStatus() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - before setting optimization info
        assertFalse(KeepMarker.isKept(programClass), "Class should not be kept initially");

        // Act - set optimization info
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Assert - after setting optimization info
        assertTrue(KeepMarker.isKept(programClass), "Class should be kept after setting info");
    }

    /**
     * Tests that isKept can handle classes that have been marked multiple times.
     * Re-visiting a class should not affect its kept status.
     */
    @Test
    public void testIsKept_classMarkedMultipleTimes_remainsKept() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        KeepMarker keepMarker = new KeepMarker();

        // Act - mark the class multiple times
        keepMarker.visitAnyClass(programClass);
        boolean result1 = KeepMarker.isKept(programClass);

        keepMarker.visitAnyClass(programClass);
        boolean result2 = KeepMarker.isKept(programClass);

        keepMarker.visitAnyClass(programClass);
        boolean result3 = KeepMarker.isKept(programClass);

        // Assert
        assertTrue(result1, "Class should be kept after first visit");
        assertTrue(result2, "Class should be kept after second visit");
        assertTrue(result3, "Class should be kept after third visit");
    }

    /**
     * Tests that isKept works correctly after processing info is replaced.
     * Setting a new ClassOptimizationInfo should maintain kept status.
     */
    @Test
    public void testIsKept_afterReplacingOptimizationInfo_stillReturnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - set optimization info multiple times
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);
        assertTrue(KeepMarker.isKept(programClass), "Should be kept after first set");

        ClassOptimizationInfo.setClassOptimizationInfo(programClass);
        assertTrue(KeepMarker.isKept(programClass), "Should be kept after replacement");
    }

    /**
     * Tests that isKept handles a class where processing info was removed.
     * Setting processing info to null should make the class not kept.
     */
    @Test
    public void testIsKept_afterRemovingProcessingInfo_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);
        assertTrue(KeepMarker.isKept(programClass), "Should be kept initially");

        // Act - remove processing info
        programClass.setProcessingInfo(null);

        // Assert
        assertFalse(KeepMarker.isKept(programClass), "Should not be kept after removing info");
    }

    /**
     * Tests that isKept works with newly instantiated ProgramClass objects.
     * Fresh class instances should not be kept by default.
     */
    @Test
    public void testIsKept_withFreshProgramClass_returnsFalse() {
        // Arrange & Act
        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();
        ProgramClass programClass3 = new ProgramClass();

        // Assert
        assertFalse(KeepMarker.isKept(programClass1), "Fresh class 1 should not be kept");
        assertFalse(KeepMarker.isKept(programClass2), "Fresh class 2 should not be kept");
        assertFalse(KeepMarker.isKept(programClass3), "Fresh class 3 should not be kept");
    }

    /**
     * Tests that isKept correctly identifies kept classes in a batch scenario.
     * Simulates marking multiple classes and checking their status.
     */
    @Test
    public void testIsKept_batchProcessing_correctlyIdentifiesKeptClasses() {
        // Arrange
        ProgramClass[] classes = new ProgramClass[10];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = new ProgramClass();
        }

        // Mark only even-indexed classes
        for (int i = 0; i < classes.length; i += 2) {
            ClassOptimizationInfo.setClassOptimizationInfo(classes[i]);
        }

        // Act & Assert
        for (int i = 0; i < classes.length; i++) {
            if (i % 2 == 0) {
                assertTrue(KeepMarker.isKept(classes[i]),
                        "Class at index " + i + " should be kept");
            } else {
                assertFalse(KeepMarker.isKept(classes[i]),
                        "Class at index " + i + " should not be kept");
            }
        }
    }

    /**
     * Tests that isKept does not throw exceptions with valid input.
     * The method should handle standard cases without throwing.
     */
    @Test
    public void testIsKept_withValidInput_doesNotThrowException() {
        // Arrange
        ProgramClass keptClass = new ProgramClass();
        ProgramClass notKeptClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(keptClass);

        // Act & Assert
        assertDoesNotThrow(() -> KeepMarker.isKept(keptClass),
                "isKept with kept class should not throw");
        assertDoesNotThrow(() -> KeepMarker.isKept(notKeptClass),
                "isKept with non-kept class should not throw");
    }

    /**
     * Tests that isKept is consistent with direct ClassOptimizationInfo checks.
     * The static method should match what we'd get from directly checking the info.
     */
    @Test
    public void testIsKept_consistentWithDirectInfoCheck() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act
        boolean staticMethodResult = KeepMarker.isKept(programClass);
        ClassOptimizationInfo info = ClassOptimizationInfo.getClassOptimizationInfo(programClass);
        boolean directCheckResult = (info != null && info.isKept());

        // Assert
        assertEquals(directCheckResult, staticMethodResult,
                "Static method should match direct info check");
    }

    /**
     * Tests that isKept handles rapid sequential calls efficiently.
     * The method should be able to handle many consecutive calls.
     */
    @Test
    public void testIsKept_rapidSequentialCalls_handlesEfficiently() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act & Assert - all calls should return true and not throw
        for (int i = 0; i < 1000; i++) {
            assertTrue(KeepMarker.isKept(programClass),
                    "Call " + i + " should return true");
        }
    }

    /**
     * Tests that isKept works correctly with base ClassOptimizationInfo.
     * The base class should return true for isKept.
     */
    @Test
    public void testIsKept_withBaseClassOptimizationInfo_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.setProcessingInfo(new ClassOptimizationInfo());

        // Act
        boolean result = KeepMarker.isKept(programClass);

        // Assert
        assertTrue(result, "Class with base ClassOptimizationInfo should be kept");
    }

    /**
     * Tests isKept behavior when processing info is set to various non-ClassOptimizationInfo types.
     * These should throw ClassCastException due to the unchecked cast in getClassOptimizationInfo.
     */
    @Test
    public void testIsKept_withVariousNonOptimizationInfoTypes_throwsClassCastException() {
        // Arrange
        ProgramClass classWithString = new ProgramClass();
        ProgramClass classWithInteger = new ProgramClass();
        ProgramClass classWithObject = new ProgramClass();

        classWithString.setProcessingInfo("test");
        classWithInteger.setProcessingInfo(42);
        classWithObject.setProcessingInfo(new Object());

        // Act & Assert
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(classWithString),
                "Class with String processing info should throw ClassCastException");
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(classWithInteger),
                "Class with Integer processing info should throw ClassCastException");
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(classWithObject),
                "Class with Object processing info should throw ClassCastException");
    }

    /**
     * Tests that isKept returns false for ProgramClassOptimizationInfo.
     * ProgramClassOptimizationInfo.isKept() returns false, unlike the base ClassOptimizationInfo.
     */
    @Test
    public void testIsKept_withProgramClassOptimizationInfo_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.setProcessingInfo(new ProgramClassOptimizationInfo());

        // Act
        boolean result = KeepMarker.isKept(programClass);

        // Assert
        assertFalse(result, "Class with ProgramClassOptimizationInfo should not be kept " +
                "(ProgramClassOptimizationInfo.isKept() returns false)");
    }

    /**
     * Tests the difference between ClassOptimizationInfo and ProgramClassOptimizationInfo.
     * Verifies that isKept behaves differently based on the concrete type.
     */
    @Test
    public void testIsKept_differentOptimizationInfoTypes_returnDifferentResults() {
        // Arrange
        ProgramClass classWithBaseInfo = new ProgramClass();
        ProgramClass classWithProgramInfo = new ProgramClass();

        classWithBaseInfo.setProcessingInfo(new ClassOptimizationInfo());
        classWithProgramInfo.setProcessingInfo(new ProgramClassOptimizationInfo());

        // Act
        boolean baseResult = KeepMarker.isKept(classWithBaseInfo);
        boolean programResult = KeepMarker.isKept(classWithProgramInfo);

        // Assert
        assertTrue(baseResult, "Base ClassOptimizationInfo should be kept");
        assertFalse(programResult, "ProgramClassOptimizationInfo should not be kept");
        assertNotEquals(baseResult, programResult,
                "Different optimization info types should yield different results");
    }

    // ==================== Tests for isKept(Field) ====================

    /**
     * Tests that isKept returns true for a field with FieldOptimizationInfo set.
     * This is the standard case where a field has been visited by KeepMarker.
     */
    @Test
    public void testIsKeptField_withFieldOptimizationInfo_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, programField);

        // Act
        boolean result = KeepMarker.isKept(programField);

        // Assert
        assertTrue(result, "Field with FieldOptimizationInfo should be kept");
    }

    /**
     * Tests that isKept returns false for a field without any processing info.
     * When a field hasn't been visited by KeepMarker, it has no processing info.
     */
    @Test
    public void testIsKeptField_withNullProcessingInfo_returnsFalse() {
        // Arrange
        ProgramField programField = new ProgramField();
        assertNull(programField.getProcessingInfo(), "Processing info should be null");

        // Act
        boolean result = KeepMarker.isKept(programField);

        // Assert
        assertFalse(result, "Field without processing info should not be kept");
    }

    /**
     * Tests that isKept throws ClassCastException when field processing info is not FieldOptimizationInfo.
     * The method attempts to cast processing info to FieldOptimizationInfo, which will fail
     * if the field has other types of processing info set.
     */
    @Test
    public void testIsKeptField_withNonFieldOptimizationInfoProcessingInfo_throwsClassCastException() {
        // Arrange
        ProgramField programField = new ProgramField();
        programField.setProcessingInfo("Some other processing info");

        // Act & Assert
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(programField),
                "isKept should throw ClassCastException with non-FieldOptimizationInfo processing info");
    }

    /**
     * Tests that isKept returns true for a field visited by KeepMarker.
     * This verifies the integration with the visitor pattern.
     */
    @Test
    public void testIsKeptField_afterKeepMarkerVisit_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();
        KeepMarker keepMarker = new KeepMarker();

        // Act
        keepMarker.visitProgramField(programClass, programField);
        boolean result = KeepMarker.isKept(programField);

        // Assert
        assertTrue(result, "Field visited by KeepMarker should be kept");
    }

    /**
     * Tests that isKept returns true for multiple fields each with FieldOptimizationInfo.
     * Verifies that each field's kept status is independent.
     */
    @Test
    public void testIsKeptField_multipleFields_eachKeptIndependently() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField field1 = new ProgramField();
        ProgramField field2 = new ProgramField();
        ProgramField field3 = new ProgramField();

        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, field1);
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, field2);
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, field3);

        // Act & Assert
        assertTrue(KeepMarker.isKept(field1), "First field should be kept");
        assertTrue(KeepMarker.isKept(field2), "Second field should be kept");
        assertTrue(KeepMarker.isKept(field3), "Third field should be kept");
    }

    /**
     * Tests that isKept returns correct values for a mix of kept and non-kept fields.
     * Some fields have optimization info, others don't.
     */
    @Test
    public void testIsKeptField_mixedFields_returnsCorrectStatusForEach() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField keptField = new ProgramField();
        ProgramField notKeptField = new ProgramField();

        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, keptField);
        // notKeptField has no processing info

        // Act & Assert
        assertTrue(KeepMarker.isKept(keptField), "Field with info should be kept");
        assertFalse(KeepMarker.isKept(notKeptField), "Field without info should not be kept");
    }

    /**
     * Tests that isKept behavior is consistent across multiple calls for fields.
     * Calling isKept multiple times should return the same result.
     */
    @Test
    public void testIsKeptField_calledMultipleTimes_consistentResult() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, programField);

        // Act
        boolean result1 = KeepMarker.isKept(programField);
        boolean result2 = KeepMarker.isKept(programField);
        boolean result3 = KeepMarker.isKept(programField);

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that isKept returns false consistently for a field without optimization info.
     * Multiple calls should all return false.
     */
    @Test
    public void testIsKeptField_notKeptField_consistentlyReturnsFalse() {
        // Arrange
        ProgramField programField = new ProgramField();

        // Act
        boolean result1 = KeepMarker.isKept(programField);
        boolean result2 = KeepMarker.isKept(programField);
        boolean result3 = KeepMarker.isKept(programField);

        // Assert
        assertFalse(result1, "First call should return false");
        assertFalse(result2, "Second call should return false");
        assertFalse(result3, "Third call should return false");
    }

    /**
     * Tests that isKept reflects changes when FieldOptimizationInfo is added to a field.
     * The kept status should change from false to true.
     */
    @Test
    public void testIsKeptField_beforeAndAfterSettingOptimizationInfo_changesStatus() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act & Assert - before setting optimization info
        assertFalse(KeepMarker.isKept(programField), "Field should not be kept initially");

        // Act - set optimization info
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, programField);

        // Assert - after setting optimization info
        assertTrue(KeepMarker.isKept(programField), "Field should be kept after setting info");
    }

    /**
     * Tests that isKept works correctly after processing info is replaced.
     * Setting a new FieldOptimizationInfo should maintain kept status.
     */
    @Test
    public void testIsKeptField_afterReplacingOptimizationInfo_stillReturnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act - set optimization info multiple times
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, programField);
        assertTrue(KeepMarker.isKept(programField), "Should be kept after first set");

        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, programField);
        assertTrue(KeepMarker.isKept(programField), "Should be kept after replacement");
    }

    /**
     * Tests that isKept handles a field where processing info was removed.
     * Setting processing info to null should make the field not kept.
     */
    @Test
    public void testIsKeptField_afterRemovingProcessingInfo_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, programField);
        assertTrue(KeepMarker.isKept(programField), "Should be kept initially");

        // Act - remove processing info
        programField.setProcessingInfo(null);

        // Assert
        assertFalse(KeepMarker.isKept(programField), "Should not be kept after removing info");
    }

    /**
     * Tests that isKept works with newly instantiated ProgramField objects.
     * Fresh field instances should not be kept by default.
     */
    @Test
    public void testIsKeptField_withFreshProgramField_returnsFalse() {
        // Arrange & Act
        ProgramField programField1 = new ProgramField();
        ProgramField programField2 = new ProgramField();
        ProgramField programField3 = new ProgramField();

        // Assert
        assertFalse(KeepMarker.isKept(programField1), "Fresh field 1 should not be kept");
        assertFalse(KeepMarker.isKept(programField2), "Fresh field 2 should not be kept");
        assertFalse(KeepMarker.isKept(programField3), "Fresh field 3 should not be kept");
    }

    /**
     * Tests that isKept correctly identifies kept fields in a batch scenario.
     * Simulates marking multiple fields and checking their status.
     */
    @Test
    public void testIsKeptField_batchProcessing_correctlyIdentifiesKeptFields() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField[] fields = new ProgramField[10];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new ProgramField();
        }

        // Mark only even-indexed fields
        for (int i = 0; i < fields.length; i += 2) {
            FieldOptimizationInfo.setFieldOptimizationInfo(programClass, fields[i]);
        }

        // Act & Assert
        for (int i = 0; i < fields.length; i++) {
            if (i % 2 == 0) {
                assertTrue(KeepMarker.isKept(fields[i]),
                        "Field at index " + i + " should be kept");
            } else {
                assertFalse(KeepMarker.isKept(fields[i]),
                        "Field at index " + i + " should not be kept");
            }
        }
    }

    /**
     * Tests that isKept does not throw exceptions with valid field input.
     * The method should handle standard cases without throwing.
     */
    @Test
    public void testIsKeptField_withValidInput_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField keptField = new ProgramField();
        ProgramField notKeptField = new ProgramField();
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, keptField);

        // Act & Assert
        assertDoesNotThrow(() -> KeepMarker.isKept(keptField),
                "isKept with kept field should not throw");
        assertDoesNotThrow(() -> KeepMarker.isKept(notKeptField),
                "isKept with non-kept field should not throw");
    }

    /**
     * Tests that isKept is consistent with direct FieldOptimizationInfo checks.
     * The static method should match what we'd get from directly checking the info.
     */
    @Test
    public void testIsKeptField_consistentWithDirectInfoCheck() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, programField);

        // Act
        boolean staticMethodResult = KeepMarker.isKept(programField);
        FieldOptimizationInfo info = FieldOptimizationInfo.getFieldOptimizationInfo(programField);
        boolean directCheckResult = (info != null && info.isKept());

        // Assert
        assertEquals(directCheckResult, staticMethodResult,
                "Static method should match direct info check");
    }

    /**
     * Tests that isKept works correctly with base FieldOptimizationInfo.
     * The base class should return true for isKept.
     */
    @Test
    public void testIsKeptField_withBaseFieldOptimizationInfo_returnsTrue() {
        // Arrange
        ProgramField programField = new ProgramField();
        programField.setProcessingInfo(new FieldOptimizationInfo());

        // Act
        boolean result = KeepMarker.isKept(programField);

        // Assert
        assertTrue(result, "Field with base FieldOptimizationInfo should be kept");
    }

    /**
     * Tests isKept behavior when processing info is set to various non-FieldOptimizationInfo types.
     * These should throw ClassCastException due to the unchecked cast in getFieldOptimizationInfo.
     */
    @Test
    public void testIsKeptField_withVariousNonOptimizationInfoTypes_throwsClassCastException() {
        // Arrange
        ProgramField fieldWithString = new ProgramField();
        ProgramField fieldWithInteger = new ProgramField();
        ProgramField fieldWithObject = new ProgramField();

        fieldWithString.setProcessingInfo("test");
        fieldWithInteger.setProcessingInfo(42);
        fieldWithObject.setProcessingInfo(new Object());

        // Act & Assert
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(fieldWithString),
                "Field with String processing info should throw ClassCastException");
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(fieldWithInteger),
                "Field with Integer processing info should throw ClassCastException");
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(fieldWithObject),
                "Field with Object processing info should throw ClassCastException");
    }

    /**
     * Tests that isKept returns false for ProgramFieldOptimizationInfo.
     * ProgramFieldOptimizationInfo.isKept() returns false, unlike the base FieldOptimizationInfo.
     */
    @Test
    public void testIsKeptField_withProgramFieldOptimizationInfo_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();
        programField.setProcessingInfo(new ProgramFieldOptimizationInfo(programClass, programField, false));

        // Act
        boolean result = KeepMarker.isKept(programField);

        // Assert
        assertFalse(result, "Field with ProgramFieldOptimizationInfo should not be kept " +
                "(ProgramFieldOptimizationInfo.isKept() returns false)");
    }

    /**
     * Tests the difference between FieldOptimizationInfo and ProgramFieldOptimizationInfo.
     * Verifies that isKept behaves differently based on the concrete type.
     */
    @Test
    public void testIsKeptField_differentOptimizationInfoTypes_returnDifferentResults() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField fieldWithBaseInfo = new ProgramField();
        ProgramField fieldWithProgramInfo = new ProgramField();

        fieldWithBaseInfo.setProcessingInfo(new FieldOptimizationInfo());
        fieldWithProgramInfo.setProcessingInfo(new ProgramFieldOptimizationInfo(programClass, fieldWithProgramInfo, false));

        // Act
        boolean baseResult = KeepMarker.isKept(fieldWithBaseInfo);
        boolean programResult = KeepMarker.isKept(fieldWithProgramInfo);

        // Assert
        assertTrue(baseResult, "Base FieldOptimizationInfo should be kept");
        assertFalse(programResult, "ProgramFieldOptimizationInfo should not be kept");
        assertNotEquals(baseResult, programResult,
                "Different optimization info types should yield different results");
    }

    /**
     * Tests that isKept works with LibraryField instances.
     * Verifies the method works with different Field implementations.
     */
    @Test
    public void testIsKeptField_withLibraryField_worksCorrectly() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryField libraryField = new LibraryField();
        FieldOptimizationInfo.setFieldOptimizationInfo(libraryClass, libraryField);

        // Act
        boolean result = KeepMarker.isKept(libraryField);

        // Assert
        assertTrue(result, "LibraryField with FieldOptimizationInfo should be kept");
    }

    /**
     * Tests that isKept works correctly after a LibraryField is visited by KeepMarker.
     * Verifies integration with the visitor pattern for library fields.
     */
    @Test
    public void testIsKeptField_afterKeepMarkerVisitLibraryField_returnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryField libraryField = new LibraryField();
        KeepMarker keepMarker = new KeepMarker();

        // Act
        keepMarker.visitLibraryField(libraryClass, libraryField);
        boolean result = KeepMarker.isKept(libraryField);

        // Assert
        assertTrue(result, "LibraryField visited by KeepMarker should be kept");
    }

    // ==================== Tests for isKept(Method) ====================

    /**
     * Tests that isKept returns true for a method with MethodOptimizationInfo set.
     * This is the standard case where a method has been visited by KeepMarker.
     */
    @Test
    public void testIsKeptMethod_withMethodOptimizationInfo_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Act
        boolean result = KeepMarker.isKept(programMethod);

        // Assert
        assertTrue(result, "Method with MethodOptimizationInfo should be kept");
    }

    /**
     * Tests that isKept returns false for a method without any processing info.
     * When a method hasn't been visited by KeepMarker, it has no processing info.
     */
    @Test
    public void testIsKeptMethod_withNullProcessingInfo_returnsFalse() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        assertNull(programMethod.getProcessingInfo(), "Processing info should be null");

        // Act
        boolean result = KeepMarker.isKept(programMethod);

        // Assert
        assertFalse(result, "Method without processing info should not be kept");
    }

    /**
     * Tests that isKept throws ClassCastException when method processing info is not MethodOptimizationInfo.
     * The method attempts to cast processing info to MethodOptimizationInfo, which will fail
     * if the method has other types of processing info set.
     */
    @Test
    public void testIsKeptMethod_withNonMethodOptimizationInfoProcessingInfo_throwsClassCastException() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.setProcessingInfo("Some other processing info");

        // Act & Assert
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(programMethod),
                "isKept should throw ClassCastException with non-MethodOptimizationInfo processing info");
    }

    /**
     * Tests that isKept returns true for a method visited by KeepMarker.
     * This verifies the integration with the visitor pattern.
     */
    @Test
    public void testIsKeptMethod_afterKeepMarkerVisit_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        KeepMarker keepMarker = new KeepMarker();

        // Act
        keepMarker.visitProgramMethod(programClass, programMethod);
        boolean result = KeepMarker.isKept(programMethod);

        // Assert
        assertTrue(result, "Method visited by KeepMarker should be kept");
    }

    /**
     * Tests that isKept returns true for multiple methods each with MethodOptimizationInfo.
     * Verifies that each method's kept status is independent.
     */
    @Test
    public void testIsKeptMethod_multipleMethods_eachKeptIndependently() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramMethod method3 = new ProgramMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method2);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method3);

        // Act & Assert
        assertTrue(KeepMarker.isKept(method1), "First method should be kept");
        assertTrue(KeepMarker.isKept(method2), "Second method should be kept");
        assertTrue(KeepMarker.isKept(method3), "Third method should be kept");
    }

    /**
     * Tests that isKept returns correct values for a mix of kept and non-kept methods.
     * Some methods have optimization info, others don't.
     */
    @Test
    public void testIsKeptMethod_mixedMethods_returnsCorrectStatusForEach() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod keptMethod = new ProgramMethod();
        ProgramMethod notKeptMethod = new ProgramMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, keptMethod);
        // notKeptMethod has no processing info

        // Act & Assert
        assertTrue(KeepMarker.isKept(keptMethod), "Method with info should be kept");
        assertFalse(KeepMarker.isKept(notKeptMethod), "Method without info should not be kept");
    }

    /**
     * Tests that isKept behavior is consistent across multiple calls for methods.
     * Calling isKept multiple times should return the same result.
     */
    @Test
    public void testIsKeptMethod_calledMultipleTimes_consistentResult() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Act
        boolean result1 = KeepMarker.isKept(programMethod);
        boolean result2 = KeepMarker.isKept(programMethod);
        boolean result3 = KeepMarker.isKept(programMethod);

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that isKept returns false consistently for a method without optimization info.
     * Multiple calls should all return false.
     */
    @Test
    public void testIsKeptMethod_notKeptMethod_consistentlyReturnsFalse() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Act
        boolean result1 = KeepMarker.isKept(programMethod);
        boolean result2 = KeepMarker.isKept(programMethod);
        boolean result3 = KeepMarker.isKept(programMethod);

        // Assert
        assertFalse(result1, "First call should return false");
        assertFalse(result2, "Second call should return false");
        assertFalse(result3, "Third call should return false");
    }

    /**
     * Tests that isKept reflects changes when MethodOptimizationInfo is added to a method.
     * The kept status should change from false to true.
     */
    @Test
    public void testIsKeptMethod_beforeAndAfterSettingOptimizationInfo_changesStatus() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert - before setting optimization info
        assertFalse(KeepMarker.isKept(programMethod), "Method should not be kept initially");

        // Act - set optimization info
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Assert - after setting optimization info
        assertTrue(KeepMarker.isKept(programMethod), "Method should be kept after setting info");
    }

    /**
     * Tests that isKept works correctly after processing info is replaced.
     * Setting a new MethodOptimizationInfo should maintain kept status.
     */
    @Test
    public void testIsKeptMethod_afterReplacingOptimizationInfo_stillReturnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act - set optimization info multiple times
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);
        assertTrue(KeepMarker.isKept(programMethod), "Should be kept after first set");

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);
        assertTrue(KeepMarker.isKept(programMethod), "Should be kept after replacement");
    }

    /**
     * Tests that isKept handles a method where processing info was removed.
     * Setting processing info to null should make the method not kept.
     */
    @Test
    public void testIsKeptMethod_afterRemovingProcessingInfo_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);
        assertTrue(KeepMarker.isKept(programMethod), "Should be kept initially");

        // Act - remove processing info
        programMethod.setProcessingInfo(null);

        // Assert
        assertFalse(KeepMarker.isKept(programMethod), "Should not be kept after removing info");
    }

    /**
     * Tests that isKept works with newly instantiated ProgramMethod objects.
     * Fresh method instances should not be kept by default.
     */
    @Test
    public void testIsKeptMethod_withFreshProgramMethod_returnsFalse() {
        // Arrange & Act
        ProgramMethod programMethod1 = new ProgramMethod();
        ProgramMethod programMethod2 = new ProgramMethod();
        ProgramMethod programMethod3 = new ProgramMethod();

        // Assert
        assertFalse(KeepMarker.isKept(programMethod1), "Fresh method 1 should not be kept");
        assertFalse(KeepMarker.isKept(programMethod2), "Fresh method 2 should not be kept");
        assertFalse(KeepMarker.isKept(programMethod3), "Fresh method 3 should not be kept");
    }

    /**
     * Tests that isKept correctly identifies kept methods in a batch scenario.
     * Simulates marking multiple methods and checking their status.
     */
    @Test
    public void testIsKeptMethod_batchProcessing_correctlyIdentifiesKeptMethods() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod[] methods = new ProgramMethod[10];
        for (int i = 0; i < methods.length; i++) {
            methods[i] = new ProgramMethod();
        }

        // Mark only even-indexed methods
        for (int i = 0; i < methods.length; i += 2) {
            MethodOptimizationInfo.setMethodOptimizationInfo(programClass, methods[i]);
        }

        // Act & Assert
        for (int i = 0; i < methods.length; i++) {
            if (i % 2 == 0) {
                assertTrue(KeepMarker.isKept(methods[i]),
                        "Method at index " + i + " should be kept");
            } else {
                assertFalse(KeepMarker.isKept(methods[i]),
                        "Method at index " + i + " should not be kept");
            }
        }
    }

    /**
     * Tests that isKept does not throw exceptions with valid method input.
     * The method should handle standard cases without throwing.
     */
    @Test
    public void testIsKeptMethod_withValidInput_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod keptMethod = new ProgramMethod();
        ProgramMethod notKeptMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, keptMethod);

        // Act & Assert
        assertDoesNotThrow(() -> KeepMarker.isKept(keptMethod),
                "isKept with kept method should not throw");
        assertDoesNotThrow(() -> KeepMarker.isKept(notKeptMethod),
                "isKept with non-kept method should not throw");
    }

    /**
     * Tests that isKept is consistent with direct MethodOptimizationInfo checks.
     * The static method should match what we'd get from directly checking the info.
     */
    @Test
    public void testIsKeptMethod_consistentWithDirectInfoCheck() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Act
        boolean staticMethodResult = KeepMarker.isKept(programMethod);
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        boolean directCheckResult = (info != null && info.isKept());

        // Assert
        assertEquals(directCheckResult, staticMethodResult,
                "Static method should match direct info check");
    }

    /**
     * Tests that isKept works correctly with base MethodOptimizationInfo.
     * The base class should return true for isKept.
     */
    @Test
    public void testIsKeptMethod_withBaseMethodOptimizationInfo_returnsTrue() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.setProcessingInfo(new MethodOptimizationInfo());

        // Act
        boolean result = KeepMarker.isKept(programMethod);

        // Assert
        assertTrue(result, "Method with base MethodOptimizationInfo should be kept");
    }

    /**
     * Tests isKept behavior when processing info is set to various non-MethodOptimizationInfo types.
     * These should throw ClassCastException due to the unchecked cast in getMethodOptimizationInfo.
     */
    @Test
    public void testIsKeptMethod_withVariousNonOptimizationInfoTypes_throwsClassCastException() {
        // Arrange
        ProgramMethod methodWithString = new ProgramMethod();
        ProgramMethod methodWithInteger = new ProgramMethod();
        ProgramMethod methodWithObject = new ProgramMethod();

        methodWithString.setProcessingInfo("test");
        methodWithInteger.setProcessingInfo(42);
        methodWithObject.setProcessingInfo(new Object());

        // Act & Assert
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(methodWithString),
                "Method with String processing info should throw ClassCastException");
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(methodWithInteger),
                "Method with Integer processing info should throw ClassCastException");
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(methodWithObject),
                "Method with Object processing info should throw ClassCastException");
    }

    /**
     * Tests that isKept returns false for ProgramMethodOptimizationInfo.
     * ProgramMethodOptimizationInfo.isKept() returns false, unlike the base MethodOptimizationInfo.
     */
    @Test
    public void testIsKeptMethod_withProgramMethodOptimizationInfo_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.setProcessingInfo(new ProgramMethodOptimizationInfo(programClass, programMethod));

        // Act
        boolean result = KeepMarker.isKept(programMethod);

        // Assert
        assertFalse(result, "Method with ProgramMethodOptimizationInfo should not be kept " +
                "(ProgramMethodOptimizationInfo.isKept() returns false)");
    }

    /**
     * Tests the difference between MethodOptimizationInfo and ProgramMethodOptimizationInfo.
     * Verifies that isKept behaves differently based on the concrete type.
     */
    @Test
    public void testIsKeptMethod_differentOptimizationInfoTypes_returnDifferentResults() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod methodWithBaseInfo = new ProgramMethod();
        ProgramMethod methodWithProgramInfo = new ProgramMethod();

        methodWithBaseInfo.setProcessingInfo(new MethodOptimizationInfo());
        methodWithProgramInfo.setProcessingInfo(new ProgramMethodOptimizationInfo(programClass, methodWithProgramInfo));

        // Act
        boolean baseResult = KeepMarker.isKept(methodWithBaseInfo);
        boolean programResult = KeepMarker.isKept(methodWithProgramInfo);

        // Assert
        assertTrue(baseResult, "Base MethodOptimizationInfo should be kept");
        assertFalse(programResult, "ProgramMethodOptimizationInfo should not be kept");
        assertNotEquals(baseResult, programResult,
                "Different optimization info types should yield different results");
    }

    /**
     * Tests that isKept works with LibraryMethod instances.
     * Verifies the method works with different Method implementations.
     */
    @Test
    public void testIsKeptMethod_withLibraryMethod_worksCorrectly() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);

        // Act
        boolean result = KeepMarker.isKept(libraryMethod);

        // Assert
        assertTrue(result, "LibraryMethod with MethodOptimizationInfo should be kept");
    }

    /**
     * Tests that isKept works correctly after a LibraryMethod is visited by KeepMarker.
     * Verifies integration with the visitor pattern for library methods.
     */
    @Test
    public void testIsKeptMethod_afterKeepMarkerVisitLibraryMethod_returnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        KeepMarker keepMarker = new KeepMarker();

        // Act
        keepMarker.visitLibraryMethod(libraryClass, libraryMethod);
        boolean result = KeepMarker.isKept(libraryMethod);

        // Assert
        assertTrue(result, "LibraryMethod visited by KeepMarker should be kept");
    }

    // ==================== Tests for isKept(CodeAttribute) ====================

    /**
     * Tests that isKept returns true for a code attribute with CodeAttributeOptimizationInfo set.
     * This is the standard case where a code attribute has been visited by KeepMarker.
     */
    @Test
    public void testIsKeptCodeAttribute_withCodeAttributeOptimizationInfo_returnsTrue() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);

        // Act
        boolean result = KeepMarker.isKept(codeAttribute);

        // Assert
        assertTrue(result, "CodeAttribute with CodeAttributeOptimizationInfo should be kept");
    }

    /**
     * Tests that isKept returns false for a code attribute without any processing info.
     * When a code attribute hasn't been visited by KeepMarker, it has no processing info.
     */
    @Test
    public void testIsKeptCodeAttribute_withNullProcessingInfo_returnsFalse() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        assertNull(codeAttribute.getProcessingInfo(), "Processing info should be null");

        // Act
        boolean result = KeepMarker.isKept(codeAttribute);

        // Assert
        assertFalse(result, "CodeAttribute without processing info should not be kept");
    }

    /**
     * Tests that isKept throws ClassCastException when code attribute processing info is not CodeAttributeOptimizationInfo.
     * The method attempts to cast processing info to CodeAttributeOptimizationInfo, which will fail
     * if the code attribute has other types of processing info set.
     */
    @Test
    public void testIsKeptCodeAttribute_withNonCodeAttributeOptimizationInfoProcessingInfo_throwsClassCastException() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.setProcessingInfo("Some other processing info");

        // Act & Assert
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(codeAttribute),
                "isKept should throw ClassCastException with non-CodeAttributeOptimizationInfo processing info");
    }

    /**
     * Tests that isKept returns true for a code attribute visited by KeepMarker.
     * This verifies the integration with the visitor pattern.
     */
    @Test
    public void testIsKeptCodeAttribute_afterKeepMarkerVisit_returnsTrue() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        CodeAttribute codeAttribute = new CodeAttribute();
        KeepMarker keepMarker = new KeepMarker();

        // Act
        keepMarker.visitCodeAttribute(clazz, method, codeAttribute);
        boolean result = KeepMarker.isKept(codeAttribute);

        // Assert
        assertTrue(result, "CodeAttribute visited by KeepMarker should be kept");
    }

    /**
     * Tests that isKept returns true for multiple code attributes each with CodeAttributeOptimizationInfo.
     * Verifies that each code attribute's kept status is independent.
     */
    @Test
    public void testIsKeptCodeAttribute_multipleCodeAttributes_eachKeptIndependently() {
        // Arrange
        CodeAttribute codeAttribute1 = new CodeAttribute();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        CodeAttribute codeAttribute3 = new CodeAttribute();

        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute1);
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute2);
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute3);

        // Act & Assert
        assertTrue(KeepMarker.isKept(codeAttribute1), "First code attribute should be kept");
        assertTrue(KeepMarker.isKept(codeAttribute2), "Second code attribute should be kept");
        assertTrue(KeepMarker.isKept(codeAttribute3), "Third code attribute should be kept");
    }

    /**
     * Tests that isKept returns correct values for a mix of kept and non-kept code attributes.
     * Some code attributes have optimization info, others don't.
     */
    @Test
    public void testIsKeptCodeAttribute_mixedCodeAttributes_returnsCorrectStatusForEach() {
        // Arrange
        CodeAttribute keptCodeAttribute = new CodeAttribute();
        CodeAttribute notKeptCodeAttribute = new CodeAttribute();

        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(keptCodeAttribute);
        // notKeptCodeAttribute has no processing info

        // Act & Assert
        assertTrue(KeepMarker.isKept(keptCodeAttribute), "CodeAttribute with info should be kept");
        assertFalse(KeepMarker.isKept(notKeptCodeAttribute), "CodeAttribute without info should not be kept");
    }

    /**
     * Tests that isKept behavior is consistent across multiple calls for code attributes.
     * Calling isKept multiple times should return the same result.
     */
    @Test
    public void testIsKeptCodeAttribute_calledMultipleTimes_consistentResult() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);

        // Act
        boolean result1 = KeepMarker.isKept(codeAttribute);
        boolean result2 = KeepMarker.isKept(codeAttribute);
        boolean result3 = KeepMarker.isKept(codeAttribute);

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that isKept returns false consistently for a code attribute without optimization info.
     * Multiple calls should all return false.
     */
    @Test
    public void testIsKeptCodeAttribute_notKeptCodeAttribute_consistentlyReturnsFalse() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        boolean result1 = KeepMarker.isKept(codeAttribute);
        boolean result2 = KeepMarker.isKept(codeAttribute);
        boolean result3 = KeepMarker.isKept(codeAttribute);

        // Assert
        assertFalse(result1, "First call should return false");
        assertFalse(result2, "Second call should return false");
        assertFalse(result3, "Third call should return false");
    }

    /**
     * Tests that isKept reflects changes when CodeAttributeOptimizationInfo is added to a code attribute.
     * The kept status should change from false to true.
     */
    @Test
    public void testIsKeptCodeAttribute_beforeAndAfterSettingOptimizationInfo_changesStatus() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert - before setting optimization info
        assertFalse(KeepMarker.isKept(codeAttribute), "CodeAttribute should not be kept initially");

        // Act - set optimization info
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);

        // Assert - after setting optimization info
        assertTrue(KeepMarker.isKept(codeAttribute), "CodeAttribute should be kept after setting info");
    }

    /**
     * Tests that isKept works correctly after processing info is replaced.
     * Setting a new CodeAttributeOptimizationInfo should maintain kept status.
     */
    @Test
    public void testIsKeptCodeAttribute_afterReplacingOptimizationInfo_stillReturnsTrue() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act - set optimization info multiple times
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);
        assertTrue(KeepMarker.isKept(codeAttribute), "Should be kept after first set");

        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);
        assertTrue(KeepMarker.isKept(codeAttribute), "Should be kept after replacement");
    }

    /**
     * Tests that isKept handles a code attribute where processing info was removed.
     * Setting processing info to null should make the code attribute not kept.
     */
    @Test
    public void testIsKeptCodeAttribute_afterRemovingProcessingInfo_returnsFalse() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);
        assertTrue(KeepMarker.isKept(codeAttribute), "Should be kept initially");

        // Act - remove processing info
        codeAttribute.setProcessingInfo(null);

        // Assert
        assertFalse(KeepMarker.isKept(codeAttribute), "Should not be kept after removing info");
    }

    /**
     * Tests that isKept works with newly instantiated CodeAttribute objects.
     * Fresh code attribute instances should not be kept by default.
     */
    @Test
    public void testIsKeptCodeAttribute_withFreshCodeAttribute_returnsFalse() {
        // Arrange & Act
        CodeAttribute codeAttribute1 = new CodeAttribute();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        CodeAttribute codeAttribute3 = new CodeAttribute();

        // Assert
        assertFalse(KeepMarker.isKept(codeAttribute1), "Fresh code attribute 1 should not be kept");
        assertFalse(KeepMarker.isKept(codeAttribute2), "Fresh code attribute 2 should not be kept");
        assertFalse(KeepMarker.isKept(codeAttribute3), "Fresh code attribute 3 should not be kept");
    }

    /**
     * Tests that isKept correctly identifies kept code attributes in a batch scenario.
     * Simulates marking multiple code attributes and checking their status.
     */
    @Test
    public void testIsKeptCodeAttribute_batchProcessing_correctlyIdentifiesKeptCodeAttributes() {
        // Arrange
        CodeAttribute[] codeAttributes = new CodeAttribute[10];
        for (int i = 0; i < codeAttributes.length; i++) {
            codeAttributes[i] = new CodeAttribute();
        }

        // Mark only even-indexed code attributes
        for (int i = 0; i < codeAttributes.length; i += 2) {
            CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttributes[i]);
        }

        // Act & Assert
        for (int i = 0; i < codeAttributes.length; i++) {
            if (i % 2 == 0) {
                assertTrue(KeepMarker.isKept(codeAttributes[i]),
                        "CodeAttribute at index " + i + " should be kept");
            } else {
                assertFalse(KeepMarker.isKept(codeAttributes[i]),
                        "CodeAttribute at index " + i + " should not be kept");
            }
        }
    }

    /**
     * Tests that isKept does not throw exceptions with valid code attribute input.
     * The method should handle standard cases without throwing.
     */
    @Test
    public void testIsKeptCodeAttribute_withValidInput_doesNotThrowException() {
        // Arrange
        CodeAttribute keptCodeAttribute = new CodeAttribute();
        CodeAttribute notKeptCodeAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(keptCodeAttribute);

        // Act & Assert
        assertDoesNotThrow(() -> KeepMarker.isKept(keptCodeAttribute),
                "isKept with kept code attribute should not throw");
        assertDoesNotThrow(() -> KeepMarker.isKept(notKeptCodeAttribute),
                "isKept with non-kept code attribute should not throw");
    }

    /**
     * Tests that isKept is consistent with direct CodeAttributeOptimizationInfo checks.
     * The static method should match what we'd get from directly checking the info.
     */
    @Test
    public void testIsKeptCodeAttribute_consistentWithDirectInfoCheck() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);

        // Act
        boolean staticMethodResult = KeepMarker.isKept(codeAttribute);
        CodeAttributeOptimizationInfo info = CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute);
        boolean directCheckResult = (info != null && info.isKept());

        // Assert
        assertEquals(directCheckResult, staticMethodResult,
                "Static method should match direct info check");
    }

    /**
     * Tests that isKept works correctly with base CodeAttributeOptimizationInfo.
     * The base class should return true for isKept.
     */
    @Test
    public void testIsKeptCodeAttribute_withBaseCodeAttributeOptimizationInfo_returnsTrue() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.setProcessingInfo(new CodeAttributeOptimizationInfo());

        // Act
        boolean result = KeepMarker.isKept(codeAttribute);

        // Assert
        assertTrue(result, "CodeAttribute with base CodeAttributeOptimizationInfo should be kept");
    }

    /**
     * Tests isKept behavior when processing info is set to various non-CodeAttributeOptimizationInfo types.
     * These should throw ClassCastException due to the unchecked cast in getCodeAttributeOptimizationInfo.
     */
    @Test
    public void testIsKeptCodeAttribute_withVariousNonOptimizationInfoTypes_throwsClassCastException() {
        // Arrange
        CodeAttribute codeAttributeWithString = new CodeAttribute();
        CodeAttribute codeAttributeWithInteger = new CodeAttribute();
        CodeAttribute codeAttributeWithObject = new CodeAttribute();

        codeAttributeWithString.setProcessingInfo("test");
        codeAttributeWithInteger.setProcessingInfo(42);
        codeAttributeWithObject.setProcessingInfo(new Object());

        // Act & Assert
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(codeAttributeWithString),
                "CodeAttribute with String processing info should throw ClassCastException");
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(codeAttributeWithInteger),
                "CodeAttribute with Integer processing info should throw ClassCastException");
        assertThrows(ClassCastException.class,
                () -> KeepMarker.isKept(codeAttributeWithObject),
                "CodeAttribute with Object processing info should throw ClassCastException");
    }

    /**
     * Tests that CodeAttributeOptimizationInfo always returns true for isKept.
     * Unlike other optimization info classes, there is no variant that returns false.
     */
    @Test
    public void testIsKeptCodeAttribute_alwaysReturnsTrueWhenInfoPresent() {
        // Arrange
        CodeAttribute codeAttribute1 = new CodeAttribute();
        CodeAttribute codeAttribute2 = new CodeAttribute();
        CodeAttribute codeAttribute3 = new CodeAttribute();

        codeAttribute1.setProcessingInfo(new CodeAttributeOptimizationInfo());
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute2);

        // Create another instance
        CodeAttributeOptimizationInfo info = new CodeAttributeOptimizationInfo();
        codeAttribute3.setProcessingInfo(info);

        // Act & Assert
        assertTrue(KeepMarker.isKept(codeAttribute1), "CodeAttribute with direct info should be kept");
        assertTrue(KeepMarker.isKept(codeAttribute2), "CodeAttribute with static setter info should be kept");
        assertTrue(KeepMarker.isKept(codeAttribute3), "CodeAttribute with separate instance should be kept");
    }

    /**
     * Tests that isKept can handle code attributes that have been marked multiple times.
     * Re-visiting a code attribute should not affect its kept status.
     */
    @Test
    public void testIsKeptCodeAttribute_markedMultipleTimes_remainsKept() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        CodeAttribute codeAttribute = new CodeAttribute();
        KeepMarker keepMarker = new KeepMarker();

        // Act - mark the code attribute multiple times
        keepMarker.visitCodeAttribute(clazz, method, codeAttribute);
        boolean result1 = KeepMarker.isKept(codeAttribute);

        keepMarker.visitCodeAttribute(clazz, method, codeAttribute);
        boolean result2 = KeepMarker.isKept(codeAttribute);

        keepMarker.visitCodeAttribute(clazz, method, codeAttribute);
        boolean result3 = KeepMarker.isKept(codeAttribute);

        // Assert
        assertTrue(result1, "CodeAttribute should be kept after first visit");
        assertTrue(result2, "CodeAttribute should be kept after second visit");
        assertTrue(result3, "CodeAttribute should be kept after third visit");
    }

    /**
     * Tests that isKept handles rapid sequential calls efficiently.
     * The method should be able to handle many consecutive calls.
     */
    @Test
    public void testIsKeptCodeAttribute_rapidSequentialCalls_handlesEfficiently() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);

        // Act & Assert - all calls should return true and not throw
        for (int i = 0; i < 1000; i++) {
            assertTrue(KeepMarker.isKept(codeAttribute),
                    "Call " + i + " should return true");
        }
    }
}

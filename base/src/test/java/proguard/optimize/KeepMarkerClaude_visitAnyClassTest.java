package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.optimize.info.ClassOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KeepMarker#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in KeepMarker sets ClassOptimizationInfo on the visited class.
 * This marks the class as kept during optimization, ensuring it remains unchanged.
 *
 * The method calls ClassOptimizationInfo.setClassOptimizationInfo(clazz), which:
 * 1. Creates a new ClassOptimizationInfo instance
 * 2. Sets it as the processing info on the class via clazz.setProcessingInfo()
 *
 * These tests verify that:
 * 1. The method correctly sets ClassOptimizationInfo on the class
 * 2. The processing info can be retrieved after being set
 * 3. The method handles multiple invocations correctly
 * 4. The method handles different class types appropriately
 */
public class KeepMarkerClaude_visitAnyClassTest {

    private KeepMarker keepMarker;

    @BeforeEach
    public void setUp() {
        keepMarker = new KeepMarker();
    }

    /**
     * Tests that visitAnyClass sets ClassOptimizationInfo on a ProgramClass.
     * Verifies the core functionality - the class should have optimization info set after visiting.
     */
    @Test
    public void testVisitAnyClass_withProgramClass_setsClassOptimizationInfo() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        assertNull(programClass.getProcessingInfo(), "Processing info should be null before visiting");

        // Act
        keepMarker.visitAnyClass(programClass);

        // Assert
        Object processingInfo = programClass.getProcessingInfo();
        assertNotNull(processingInfo, "Processing info should be set after visiting");
        assertInstanceOf(ClassOptimizationInfo.class, processingInfo,
                "Processing info should be an instance of ClassOptimizationInfo");
    }

    /**
     * Tests that the ClassOptimizationInfo can be retrieved and used after being set.
     * Verifies that the info object is properly configured.
     */
    @Test
    public void testVisitAnyClass_classOptimizationInfoIsUsable() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act
        keepMarker.visitAnyClass(programClass);

        // Assert
        ClassOptimizationInfo info = ClassOptimizationInfo.getClassOptimizationInfo(programClass);
        assertNotNull(info, "ClassOptimizationInfo should be retrievable");
        assertTrue(info.isKept(), "Class should be marked as kept");
    }

    /**
     * Tests that visitAnyClass can be called multiple times on the same class.
     * Each call replaces the previous ClassOptimizationInfo.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_replacesOptimizationInfo() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - call visitAnyClass multiple times
        keepMarker.visitAnyClass(programClass);
        ClassOptimizationInfo firstInfo = ClassOptimizationInfo.getClassOptimizationInfo(programClass);

        keepMarker.visitAnyClass(programClass);
        ClassOptimizationInfo secondInfo = ClassOptimizationInfo.getClassOptimizationInfo(programClass);

        keepMarker.visitAnyClass(programClass);
        ClassOptimizationInfo thirdInfo = ClassOptimizationInfo.getClassOptimizationInfo(programClass);

        // Assert - each call creates a new instance
        assertNotNull(firstInfo, "First info should not be null");
        assertNotNull(secondInfo, "Second info should not be null");
        assertNotNull(thirdInfo, "Third info should not be null");
        assertNotSame(firstInfo, secondInfo, "Second call should create a new info instance");
        assertNotSame(secondInfo, thirdInfo, "Third call should create a new info instance");
    }

    /**
     * Tests that visitAnyClass can be called on multiple different classes.
     * Verifies that each class gets its own ClassOptimizationInfo instance.
     */
    @Test
    public void testVisitAnyClass_multipleClasses_eachGetsOwnOptimizationInfo() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        // Act
        keepMarker.visitAnyClass(class1);
        keepMarker.visitAnyClass(class2);
        keepMarker.visitAnyClass(class3);

        // Assert
        ClassOptimizationInfo info1 = ClassOptimizationInfo.getClassOptimizationInfo(class1);
        ClassOptimizationInfo info2 = ClassOptimizationInfo.getClassOptimizationInfo(class2);
        ClassOptimizationInfo info3 = ClassOptimizationInfo.getClassOptimizationInfo(class3);

        assertNotNull(info1, "First class should have optimization info");
        assertNotNull(info2, "Second class should have optimization info");
        assertNotNull(info3, "Third class should have optimization info");

        assertNotSame(info1, info2, "Each class should have its own info instance");
        assertNotSame(info2, info3, "Each class should have its own info instance");
        assertNotSame(info1, info3, "Each class should have its own info instance");
    }

    /**
     * Tests that visitAnyClass works correctly with a mock Clazz.
     * Verifies that the method interacts correctly with the Clazz interface.
     */
    @Test
    public void testVisitAnyClass_withMockClazz_setsProcessingInfo() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);

        // Act
        keepMarker.visitAnyClass(mockClazz);

        // Assert - verify setProcessingInfo was called with a ClassOptimizationInfo instance
        verify(mockClazz, times(1)).setProcessingInfo(any(ClassOptimizationInfo.class));
    }

    /**
     * Tests that visitAnyClass throws NullPointerException with null Clazz.
     * The method should not handle null gracefully as it needs to set processing info.
     */
    @Test
    public void testVisitAnyClass_withNullClazz_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> keepMarker.visitAnyClass(null),
                "visitAnyClass with null should throw NullPointerException");
    }

    /**
     * Tests that visitAnyClass can be used by multiple KeepMarker instances.
     * Verifies that different marker instances behave consistently.
     */
    @Test
    public void testVisitAnyClass_multipleKeepMarkerInstances_allSetOptimizationInfo() {
        // Arrange
        KeepMarker marker1 = new KeepMarker();
        KeepMarker marker2 = new KeepMarker();
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        // Act
        marker1.visitAnyClass(class1);
        marker2.visitAnyClass(class2);

        // Assert
        assertNotNull(ClassOptimizationInfo.getClassOptimizationInfo(class1),
                "First class should have optimization info");
        assertNotNull(ClassOptimizationInfo.getClassOptimizationInfo(class2),
                "Second class should have optimization info");
    }

    /**
     * Tests that visitAnyClass replaces existing processing info.
     * If the class already has processing info, it should be replaced.
     */
    @Test
    public void testVisitAnyClass_replacesExistingProcessingInfo() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        Object existingInfo = new Object();
        programClass.setProcessingInfo(existingInfo);

        // Act
        keepMarker.visitAnyClass(programClass);

        // Assert
        Object newInfo = programClass.getProcessingInfo();
        assertNotNull(newInfo, "New processing info should be set");
        assertNotSame(existingInfo, newInfo, "New info should replace existing info");
        assertInstanceOf(ClassOptimizationInfo.class, newInfo,
                "New info should be ClassOptimizationInfo");
    }

    /**
     * Tests that visitAnyClass does not throw any exceptions with valid input.
     * Verifies basic exception-free operation.
     */
    @Test
    public void testVisitAnyClass_withValidClass_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> keepMarker.visitAnyClass(programClass),
                "visitAnyClass should not throw exception with valid class");
    }

    /**
     * Tests that the ClassOptimizationInfo set by visitAnyClass has expected default values.
     * Verifies that the info object is properly initialized.
     */
    @Test
    public void testVisitAnyClass_setsClassOptimizationInfoWithCorrectDefaults() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act
        keepMarker.visitAnyClass(programClass);

        // Assert
        ClassOptimizationInfo info = ClassOptimizationInfo.getClassOptimizationInfo(programClass);
        assertTrue(info.isKept(), "Class should be kept");
        assertTrue(info.containsConstructors(), "Should report containing constructors");
        assertTrue(info.isInstantiated(), "Should report being instantiated");
        assertTrue(info.isCaught(), "Should report being caught");
        assertTrue(info.isEscaping(), "Should report instances escaping");
        assertTrue(info.containsPackageVisibleMembers(),
                "Should report containing package visible members");
        assertTrue(info.invokesPackageVisibleMembers(),
                "Should report invoking package visible members");
    }

    /**
     * Tests that visitAnyClass can be called in rapid succession.
     * Verifies consistent behavior under rapid sequential calls.
     */
    @Test
    public void testVisitAnyClass_rapidSequentialCalls_consistentBehavior() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - all calls should succeed
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() -> keepMarker.visitAnyClass(programClass),
                    "Call " + i + " should not throw exception");
        }

        // Verify final state
        assertNotNull(ClassOptimizationInfo.getClassOptimizationInfo(programClass),
                "Class should still have optimization info after many calls");
    }

    /**
     * Tests that visitAnyClass works with a class that has a name.
     * Verifies the method works with more realistic class instances.
     */
    @Test
    public void testVisitAnyClass_withNamedClass_setsOptimizationInfo() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1; // Set a class index

        // Act
        keepMarker.visitAnyClass(programClass);

        // Assert
        assertNotNull(ClassOptimizationInfo.getClassOptimizationInfo(programClass),
                "Named class should have optimization info set");
    }

    /**
     * Tests that the method can be used in a visitor pattern context.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitAnyClass_usedAsClassVisitor_setsOptimizationInfo() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        proguard.classfile.visitor.ClassVisitor visitor = keepMarker;

        // Act
        visitor.visitAnyClass(programClass);

        // Assert
        assertNotNull(ClassOptimizationInfo.getClassOptimizationInfo(programClass),
                "Class should have optimization info when visited through ClassVisitor interface");
    }

    /**
     * Tests that visitAnyClass creates independent optimization info for each class.
     * Modifying one class's info should not affect another class's info.
     */
    @Test
    public void testVisitAnyClass_createsIndependentOptimizationInfo() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        // Act
        keepMarker.visitAnyClass(class1);
        keepMarker.visitAnyClass(class2);

        ClassOptimizationInfo info1 = ClassOptimizationInfo.getClassOptimizationInfo(class1);
        ClassOptimizationInfo info2 = ClassOptimizationInfo.getClassOptimizationInfo(class2);

        // Modify info1
        info1.setNoSideEffects();

        // Assert - info2 should not be affected
        assertTrue(info1.hasNoSideEffects(), "First class info should have no side effects");
        assertFalse(info2.hasNoSideEffects(), "Second class info should not be affected");
    }

    /**
     * Tests that visitAnyClass works correctly after the KeepMarker has visited other types.
     * Verifies that the method maintains consistent behavior across different visitor method calls.
     */
    @Test
    public void testVisitAnyClass_afterOtherVisitorMethods_stillSetsOptimizationInfo() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();
        proguard.classfile.ProgramField programField = mock(proguard.classfile.ProgramField.class);

        // Act - call other visitor methods first
        keepMarker.visitProgramField(programClass, programField);
        keepMarker.visitAnyClass(programClass2);

        // Assert
        assertNotNull(ClassOptimizationInfo.getClassOptimizationInfo(programClass2),
                "Class should have optimization info even after other visitor methods were called");
    }
}

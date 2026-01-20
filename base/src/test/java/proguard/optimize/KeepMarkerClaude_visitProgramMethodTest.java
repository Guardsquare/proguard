package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.optimize.info.MethodOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KeepMarker#visitProgramMethod(ProgramClass, ProgramMethod)}.
 *
 * The visitProgramMethod method in KeepMarker sets MethodOptimizationInfo on the visited method.
 * This marks the method as kept during optimization, ensuring it remains unchanged.
 *
 * The method calls MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod), which:
 * 1. Uses MethodLinker.lastMember(method) to get the last method in a linked chain
 * 2. Creates a new MethodOptimizationInfo instance
 * 3. Sets it as the processing info on the method via method.setProcessingInfo()
 *
 * These tests verify that:
 * 1. The method correctly sets MethodOptimizationInfo on the method
 * 2. The processing info can be retrieved after being set
 * 3. The method handles multiple invocations correctly
 * 4. The method handles different method and class combinations appropriately
 */
public class KeepMarkerClaude_visitProgramMethodTest {

    private KeepMarker keepMarker;
    private ProgramClass programClass;

    @BeforeEach
    public void setUp() {
        keepMarker = new KeepMarker();
        programClass = new ProgramClass();
    }

    /**
     * Tests that visitProgramMethod sets MethodOptimizationInfo on a ProgramMethod.
     * Verifies the core functionality - the method should have optimization info set after visiting.
     */
    @Test
    public void testVisitProgramMethod_withValidMethod_setsMethodOptimizationInfo() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        assertNull(programMethod.getProcessingInfo(), "Processing info should be null before visiting");

        // Act
        keepMarker.visitProgramMethod(programClass, programMethod);

        // Assert
        Object processingInfo = programMethod.getProcessingInfo();
        assertNotNull(processingInfo, "Processing info should be set after visiting");
        assertInstanceOf(MethodOptimizationInfo.class, processingInfo,
                "Processing info should be an instance of MethodOptimizationInfo");
    }

    /**
     * Tests that the MethodOptimizationInfo can be retrieved and used after being set.
     * Verifies that the info object is properly configured.
     */
    @Test
    public void testVisitProgramMethod_methodOptimizationInfoIsUsable() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Act
        keepMarker.visitProgramMethod(programClass, programMethod);

        // Assert
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertNotNull(info, "MethodOptimizationInfo should be retrievable");
        assertTrue(info.isKept(), "Method should be marked as kept");
    }

    /**
     * Tests that visitProgramMethod can be called multiple times on the same method.
     * Each call replaces the previous MethodOptimizationInfo.
     */
    @Test
    public void testVisitProgramMethod_calledMultipleTimes_replacesOptimizationInfo() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Act - call visitProgramMethod multiple times
        keepMarker.visitProgramMethod(programClass, programMethod);
        MethodOptimizationInfo firstInfo = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);

        keepMarker.visitProgramMethod(programClass, programMethod);
        MethodOptimizationInfo secondInfo = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);

        keepMarker.visitProgramMethod(programClass, programMethod);
        MethodOptimizationInfo thirdInfo = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);

        // Assert - each call creates a new instance
        assertNotNull(firstInfo, "First info should not be null");
        assertNotNull(secondInfo, "Second info should not be null");
        assertNotNull(thirdInfo, "Third info should not be null");
        assertNotSame(firstInfo, secondInfo, "Second call should create a new info instance");
        assertNotSame(secondInfo, thirdInfo, "Third call should create a new info instance");
    }

    /**
     * Tests that visitProgramMethod can be called on multiple different methods.
     * Verifies that each method gets its own MethodOptimizationInfo instance.
     */
    @Test
    public void testVisitProgramMethod_multipleMethods_eachGetsOwnOptimizationInfo() {
        // Arrange
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramMethod method3 = new ProgramMethod();

        // Act
        keepMarker.visitProgramMethod(programClass, method1);
        keepMarker.visitProgramMethod(programClass, method2);
        keepMarker.visitProgramMethod(programClass, method3);

        // Assert
        MethodOptimizationInfo info1 = MethodOptimizationInfo.getMethodOptimizationInfo(method1);
        MethodOptimizationInfo info2 = MethodOptimizationInfo.getMethodOptimizationInfo(method2);
        MethodOptimizationInfo info3 = MethodOptimizationInfo.getMethodOptimizationInfo(method3);

        assertNotNull(info1, "First method should have optimization info");
        assertNotNull(info2, "Second method should have optimization info");
        assertNotNull(info3, "Third method should have optimization info");

        assertNotSame(info1, info2, "Each method should have its own info instance");
        assertNotSame(info2, info3, "Each method should have its own info instance");
        assertNotSame(info1, info3, "Each method should have its own info instance");
    }

    /**
     * Tests that visitProgramMethod works correctly with mock objects.
     * Verifies that the method interacts correctly with the Method interface.
     */
    @Test
    public void testVisitProgramMethod_withMockMethod_setsProcessingInfo() {
        // Arrange
        ProgramMethod mockMethod = mock(ProgramMethod.class);

        // Act
        keepMarker.visitProgramMethod(programClass, mockMethod);

        // Assert - verify setProcessingInfo was called with a MethodOptimizationInfo instance
        verify(mockMethod, times(1)).setProcessingInfo(any(MethodOptimizationInfo.class));
    }

    /**
     * Tests that visitProgramMethod throws NullPointerException with null method.
     * The method should not handle null gracefully as it needs to set processing info.
     */
    @Test
    public void testVisitProgramMethod_withNullMethod_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> keepMarker.visitProgramMethod(programClass, null),
                "visitProgramMethod with null method should throw NullPointerException");
    }

    /**
     * Tests that visitProgramMethod can handle null ProgramClass.
     * The class parameter is passed but not used in the method, so null should be acceptable.
     */
    @Test
    public void testVisitProgramMethod_withNullClass_stillSetsOptimizationInfo() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Act
        keepMarker.visitProgramMethod(null, programMethod);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(programMethod),
                "Method should have optimization info even with null class");
    }

    /**
     * Tests that visitProgramMethod can be used by multiple KeepMarker instances.
     * Verifies that different marker instances behave consistently.
     */
    @Test
    public void testVisitProgramMethod_multipleKeepMarkerInstances_allSetOptimizationInfo() {
        // Arrange
        KeepMarker marker1 = new KeepMarker();
        KeepMarker marker2 = new KeepMarker();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();

        // Act
        marker1.visitProgramMethod(programClass, method1);
        marker2.visitProgramMethod(programClass, method2);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(method1),
                "First method should have optimization info");
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(method2),
                "Second method should have optimization info");
    }

    /**
     * Tests that visitProgramMethod replaces existing processing info.
     * If the method already has processing info, it should be replaced.
     */
    @Test
    public void testVisitProgramMethod_replacesExistingProcessingInfo() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        Object existingInfo = new Object();
        programMethod.setProcessingInfo(existingInfo);

        // Act
        keepMarker.visitProgramMethod(programClass, programMethod);

        // Assert
        Object newInfo = programMethod.getProcessingInfo();
        assertNotNull(newInfo, "New processing info should be set");
        assertNotSame(existingInfo, newInfo, "New info should replace existing info");
        assertInstanceOf(MethodOptimizationInfo.class, newInfo,
                "New info should be MethodOptimizationInfo");
    }

    /**
     * Tests that visitProgramMethod does not throw any exceptions with valid input.
     * Verifies basic exception-free operation.
     */
    @Test
    public void testVisitProgramMethod_withValidInput_doesNotThrowException() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert
        assertDoesNotThrow(() -> keepMarker.visitProgramMethod(programClass, programMethod),
                "visitProgramMethod should not throw exception with valid input");
    }

    /**
     * Tests that the MethodOptimizationInfo set by visitProgramMethod has expected default values.
     * Verifies that the info object is properly initialized.
     */
    @Test
    public void testVisitProgramMethod_setsMethodOptimizationInfoWithCorrectDefaults() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Act
        keepMarker.visitProgramMethod(programClass, programMethod);

        // Assert
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertTrue(info.isKept(), "Method should be kept");
        assertFalse(info.hasNoSideEffects(), "Should report having side effects by default");
        assertFalse(info.hasNoExternalSideEffects(), "Should report having external side effects by default");
        assertFalse(info.hasNoEscapingParameters(), "Should report having escaping parameters by default");
        assertTrue(info.hasSideEffects(), "Should report having side effects");
    }

    /**
     * Tests that visitProgramMethod can be called in rapid succession.
     * Verifies consistent behavior under rapid sequential calls.
     */
    @Test
    public void testVisitProgramMethod_rapidSequentialCalls_consistentBehavior() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert - all calls should succeed
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() -> keepMarker.visitProgramMethod(programClass, programMethod),
                    "Call " + i + " should not throw exception");
        }

        // Verify final state
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(programMethod),
                "Method should still have optimization info after many calls");
    }

    /**
     * Tests that visitProgramMethod works with methods that have names.
     * Verifies the method works with more realistic method instances.
     */
    @Test
    public void testVisitProgramMethod_withNamedMethod_setsOptimizationInfo() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2nameIndex = 1; // Set a name index

        // Act
        keepMarker.visitProgramMethod(programClass, programMethod);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(programMethod),
                "Named method should have optimization info set");
    }

    /**
     * Tests that visitProgramMethod works with methods that have descriptors.
     * Verifies the method works with more realistic method instances.
     */
    @Test
    public void testVisitProgramMethod_withMethodDescriptor_setsOptimizationInfo() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2descriptorIndex = 1; // Set a descriptor index

        // Act
        keepMarker.visitProgramMethod(programClass, programMethod);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(programMethod),
                "Method with descriptor should have optimization info set");
    }

    /**
     * Tests that the method can be used in a visitor pattern context.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitProgramMethod_usedAsMemberVisitor_setsOptimizationInfo() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        proguard.classfile.visitor.MemberVisitor visitor = keepMarker;

        // Act
        visitor.visitProgramMethod(programClass, programMethod);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(programMethod),
                "Method should have optimization info when visited through MemberVisitor interface");
    }

    /**
     * Tests that visitProgramMethod creates independent optimization info for each method.
     * Modifying one method's info should not affect another method's info.
     */
    @Test
    public void testVisitProgramMethod_createsIndependentOptimizationInfo() {
        // Arrange
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();

        // Act
        keepMarker.visitProgramMethod(programClass, method1);
        keepMarker.visitProgramMethod(programClass, method2);

        MethodOptimizationInfo info1 = MethodOptimizationInfo.getMethodOptimizationInfo(method1);
        MethodOptimizationInfo info2 = MethodOptimizationInfo.getMethodOptimizationInfo(method2);

        // Modify info1
        info1.setNoSideEffects();

        // Assert - info2 should not be affected
        assertTrue(info1.hasNoSideEffects(), "First method info should have no side effects");
        assertFalse(info2.hasNoSideEffects(), "Second method info should not be affected");
    }

    /**
     * Tests that visitProgramMethod works correctly with different ProgramClass instances.
     * Verifies that the class parameter doesn't affect the method optimization info setting.
     */
    @Test
    public void testVisitProgramMethod_withDifferentClasses_setsOptimizationInfo() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();

        // Act
        keepMarker.visitProgramMethod(class1, method1);
        keepMarker.visitProgramMethod(class2, method2);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(method1),
                "Method from first class should have optimization info");
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(method2),
                "Method from second class should have optimization info");
    }

    /**
     * Tests that visitProgramMethod works correctly after the KeepMarker has visited other types.
     * Verifies that the method maintains consistent behavior across different visitor method calls.
     */
    @Test
    public void testVisitProgramMethod_afterOtherVisitorMethods_stillSetsOptimizationInfo() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        ProgramClass testClass = new ProgramClass();
        proguard.classfile.ProgramField programField = new proguard.classfile.ProgramField();

        // Act - call other visitor methods first
        keepMarker.visitAnyClass(testClass);
        keepMarker.visitProgramField(testClass, programField);
        keepMarker.visitProgramMethod(programClass, programMethod);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(programMethod),
                "Method should have optimization info even after other visitor methods were called");
    }

    /**
     * Tests that visitProgramMethod can handle methods with access flags set.
     * Verifies the method works with methods that have various access modifiers.
     */
    @Test
    public void testVisitProgramMethod_withAccessFlags_setsOptimizationInfo() {
        // Arrange
        ProgramMethod publicMethod = new ProgramMethod();
        publicMethod.u2accessFlags = 0x0001; // ACC_PUBLIC

        ProgramMethod privateMethod = new ProgramMethod();
        privateMethod.u2accessFlags = 0x0002; // ACC_PRIVATE

        ProgramMethod staticMethod = new ProgramMethod();
        staticMethod.u2accessFlags = 0x0008; // ACC_STATIC

        ProgramMethod synchronizedMethod = new ProgramMethod();
        synchronizedMethod.u2accessFlags = 0x0020; // ACC_SYNCHRONIZED

        // Act
        keepMarker.visitProgramMethod(programClass, publicMethod);
        keepMarker.visitProgramMethod(programClass, privateMethod);
        keepMarker.visitProgramMethod(programClass, staticMethod);
        keepMarker.visitProgramMethod(programClass, synchronizedMethod);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(publicMethod),
                "Public method should have optimization info");
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(privateMethod),
                "Private method should have optimization info");
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(staticMethod),
                "Static method should have optimization info");
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(synchronizedMethod),
                "Synchronized method should have optimization info");
    }

    /**
     * Tests that visitProgramMethod handles the same method being visited from different classes.
     * Verifies that the method's optimization info is updated regardless of which class visits it.
     */
    @Test
    public void testVisitProgramMethod_sameMethodDifferentClasses_updatesOptimizationInfo() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act
        keepMarker.visitProgramMethod(class1, programMethod);
        MethodOptimizationInfo info1 = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);

        keepMarker.visitProgramMethod(class2, programMethod);
        MethodOptimizationInfo info2 = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);

        // Assert
        assertNotNull(info1, "Method should have optimization info after first visit");
        assertNotNull(info2, "Method should have optimization info after second visit");
        assertNotSame(info1, info2, "Second visit should create new optimization info");
    }

    /**
     * Tests that visitProgramMethod does not modify the ProgramClass parameter.
     * The class should remain unchanged after the method call.
     */
    @Test
    public void testVisitProgramMethod_doesNotModifyClass() {
        // Arrange
        ProgramClass testClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        Object classProcessingInfo = new Object();
        testClass.setProcessingInfo(classProcessingInfo);

        // Act
        keepMarker.visitProgramMethod(testClass, programMethod);

        // Assert
        assertSame(classProcessingInfo, testClass.getProcessingInfo(),
                "Class processing info should not be modified");
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(programMethod),
                "Method should have optimization info");
    }

    /**
     * Tests that visitProgramMethod works with constructor methods.
     * Verifies the method handles special methods like constructors.
     */
    @Test
    public void testVisitProgramMethod_withConstructor_setsOptimizationInfo() {
        // Arrange
        ProgramMethod constructor = new ProgramMethod();
        // Constructors are typically named "<init>" but we just set a flag to simulate
        constructor.u2nameIndex = 1;

        // Act
        keepMarker.visitProgramMethod(programClass, constructor);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(constructor),
                "Constructor should have optimization info set");
    }

    /**
     * Tests that visitProgramMethod works with static initializer methods.
     * Verifies the method handles special methods like static initializers.
     */
    @Test
    public void testVisitProgramMethod_withStaticInitializer_setsOptimizationInfo() {
        // Arrange
        ProgramMethod staticInitializer = new ProgramMethod();
        // Static initializers are typically named "<clinit>" but we just set flags
        staticInitializer.u2nameIndex = 1;
        staticInitializer.u2accessFlags = 0x0008; // ACC_STATIC

        // Act
        keepMarker.visitProgramMethod(programClass, staticInitializer);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(staticInitializer),
                "Static initializer should have optimization info set");
    }
}

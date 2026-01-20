package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.optimize.info.MethodOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KeepMarker#visitLibraryMethod(LibraryClass, LibraryMethod)}.
 *
 * The visitLibraryMethod method in KeepMarker sets MethodOptimizationInfo on the visited library method.
 * This marks the method as kept during optimization, ensuring it remains unchanged.
 *
 * The method calls MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod), which:
 * 1. Uses MethodLinker.lastMember(method) to get the last method in a linked chain
 * 2. Creates a new MethodOptimizationInfo instance
 * 3. Sets it as the processing info on the method via method.setProcessingInfo()
 *
 * These tests verify that:
 * 1. The method correctly sets MethodOptimizationInfo on the library method
 * 2. The processing info can be retrieved after being set
 * 3. The method handles multiple invocations correctly
 * 4. The method handles different method and class combinations appropriately
 */
public class KeepMarkerClaude_visitLibraryMethodTest {

    private KeepMarker keepMarker;
    private LibraryClass libraryClass;

    @BeforeEach
    public void setUp() {
        keepMarker = new KeepMarker();
        libraryClass = new LibraryClass();
    }

    /**
     * Tests that visitLibraryMethod sets MethodOptimizationInfo on a LibraryMethod.
     * Verifies the core functionality - the method should have optimization info set after visiting.
     */
    @Test
    public void testVisitLibraryMethod_withValidMethod_setsMethodOptimizationInfo() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();
        assertNull(libraryMethod.getProcessingInfo(), "Processing info should be null before visiting");

        // Act
        keepMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        Object processingInfo = libraryMethod.getProcessingInfo();
        assertNotNull(processingInfo, "Processing info should be set after visiting");
        assertInstanceOf(MethodOptimizationInfo.class, processingInfo,
                "Processing info should be an instance of MethodOptimizationInfo");
    }

    /**
     * Tests that the MethodOptimizationInfo can be retrieved and used after being set.
     * Verifies that the info object is properly configured.
     */
    @Test
    public void testVisitLibraryMethod_methodOptimizationInfoIsUsable() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();

        // Act
        keepMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod);
        assertNotNull(info, "MethodOptimizationInfo should be retrievable");
        assertTrue(info.isKept(), "Method should be marked as kept");
    }

    /**
     * Tests that visitLibraryMethod can be called multiple times on the same method.
     * Each call replaces the previous MethodOptimizationInfo.
     */
    @Test
    public void testVisitLibraryMethod_calledMultipleTimes_replacesOptimizationInfo() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();

        // Act - call visitLibraryMethod multiple times
        keepMarker.visitLibraryMethod(libraryClass, libraryMethod);
        MethodOptimizationInfo firstInfo = MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod);

        keepMarker.visitLibraryMethod(libraryClass, libraryMethod);
        MethodOptimizationInfo secondInfo = MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod);

        keepMarker.visitLibraryMethod(libraryClass, libraryMethod);
        MethodOptimizationInfo thirdInfo = MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod);

        // Assert - each call creates a new instance
        assertNotNull(firstInfo, "First info should not be null");
        assertNotNull(secondInfo, "Second info should not be null");
        assertNotNull(thirdInfo, "Third info should not be null");
        assertNotSame(firstInfo, secondInfo, "Second call should create a new info instance");
        assertNotSame(secondInfo, thirdInfo, "Third call should create a new info instance");
    }

    /**
     * Tests that visitLibraryMethod can be called on multiple different methods.
     * Verifies that each method gets its own MethodOptimizationInfo instance.
     */
    @Test
    public void testVisitLibraryMethod_multipleMethods_eachGetsOwnOptimizationInfo() {
        // Arrange
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();
        LibraryMethod method3 = new LibraryMethod();

        // Act
        keepMarker.visitLibraryMethod(libraryClass, method1);
        keepMarker.visitLibraryMethod(libraryClass, method2);
        keepMarker.visitLibraryMethod(libraryClass, method3);

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
     * Tests that visitLibraryMethod works correctly with mock objects.
     * Verifies that the method interacts correctly with the Method interface.
     */
    @Test
    public void testVisitLibraryMethod_withMockMethod_setsProcessingInfo() {
        // Arrange
        LibraryMethod mockMethod = mock(LibraryMethod.class);

        // Act
        keepMarker.visitLibraryMethod(libraryClass, mockMethod);

        // Assert - verify setProcessingInfo was called with a MethodOptimizationInfo instance
        verify(mockMethod, times(1)).setProcessingInfo(any(MethodOptimizationInfo.class));
    }

    /**
     * Tests that visitLibraryMethod throws NullPointerException with null method.
     * The method should not handle null gracefully as it needs to set processing info.
     */
    @Test
    public void testVisitLibraryMethod_withNullMethod_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> keepMarker.visitLibraryMethod(libraryClass, null),
                "visitLibraryMethod with null method should throw NullPointerException");
    }

    /**
     * Tests that visitLibraryMethod can handle null LibraryClass.
     * The class parameter is passed but not used in the method, so null should be acceptable.
     */
    @Test
    public void testVisitLibraryMethod_withNullClass_stillSetsOptimizationInfo() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();

        // Act
        keepMarker.visitLibraryMethod(null, libraryMethod);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod),
                "Method should have optimization info even with null class");
    }

    /**
     * Tests that visitLibraryMethod can be used by multiple KeepMarker instances.
     * Verifies that different marker instances behave consistently.
     */
    @Test
    public void testVisitLibraryMethod_multipleKeepMarkerInstances_allSetOptimizationInfo() {
        // Arrange
        KeepMarker marker1 = new KeepMarker();
        KeepMarker marker2 = new KeepMarker();
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();

        // Act
        marker1.visitLibraryMethod(libraryClass, method1);
        marker2.visitLibraryMethod(libraryClass, method2);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(method1),
                "First method should have optimization info");
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(method2),
                "Second method should have optimization info");
    }

    /**
     * Tests that visitLibraryMethod replaces existing processing info.
     * If the method already has processing info, it should be replaced.
     */
    @Test
    public void testVisitLibraryMethod_replacesExistingProcessingInfo() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();
        Object existingInfo = new Object();
        libraryMethod.setProcessingInfo(existingInfo);

        // Act
        keepMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        Object newInfo = libraryMethod.getProcessingInfo();
        assertNotNull(newInfo, "New processing info should be set");
        assertNotSame(existingInfo, newInfo, "New info should replace existing info");
        assertInstanceOf(MethodOptimizationInfo.class, newInfo,
                "New info should be MethodOptimizationInfo");
    }

    /**
     * Tests that visitLibraryMethod does not throw any exceptions with valid input.
     * Verifies basic exception-free operation.
     */
    @Test
    public void testVisitLibraryMethod_withValidInput_doesNotThrowException() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();

        // Act & Assert
        assertDoesNotThrow(() -> keepMarker.visitLibraryMethod(libraryClass, libraryMethod),
                "visitLibraryMethod should not throw exception with valid input");
    }

    /**
     * Tests that the MethodOptimizationInfo set by visitLibraryMethod has expected default values.
     * Verifies that the info object is properly initialized.
     */
    @Test
    public void testVisitLibraryMethod_setsMethodOptimizationInfoWithCorrectDefaults() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();

        // Act
        keepMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod);
        assertTrue(info.isKept(), "Method should be kept");
        assertFalse(info.hasNoSideEffects(), "Should report having side effects by default");
        assertFalse(info.hasNoExternalSideEffects(), "Should report having external side effects by default");
        assertFalse(info.hasNoEscapingParameters(), "Should report having escaping parameters by default");
        assertTrue(info.hasSideEffects(), "Should report having side effects");
    }

    /**
     * Tests that visitLibraryMethod can be called in rapid succession.
     * Verifies consistent behavior under rapid sequential calls.
     */
    @Test
    public void testVisitLibraryMethod_rapidSequentialCalls_consistentBehavior() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();

        // Act & Assert - all calls should succeed
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() -> keepMarker.visitLibraryMethod(libraryClass, libraryMethod),
                    "Call " + i + " should not throw exception");
        }

        // Verify final state
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod),
                "Method should still have optimization info after many calls");
    }

    /**
     * Tests that visitLibraryMethod works with methods that have names.
     * Verifies the method works with more realistic method instances.
     */
    @Test
    public void testVisitLibraryMethod_withNamedMethod_setsOptimizationInfo() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = "testMethod";

        // Act
        keepMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod),
                "Named method should have optimization info set");
    }

    /**
     * Tests that visitLibraryMethod works with methods that have descriptors.
     * Verifies the method works with more realistic method instances.
     */
    @Test
    public void testVisitLibraryMethod_withMethodDescriptor_setsOptimizationInfo() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.descriptor = "()V";

        // Act
        keepMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod),
                "Method with descriptor should have optimization info set");
    }

    /**
     * Tests that the method can be used in a visitor pattern context.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitLibraryMethod_usedAsMemberVisitor_setsOptimizationInfo() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();
        proguard.classfile.visitor.MemberVisitor visitor = keepMarker;

        // Act
        visitor.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod),
                "Method should have optimization info when visited through MemberVisitor interface");
    }

    /**
     * Tests that visitLibraryMethod creates independent optimization info for each method.
     * Modifying one method's info should not affect another method's info.
     */
    @Test
    public void testVisitLibraryMethod_createsIndependentOptimizationInfo() {
        // Arrange
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();

        // Act
        keepMarker.visitLibraryMethod(libraryClass, method1);
        keepMarker.visitLibraryMethod(libraryClass, method2);

        MethodOptimizationInfo info1 = MethodOptimizationInfo.getMethodOptimizationInfo(method1);
        MethodOptimizationInfo info2 = MethodOptimizationInfo.getMethodOptimizationInfo(method2);

        // Modify info1
        info1.setNoSideEffects();

        // Assert - info2 should not be affected
        assertTrue(info1.hasNoSideEffects(), "First method info should have no side effects");
        assertFalse(info2.hasNoSideEffects(), "Second method info should not be affected");
    }

    /**
     * Tests that visitLibraryMethod works correctly with different LibraryClass instances.
     * Verifies that the class parameter doesn't affect the method optimization info setting.
     */
    @Test
    public void testVisitLibraryMethod_withDifferentClasses_setsOptimizationInfo() {
        // Arrange
        LibraryClass class1 = new LibraryClass();
        LibraryClass class2 = new LibraryClass();
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();

        // Act
        keepMarker.visitLibraryMethod(class1, method1);
        keepMarker.visitLibraryMethod(class2, method2);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(method1),
                "Method from first class should have optimization info");
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(method2),
                "Method from second class should have optimization info");
    }

    /**
     * Tests that visitLibraryMethod works correctly after the KeepMarker has visited other types.
     * Verifies that the method maintains consistent behavior across different visitor method calls.
     */
    @Test
    public void testVisitLibraryMethod_afterOtherVisitorMethods_stillSetsOptimizationInfo() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();
        proguard.classfile.ProgramClass testClass = new proguard.classfile.ProgramClass();
        proguard.classfile.ProgramField programField = new proguard.classfile.ProgramField();
        proguard.classfile.LibraryField libraryField = new proguard.classfile.LibraryField();

        // Act - call other visitor methods first
        keepMarker.visitAnyClass(testClass);
        keepMarker.visitProgramField(testClass, programField);
        keepMarker.visitLibraryField(libraryClass, libraryField);
        keepMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod),
                "Library method should have optimization info even after other visitor methods were called");
    }

    /**
     * Tests that visitLibraryMethod can handle methods with access flags set.
     * Verifies the method works with methods that have various access modifiers.
     */
    @Test
    public void testVisitLibraryMethod_withAccessFlags_setsOptimizationInfo() {
        // Arrange
        LibraryMethod publicMethod = new LibraryMethod();
        publicMethod.u2accessFlags = 0x0001; // ACC_PUBLIC

        LibraryMethod privateMethod = new LibraryMethod();
        privateMethod.u2accessFlags = 0x0002; // ACC_PRIVATE

        LibraryMethod staticMethod = new LibraryMethod();
        staticMethod.u2accessFlags = 0x0008; // ACC_STATIC

        LibraryMethod synchronizedMethod = new LibraryMethod();
        synchronizedMethod.u2accessFlags = 0x0020; // ACC_SYNCHRONIZED

        LibraryMethod nativeMethod = new LibraryMethod();
        nativeMethod.u2accessFlags = 0x0100; // ACC_NATIVE

        // Act
        keepMarker.visitLibraryMethod(libraryClass, publicMethod);
        keepMarker.visitLibraryMethod(libraryClass, privateMethod);
        keepMarker.visitLibraryMethod(libraryClass, staticMethod);
        keepMarker.visitLibraryMethod(libraryClass, synchronizedMethod);
        keepMarker.visitLibraryMethod(libraryClass, nativeMethod);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(publicMethod),
                "Public method should have optimization info");
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(privateMethod),
                "Private method should have optimization info");
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(staticMethod),
                "Static method should have optimization info");
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(synchronizedMethod),
                "Synchronized method should have optimization info");
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(nativeMethod),
                "Native method should have optimization info");
    }

    /**
     * Tests that visitLibraryMethod handles the same method being visited from different classes.
     * Verifies that the method's optimization info is updated regardless of which class visits it.
     */
    @Test
    public void testVisitLibraryMethod_sameMethodDifferentClasses_updatesOptimizationInfo() {
        // Arrange
        LibraryClass class1 = new LibraryClass();
        LibraryClass class2 = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();

        // Act
        keepMarker.visitLibraryMethod(class1, libraryMethod);
        MethodOptimizationInfo info1 = MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod);

        keepMarker.visitLibraryMethod(class2, libraryMethod);
        MethodOptimizationInfo info2 = MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod);

        // Assert
        assertNotNull(info1, "Method should have optimization info after first visit");
        assertNotNull(info2, "Method should have optimization info after second visit");
        assertNotSame(info1, info2, "Second visit should create new optimization info");
    }

    /**
     * Tests that visitLibraryMethod does not modify the LibraryClass parameter.
     * The class should remain unchanged after the method call.
     */
    @Test
    public void testVisitLibraryMethod_doesNotModifyClass() {
        // Arrange
        LibraryClass testClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        Object classProcessingInfo = new Object();
        testClass.setProcessingInfo(classProcessingInfo);

        // Act
        keepMarker.visitLibraryMethod(testClass, libraryMethod);

        // Assert
        assertSame(classProcessingInfo, testClass.getProcessingInfo(),
                "Class processing info should not be modified");
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod),
                "Method should have optimization info");
    }

    /**
     * Tests that visitLibraryMethod works with methods from Java standard library.
     * Verifies the method handles methods from typical library classes.
     */
    @Test
    public void testVisitLibraryMethod_withStandardLibraryMethod_setsOptimizationInfo() {
        // Arrange
        LibraryClass javaLangString = new LibraryClass();
        javaLangString.thisClassName = "java/lang/String";

        LibraryMethod lengthMethod = new LibraryMethod();
        lengthMethod.name = "length";
        lengthMethod.descriptor = "()I";

        // Act
        keepMarker.visitLibraryMethod(javaLangString, lengthMethod);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(lengthMethod),
                "Standard library method should have optimization info");
    }

    /**
     * Tests that visitLibraryMethod can handle methods with both name and descriptor set.
     * Verifies the method works with fully configured method instances.
     */
    @Test
    public void testVisitLibraryMethod_withCompleteMethodInfo_setsOptimizationInfo() {
        // Arrange
        LibraryMethod libraryMethod = new LibraryMethod();
        libraryMethod.name = "myMethod";
        libraryMethod.descriptor = "(Ljava/lang/String;)V";
        libraryMethod.u2accessFlags = 0x0001; // ACC_PUBLIC

        // Act
        keepMarker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod),
                "Fully configured method should have optimization info");
    }

    /**
     * Tests that visitLibraryMethod works with abstract methods.
     * Verifies the method handles abstract methods from library classes.
     */
    @Test
    public void testVisitLibraryMethod_withAbstractMethod_setsOptimizationInfo() {
        // Arrange
        LibraryMethod abstractMethod = new LibraryMethod();
        abstractMethod.name = "abstractMethod";
        abstractMethod.descriptor = "()V";
        abstractMethod.u2accessFlags = 0x0401; // ACC_PUBLIC | ACC_ABSTRACT

        // Act
        keepMarker.visitLibraryMethod(libraryClass, abstractMethod);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(abstractMethod),
                "Abstract method should have optimization info");
    }

    /**
     * Tests that visitLibraryMethod works with constructor methods.
     * Verifies the method handles special methods like constructors.
     */
    @Test
    public void testVisitLibraryMethod_withConstructor_setsOptimizationInfo() {
        // Arrange
        LibraryMethod constructor = new LibraryMethod();
        constructor.name = "<init>";
        constructor.descriptor = "()V";

        // Act
        keepMarker.visitLibraryMethod(libraryClass, constructor);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(constructor),
                "Constructor should have optimization info set");
    }

    /**
     * Tests that visitLibraryMethod works with static initializer methods.
     * Verifies the method handles special methods like static initializers.
     */
    @Test
    public void testVisitLibraryMethod_withStaticInitializer_setsOptimizationInfo() {
        // Arrange
        LibraryMethod staticInitializer = new LibraryMethod();
        staticInitializer.name = "<clinit>";
        staticInitializer.descriptor = "()V";
        staticInitializer.u2accessFlags = 0x0008; // ACC_STATIC

        // Act
        keepMarker.visitLibraryMethod(libraryClass, staticInitializer);

        // Assert
        assertNotNull(MethodOptimizationInfo.getMethodOptimizationInfo(staticInitializer),
                "Static initializer should have optimization info set");
    }
}

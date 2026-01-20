package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.optimize.info.CodeAttributeOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KeepMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
 *
 * The visitCodeAttribute method in KeepMarker sets CodeAttributeOptimizationInfo on the visited code attribute.
 * This marks the code attribute as kept during optimization, ensuring it remains unchanged.
 *
 * The method calls CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute), which:
 * 1. Creates a new CodeAttributeOptimizationInfo instance
 * 2. Sets it as the processing info on the code attribute via codeAttribute.setProcessingInfo()
 *
 * These tests verify that:
 * 1. The method correctly sets CodeAttributeOptimizationInfo on the code attribute
 * 2. The processing info can be retrieved after being set
 * 3. The method handles multiple invocations correctly
 * 4. The method handles different parameter combinations appropriately
 */
public class KeepMarkerClaude_visitCodeAttributeTest {

    private KeepMarker keepMarker;
    private Clazz clazz;
    private Method method;

    @BeforeEach
    public void setUp() {
        keepMarker = new KeepMarker();
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
    }

    /**
     * Tests that visitCodeAttribute sets CodeAttributeOptimizationInfo on a CodeAttribute.
     * Verifies the core functionality - the code attribute should have optimization info set after visiting.
     */
    @Test
    public void testVisitCodeAttribute_withValidCodeAttribute_setsOptimizationInfo() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        assertNull(codeAttribute.getProcessingInfo(), "Processing info should be null before visiting");

        // Act
        keepMarker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        Object processingInfo = codeAttribute.getProcessingInfo();
        assertNotNull(processingInfo, "Processing info should be set after visiting");
        assertInstanceOf(CodeAttributeOptimizationInfo.class, processingInfo,
                "Processing info should be an instance of CodeAttributeOptimizationInfo");
    }

    /**
     * Tests that the CodeAttributeOptimizationInfo can be retrieved and used after being set.
     * Verifies that the info object is properly configured.
     */
    @Test
    public void testVisitCodeAttribute_codeAttributeOptimizationInfoIsUsable() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        keepMarker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        CodeAttributeOptimizationInfo info =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute);
        assertNotNull(info, "CodeAttributeOptimizationInfo should be retrievable");
        assertTrue(info.isKept(), "Code attribute should be marked as kept");
    }

    /**
     * Tests that visitCodeAttribute can be called multiple times on the same code attribute.
     * Each call replaces the previous CodeAttributeOptimizationInfo.
     */
    @Test
    public void testVisitCodeAttribute_calledMultipleTimes_replacesOptimizationInfo() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act - call visitCodeAttribute multiple times
        keepMarker.visitCodeAttribute(clazz, method, codeAttribute);
        CodeAttributeOptimizationInfo firstInfo =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute);

        keepMarker.visitCodeAttribute(clazz, method, codeAttribute);
        CodeAttributeOptimizationInfo secondInfo =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute);

        keepMarker.visitCodeAttribute(clazz, method, codeAttribute);
        CodeAttributeOptimizationInfo thirdInfo =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute);

        // Assert - each call creates a new instance
        assertNotNull(firstInfo, "First info should not be null");
        assertNotNull(secondInfo, "Second info should not be null");
        assertNotNull(thirdInfo, "Third info should not be null");
        assertNotSame(firstInfo, secondInfo, "Second call should create a new info instance");
        assertNotSame(secondInfo, thirdInfo, "Third call should create a new info instance");
    }

    /**
     * Tests that visitCodeAttribute can be called on multiple different code attributes.
     * Verifies that each code attribute gets its own CodeAttributeOptimizationInfo instance.
     */
    @Test
    public void testVisitCodeAttribute_multipleCodeAttributes_eachGetsOwnOptimizationInfo() {
        // Arrange
        CodeAttribute codeAttr1 = new CodeAttribute();
        CodeAttribute codeAttr2 = new CodeAttribute();
        CodeAttribute codeAttr3 = new CodeAttribute();

        // Act
        keepMarker.visitCodeAttribute(clazz, method, codeAttr1);
        keepMarker.visitCodeAttribute(clazz, method, codeAttr2);
        keepMarker.visitCodeAttribute(clazz, method, codeAttr3);

        // Assert
        CodeAttributeOptimizationInfo info1 =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttr1);
        CodeAttributeOptimizationInfo info2 =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttr2);
        CodeAttributeOptimizationInfo info3 =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttr3);

        assertNotNull(info1, "First code attribute should have optimization info");
        assertNotNull(info2, "Second code attribute should have optimization info");
        assertNotNull(info3, "Third code attribute should have optimization info");

        assertNotSame(info1, info2, "Each code attribute should have its own info instance");
        assertNotSame(info2, info3, "Each code attribute should have its own info instance");
        assertNotSame(info1, info3, "Each code attribute should have its own info instance");
    }

    /**
     * Tests that visitCodeAttribute works correctly with mock objects.
     * Verifies that the method interacts correctly with the CodeAttribute.
     */
    @Test
    public void testVisitCodeAttribute_withMockCodeAttribute_setsProcessingInfo() {
        // Arrange
        CodeAttribute mockCodeAttribute = mock(CodeAttribute.class);

        // Act
        keepMarker.visitCodeAttribute(clazz, method, mockCodeAttribute);

        // Assert - verify setProcessingInfo was called with a CodeAttributeOptimizationInfo instance
        verify(mockCodeAttribute, times(1)).setProcessingInfo(any(CodeAttributeOptimizationInfo.class));
    }

    /**
     * Tests that visitCodeAttribute throws NullPointerException with null code attribute.
     * The method should not handle null gracefully as it needs to set processing info.
     */
    @Test
    public void testVisitCodeAttribute_withNullCodeAttribute_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> keepMarker.visitCodeAttribute(clazz, method, null),
                "visitCodeAttribute with null code attribute should throw NullPointerException");
    }

    /**
     * Tests that visitCodeAttribute can handle null Clazz parameter.
     * The clazz parameter is passed but not used in the method, so null should be acceptable.
     */
    @Test
    public void testVisitCodeAttribute_withNullClazz_stillSetsOptimizationInfo() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        keepMarker.visitCodeAttribute(null, method, codeAttribute);

        // Assert
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute),
                "Code attribute should have optimization info even with null clazz");
    }

    /**
     * Tests that visitCodeAttribute can handle null Method parameter.
     * The method parameter is passed but not used in the method, so null should be acceptable.
     */
    @Test
    public void testVisitCodeAttribute_withNullMethod_stillSetsOptimizationInfo() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        keepMarker.visitCodeAttribute(clazz, null, codeAttribute);

        // Assert
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute),
                "Code attribute should have optimization info even with null method");
    }

    /**
     * Tests that visitCodeAttribute can be used by multiple KeepMarker instances.
     * Verifies that different marker instances behave consistently.
     */
    @Test
    public void testVisitCodeAttribute_multipleKeepMarkerInstances_allSetOptimizationInfo() {
        // Arrange
        KeepMarker marker1 = new KeepMarker();
        KeepMarker marker2 = new KeepMarker();
        CodeAttribute codeAttr1 = new CodeAttribute();
        CodeAttribute codeAttr2 = new CodeAttribute();

        // Act
        marker1.visitCodeAttribute(clazz, method, codeAttr1);
        marker2.visitCodeAttribute(clazz, method, codeAttr2);

        // Assert
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttr1),
                "First code attribute should have optimization info");
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttr2),
                "Second code attribute should have optimization info");
    }

    /**
     * Tests that visitCodeAttribute replaces existing processing info.
     * If the code attribute already has processing info, it should be replaced.
     */
    @Test
    public void testVisitCodeAttribute_replacesExistingProcessingInfo() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        Object existingInfo = new Object();
        codeAttribute.setProcessingInfo(existingInfo);

        // Act
        keepMarker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        Object newInfo = codeAttribute.getProcessingInfo();
        assertNotNull(newInfo, "New processing info should be set");
        assertNotSame(existingInfo, newInfo, "New info should replace existing info");
        assertInstanceOf(CodeAttributeOptimizationInfo.class, newInfo,
                "New info should be CodeAttributeOptimizationInfo");
    }

    /**
     * Tests that visitCodeAttribute does not throw any exceptions with valid input.
     * Verifies basic exception-free operation.
     */
    @Test
    public void testVisitCodeAttribute_withValidInput_doesNotThrowException() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> keepMarker.visitCodeAttribute(clazz, method, codeAttribute),
                "visitCodeAttribute should not throw exception with valid input");
    }

    /**
     * Tests that visitCodeAttribute can be called in rapid succession.
     * Verifies consistent behavior under rapid sequential calls.
     */
    @Test
    public void testVisitCodeAttribute_rapidSequentialCalls_consistentBehavior() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert - all calls should succeed
        for (int i = 0; i < 100; i++) {
            assertDoesNotThrow(() -> keepMarker.visitCodeAttribute(clazz, method, codeAttribute),
                    "Call " + i + " should not throw exception");
        }

        // Verify final state
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute),
                "Code attribute should still have optimization info after many calls");
    }

    /**
     * Tests that the method can be used in a visitor pattern context.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitCodeAttribute_usedAsAttributeVisitor_setsOptimizationInfo() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = keepMarker;

        // Act
        visitor.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute),
                "Code attribute should have optimization info when visited through AttributeVisitor interface");
    }

    /**
     * Tests that visitCodeAttribute creates independent optimization info for each code attribute.
     * Modifying one code attribute's info should not affect another code attribute's info.
     */
    @Test
    public void testVisitCodeAttribute_createsIndependentOptimizationInfo() {
        // Arrange
        CodeAttribute codeAttr1 = new CodeAttribute();
        CodeAttribute codeAttr2 = new CodeAttribute();

        // Act
        keepMarker.visitCodeAttribute(clazz, method, codeAttr1);
        keepMarker.visitCodeAttribute(clazz, method, codeAttr2);

        CodeAttributeOptimizationInfo info1 =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttr1);
        CodeAttributeOptimizationInfo info2 =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttr2);

        // Assert - both should exist and be different instances
        assertNotNull(info1, "First code attribute should have optimization info");
        assertNotNull(info2, "Second code attribute should have optimization info");
        assertNotSame(info1, info2, "Each code attribute should have independent info");
    }

    /**
     * Tests that visitCodeAttribute works correctly with different Clazz instances.
     * Verifies that the clazz parameter doesn't affect the code attribute optimization info setting.
     */
    @Test
    public void testVisitCodeAttribute_withDifferentClasses_setsOptimizationInfo() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        CodeAttribute codeAttr1 = new CodeAttribute();
        CodeAttribute codeAttr2 = new CodeAttribute();

        // Act
        keepMarker.visitCodeAttribute(clazz1, method, codeAttr1);
        keepMarker.visitCodeAttribute(clazz2, method, codeAttr2);

        // Assert
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttr1),
                "Code attribute from first class should have optimization info");
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttr2),
                "Code attribute from second class should have optimization info");
    }

    /**
     * Tests that visitCodeAttribute works correctly with different Method instances.
     * Verifies that the method parameter doesn't affect the code attribute optimization info setting.
     */
    @Test
    public void testVisitCodeAttribute_withDifferentMethods_setsOptimizationInfo() {
        // Arrange
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        CodeAttribute codeAttr1 = new CodeAttribute();
        CodeAttribute codeAttr2 = new CodeAttribute();

        // Act
        keepMarker.visitCodeAttribute(clazz, method1, codeAttr1);
        keepMarker.visitCodeAttribute(clazz, method2, codeAttr2);

        // Assert
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttr1),
                "Code attribute from first method should have optimization info");
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttr2),
                "Code attribute from second method should have optimization info");
    }

    /**
     * Tests that visitCodeAttribute works correctly after the KeepMarker has visited other types.
     * Verifies that the method maintains consistent behavior across different visitor method calls.
     */
    @Test
    public void testVisitCodeAttribute_afterOtherVisitorMethods_stillSetsOptimizationInfo() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        ProgramClass testClass = new ProgramClass();
        proguard.classfile.ProgramField programField = new proguard.classfile.ProgramField();
        proguard.classfile.ProgramMethod programMethod = new proguard.classfile.ProgramMethod();

        // Act - call other visitor methods first
        keepMarker.visitAnyClass(testClass);
        keepMarker.visitProgramField(testClass, programField);
        keepMarker.visitProgramMethod(testClass, programMethod);
        keepMarker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute),
                "Code attribute should have optimization info even after other visitor methods were called");
    }

    /**
     * Tests that visitCodeAttribute does not modify the Clazz parameter.
     * The clazz should remain unchanged after the method call.
     */
    @Test
    public void testVisitCodeAttribute_doesNotModifyClazz() {
        // Arrange
        ProgramClass testClass = new ProgramClass();
        CodeAttribute codeAttribute = new CodeAttribute();
        Object classProcessingInfo = new Object();
        testClass.setProcessingInfo(classProcessingInfo);

        // Act
        keepMarker.visitCodeAttribute(testClass, method, codeAttribute);

        // Assert
        assertSame(classProcessingInfo, testClass.getProcessingInfo(),
                "Clazz processing info should not be modified");
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute),
                "Code attribute should have optimization info");
    }

    /**
     * Tests that visitCodeAttribute does not modify the Method parameter.
     * The method should remain unchanged after the method call.
     */
    @Test
    public void testVisitCodeAttribute_doesNotModifyMethod() {
        // Arrange
        ProgramMethod testMethod = new ProgramMethod();
        CodeAttribute codeAttribute = new CodeAttribute();
        Object methodProcessingInfo = new Object();
        testMethod.setProcessingInfo(methodProcessingInfo);

        // Act
        keepMarker.visitCodeAttribute(clazz, testMethod, codeAttribute);

        // Assert
        assertSame(methodProcessingInfo, testMethod.getProcessingInfo(),
                "Method processing info should not be modified");
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute),
                "Code attribute should have optimization info");
    }

    /**
     * Tests that visitCodeAttribute works with code attributes that have max stack/locals set.
     * Verifies the method works with more realistic code attribute instances.
     */
    @Test
    public void testVisitCodeAttribute_withConfiguredCodeAttribute_setsOptimizationInfo() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxStack = 10;
        codeAttribute.u2maxLocals = 5;

        // Act
        keepMarker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute),
                "Configured code attribute should have optimization info set");
    }

    /**
     * Tests that visitCodeAttribute can be called with real ProgramClass and ProgramMethod.
     * Verifies the method works with actual instances, not just mocks.
     */
    @Test
    public void testVisitCodeAttribute_withRealClassAndMethod_setsOptimizationInfo() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        ProgramMethod realMethod = new ProgramMethod();
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        keepMarker.visitCodeAttribute(realClass, realMethod, codeAttribute);

        // Assert
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute),
                "Code attribute should have optimization info with real class and method");
    }

    /**
     * Tests that visitCodeAttribute handles the same code attribute being visited from different contexts.
     * Verifies that the code attribute's optimization info is updated regardless of the context.
     */
    @Test
    public void testVisitCodeAttribute_sameCodeAttributeDifferentContexts_updatesOptimizationInfo() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        keepMarker.visitCodeAttribute(clazz1, method1, codeAttribute);
        CodeAttributeOptimizationInfo info1 =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute);

        keepMarker.visitCodeAttribute(clazz2, method2, codeAttribute);
        CodeAttributeOptimizationInfo info2 =
                CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute);

        // Assert
        assertNotNull(info1, "Code attribute should have optimization info after first visit");
        assertNotNull(info2, "Code attribute should have optimization info after second visit");
        assertNotSame(info1, info2, "Second visit should create new optimization info");
    }

    /**
     * Tests that visitCodeAttribute can be interleaved with visitAnyAttribute calls.
     * Verifies that specialized and generic attribute visitors can coexist.
     */
    @Test
    public void testVisitCodeAttribute_interleavedWithVisitAnyAttribute_worksCorrectly() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        proguard.classfile.attribute.Attribute genericAttribute = mock(proguard.classfile.attribute.Attribute.class);

        // Act & Assert - both methods should work when called in sequence
        assertDoesNotThrow(() -> {
            keepMarker.visitAnyAttribute(clazz, genericAttribute);
            keepMarker.visitCodeAttribute(clazz, method, codeAttribute);
            keepMarker.visitAnyAttribute(clazz, genericAttribute);
        });

        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute),
                "Code attribute should have optimization info");
    }

    /**
     * Tests that visitCodeAttribute works with code attributes that have code length set.
     * Verifies the method handles code attributes with actual bytecode size information.
     */
    @Test
    public void testVisitCodeAttribute_withCodeLength_setsOptimizationInfo() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u4codeLength = 100;

        // Act
        keepMarker.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        assertNotNull(CodeAttributeOptimizationInfo.getCodeAttributeOptimizationInfo(codeAttribute),
                "Code attribute with code length should have optimization info set");
    }
}

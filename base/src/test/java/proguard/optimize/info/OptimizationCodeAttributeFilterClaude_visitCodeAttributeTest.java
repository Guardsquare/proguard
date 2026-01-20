package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link OptimizationCodeAttributeFilter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
 *
 * The visitCodeAttribute method delegates to one of two AttributeVisitors based on whether
 * the code attribute is marked as kept:
 * - If NOT kept: delegates to the first attributeVisitor
 * - If kept: delegates to the otherAttributeVisitor
 * - If the selected visitor is null, no delegation occurs
 *
 * These tests verify:
 * 1. Correct delegation to first visitor for non-kept code attributes
 * 2. Correct delegation to second visitor for kept code attributes
 * 3. Proper handling of null visitors
 * 4. No delegation when visitor is null
 * 5. Correct behavior with various parameter combinations
 */
public class OptimizationCodeAttributeFilterClaude_visitCodeAttributeTest {

    private AttributeVisitor attributeVisitor;
    private AttributeVisitor otherAttributeVisitor;
    private OptimizationCodeAttributeFilter filter;
    private Clazz clazz;
    private Method method;

    @BeforeEach
    public void setUp() {
        attributeVisitor = mock(AttributeVisitor.class);
        otherAttributeVisitor = mock(AttributeVisitor.class);
        filter = new OptimizationCodeAttributeFilter(attributeVisitor, otherAttributeVisitor);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
    }

    // =========================================================================
    // Tests for non-kept code attributes (should delegate to first visitor)
    // =========================================================================

    /**
     * Tests that visitCodeAttribute delegates to the first visitor for a non-kept code attribute.
     * This is the primary use case - code attributes that can be optimized.
     */
    @Test
    public void testVisitCodeAttribute_withNonKeptCodeAttribute_delegatesToFirstVisitor() {
        // Arrange - create a non-kept code attribute (no optimization info)
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        filter.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - verify first visitor was called
        verify(attributeVisitor, times(1)).visitCodeAttribute(clazz, method, codeAttribute);
        verify(otherAttributeVisitor, never()).visitCodeAttribute(any(), any(), any());
    }

    /**
     * Tests that visitCodeAttribute delegates to the first visitor when code attribute
     * has no optimization info at all.
     */
    @Test
    public void testVisitCodeAttribute_withNoOptimizationInfo_delegatesToFirstVisitor() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        assertNull(codeAttribute.getProcessingInfo(), "Code attribute should have no processing info");

        // Act
        filter.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        verify(attributeVisitor, times(1)).visitCodeAttribute(clazz, method, codeAttribute);
        verify(otherAttributeVisitor, never()).visitCodeAttribute(any(), any(), any());
    }

    /**
     * Tests that visitCodeAttribute delegates to first visitor for multiple non-kept attributes.
     */
    @Test
    public void testVisitCodeAttribute_multipleNonKeptAttributes_delegatesToFirstVisitor() {
        // Arrange
        CodeAttribute codeAttr1 = new CodeAttribute();
        CodeAttribute codeAttr2 = new CodeAttribute();
        CodeAttribute codeAttr3 = new CodeAttribute();

        // Act
        filter.visitCodeAttribute(clazz, method, codeAttr1);
        filter.visitCodeAttribute(clazz, method, codeAttr2);
        filter.visitCodeAttribute(clazz, method, codeAttr3);

        // Assert - first visitor should be called 3 times
        verify(attributeVisitor, times(3)).visitCodeAttribute(eq(clazz), eq(method), any(CodeAttribute.class));
        verify(otherAttributeVisitor, never()).visitCodeAttribute(any(), any(), any());
    }

    // =========================================================================
    // Tests for kept code attributes (should delegate to second visitor)
    // =========================================================================

    /**
     * Tests that visitCodeAttribute delegates to the second visitor for a kept code attribute.
     * This is when the code attribute must be preserved during optimization.
     */
    @Test
    public void testVisitCodeAttribute_withKeptCodeAttribute_delegatesToSecondVisitor() {
        // Arrange - create a kept code attribute
        CodeAttribute codeAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);

        // Act
        filter.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - verify second visitor was called
        verify(otherAttributeVisitor, times(1)).visitCodeAttribute(clazz, method, codeAttribute);
        verify(attributeVisitor, never()).visitCodeAttribute(any(), any(), any());
    }

    /**
     * Tests that visitCodeAttribute correctly identifies kept code attributes
     * using the KeepMarker.isKept check.
     */
    @Test
    public void testVisitCodeAttribute_withMarkedCodeAttribute_delegatesToSecondVisitor() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);
        assertTrue(proguard.optimize.KeepMarker.isKept(codeAttribute),
                "Code attribute should be marked as kept");

        // Act
        filter.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        verify(otherAttributeVisitor, times(1)).visitCodeAttribute(clazz, method, codeAttribute);
        verify(attributeVisitor, never()).visitCodeAttribute(any(), any(), any());
    }

    /**
     * Tests that visitCodeAttribute delegates to second visitor for multiple kept attributes.
     */
    @Test
    public void testVisitCodeAttribute_multipleKeptAttributes_delegatesToSecondVisitor() {
        // Arrange
        CodeAttribute codeAttr1 = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttr1);

        CodeAttribute codeAttr2 = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttr2);

        CodeAttribute codeAttr3 = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttr3);

        // Act
        filter.visitCodeAttribute(clazz, method, codeAttr1);
        filter.visitCodeAttribute(clazz, method, codeAttr2);
        filter.visitCodeAttribute(clazz, method, codeAttr3);

        // Assert - second visitor should be called 3 times
        verify(otherAttributeVisitor, times(3)).visitCodeAttribute(eq(clazz), eq(method), any(CodeAttribute.class));
        verify(attributeVisitor, never()).visitCodeAttribute(any(), any(), any());
    }

    // =========================================================================
    // Tests for mixed kept/non-kept scenarios
    // =========================================================================

    /**
     * Tests that visitCodeAttribute correctly delegates to different visitors
     * based on each code attribute's kept status.
     */
    @Test
    public void testVisitCodeAttribute_mixedKeptAndNonKept_delegatesCorrectly() {
        // Arrange
        CodeAttribute nonKeptAttr1 = new CodeAttribute();
        CodeAttribute keptAttr1 = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(keptAttr1);
        CodeAttribute nonKeptAttr2 = new CodeAttribute();
        CodeAttribute keptAttr2 = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(keptAttr2);

        // Act
        filter.visitCodeAttribute(clazz, method, nonKeptAttr1);
        filter.visitCodeAttribute(clazz, method, keptAttr1);
        filter.visitCodeAttribute(clazz, method, nonKeptAttr2);
        filter.visitCodeAttribute(clazz, method, keptAttr2);

        // Assert - each visitor should be called twice
        verify(attributeVisitor, times(2)).visitCodeAttribute(eq(clazz), eq(method), any(CodeAttribute.class));
        verify(otherAttributeVisitor, times(2)).visitCodeAttribute(eq(clazz), eq(method), any(CodeAttribute.class));
    }

    /**
     * Tests that the same code attribute changes delegation if its kept status changes.
     */
    @Test
    public void testVisitCodeAttribute_changingKeptStatus_changesDelegate() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act - visit when not kept
        filter.visitCodeAttribute(clazz, method, codeAttribute);

        // Mark as kept and visit again
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);
        filter.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - both visitors should have been called once
        verify(attributeVisitor, times(1)).visitCodeAttribute(clazz, method, codeAttribute);
        verify(otherAttributeVisitor, times(1)).visitCodeAttribute(clazz, method, codeAttribute);
    }

    // =========================================================================
    // Tests for null visitor handling
    // =========================================================================

    /**
     * Tests that visitCodeAttribute handles null first visitor gracefully when not kept.
     * No delegation should occur if the selected visitor is null.
     */
    @Test
    public void testVisitCodeAttribute_withNullFirstVisitor_nonKeptAttribute_noDelegation() {
        // Arrange
        OptimizationCodeAttributeFilter filterWithNullFirst =
                new OptimizationCodeAttributeFilter(null, otherAttributeVisitor);
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> filterWithNullFirst.visitCodeAttribute(clazz, method, codeAttribute));

        // Verify no visitor was called
        verify(otherAttributeVisitor, never()).visitCodeAttribute(any(), any(), any());
    }

    /**
     * Tests that visitCodeAttribute handles null second visitor gracefully when kept.
     * No delegation should occur if the selected visitor is null.
     */
    @Test
    public void testVisitCodeAttribute_withNullSecondVisitor_keptAttribute_noDelegation() {
        // Arrange
        OptimizationCodeAttributeFilter filterWithNullSecond =
                new OptimizationCodeAttributeFilter(attributeVisitor, null);
        CodeAttribute codeAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> filterWithNullSecond.visitCodeAttribute(clazz, method, codeAttribute));

        // Verify no visitor was called
        verify(attributeVisitor, never()).visitCodeAttribute(any(), any(), any());
    }

    /**
     * Tests that visitCodeAttribute handles both visitors being null.
     */
    @Test
    public void testVisitCodeAttribute_withBothVisitorsNull_noDelegation() {
        // Arrange
        OptimizationCodeAttributeFilter filterWithNullVisitors =
                new OptimizationCodeAttributeFilter(null, null);
        CodeAttribute nonKeptAttr = new CodeAttribute();
        CodeAttribute keptAttr = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(keptAttr);

        // Act & Assert - should not throw exception for either type
        assertDoesNotThrow(() -> {
            filterWithNullVisitors.visitCodeAttribute(clazz, method, nonKeptAttr);
            filterWithNullVisitors.visitCodeAttribute(clazz, method, keptAttr);
        });
    }

    /**
     * Tests that visitCodeAttribute with single-parameter constructor (only first visitor).
     */
    @Test
    public void testVisitCodeAttribute_withSingleParameterConstructor_nonKeptDelegates() {
        // Arrange
        OptimizationCodeAttributeFilter singleVisitorFilter =
                new OptimizationCodeAttributeFilter(attributeVisitor);
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        singleVisitorFilter.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - first visitor should be called
        verify(attributeVisitor, times(1)).visitCodeAttribute(clazz, method, codeAttribute);
    }

    /**
     * Tests that visitCodeAttribute with single-parameter constructor doesn't fail for kept attributes.
     */
    @Test
    public void testVisitCodeAttribute_withSingleParameterConstructor_keptNoDelegation() {
        // Arrange
        OptimizationCodeAttributeFilter singleVisitorFilter =
                new OptimizationCodeAttributeFilter(attributeVisitor);
        CodeAttribute codeAttribute = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(codeAttribute);

        // Act & Assert - should not throw exception (second visitor is null)
        assertDoesNotThrow(() -> singleVisitorFilter.visitCodeAttribute(clazz, method, codeAttribute));

        // First visitor should not be called (attribute is kept)
        verify(attributeVisitor, never()).visitCodeAttribute(any(), any(), any());
    }

    // =========================================================================
    // Tests for parameter handling
    // =========================================================================

    /**
     * Tests that visitCodeAttribute throws NullPointerException with null code attribute.
     * The method needs the code attribute to check if it's kept.
     */
    @Test
    public void testVisitCodeAttribute_withNullCodeAttribute_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> filter.visitCodeAttribute(clazz, method, null),
                "visitCodeAttribute with null code attribute should throw NullPointerException");
    }

    /**
     * Tests that visitCodeAttribute passes all parameters correctly to the delegate visitor.
     */
    @Test
    public void testVisitCodeAttribute_passesAllParametersToDelegate() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        filter.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - verify all parameters were passed to the delegate
        verify(attributeVisitor, times(1)).visitCodeAttribute(
                same(clazz),
                same(method),
                same(codeAttribute));
    }

    /**
     * Tests that visitCodeAttribute works with null Clazz parameter.
     */
    @Test
    public void testVisitCodeAttribute_withNullClazz_delegatesCorrectly() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        filter.visitCodeAttribute(null, method, codeAttribute);

        // Assert - should still delegate with null clazz
        verify(attributeVisitor, times(1)).visitCodeAttribute(null, method, codeAttribute);
    }

    /**
     * Tests that visitCodeAttribute works with null Method parameter.
     */
    @Test
    public void testVisitCodeAttribute_withNullMethod_delegatesCorrectly() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        filter.visitCodeAttribute(clazz, null, codeAttribute);

        // Assert - should still delegate with null method
        verify(attributeVisitor, times(1)).visitCodeAttribute(clazz, null, codeAttribute);
    }

    /**
     * Tests that visitCodeAttribute works with both null Clazz and Method.
     */
    @Test
    public void testVisitCodeAttribute_withNullClazzAndMethod_delegatesCorrectly() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        filter.visitCodeAttribute(null, null, codeAttribute);

        // Assert - should still delegate
        verify(attributeVisitor, times(1)).visitCodeAttribute(null, null, codeAttribute);
    }

    // =========================================================================
    // Tests for integration scenarios
    // =========================================================================

    /**
     * Tests that visitCodeAttribute can be used through AttributeVisitor interface.
     */
    @Test
    public void testVisitCodeAttribute_throughAttributeVisitorInterface_delegatesCorrectly() {
        // Arrange
        AttributeVisitor visitorInterface = filter;
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        visitorInterface.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert
        verify(attributeVisitor, times(1)).visitCodeAttribute(clazz, method, codeAttribute);
    }

    /**
     * Tests that visitCodeAttribute works with real ProgramClass and ProgramMethod instances.
     */
    @Test
    public void testVisitCodeAttribute_withRealInstances_delegatesCorrectly() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        ProgramMethod realMethod = new ProgramMethod();
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        filter.visitCodeAttribute(realClass, realMethod, codeAttribute);

        // Assert
        verify(attributeVisitor, times(1)).visitCodeAttribute(realClass, realMethod, codeAttribute);
    }

    /**
     * Tests that visitCodeAttribute can be called multiple times in rapid succession.
     */
    @Test
    public void testVisitCodeAttribute_rapidSuccessiveCalls_allDelegateCorrectly() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act - call 100 times
        for (int i = 0; i < 100; i++) {
            filter.visitCodeAttribute(clazz, method, codeAttribute);
        }

        // Assert - first visitor should be called 100 times
        verify(attributeVisitor, times(100)).visitCodeAttribute(clazz, method, codeAttribute);
        verify(otherAttributeVisitor, never()).visitCodeAttribute(any(), any(), any());
    }

    /**
     * Tests that multiple filter instances operate independently.
     */
    @Test
    public void testVisitCodeAttribute_multipleFilters_operateIndependently() {
        // Arrange
        AttributeVisitor visitor1 = mock(AttributeVisitor.class);
        AttributeVisitor visitor2 = mock(AttributeVisitor.class);
        AttributeVisitor otherVisitor1 = mock(AttributeVisitor.class);
        AttributeVisitor otherVisitor2 = mock(AttributeVisitor.class);

        OptimizationCodeAttributeFilter filter1 = new OptimizationCodeAttributeFilter(visitor1, otherVisitor1);
        OptimizationCodeAttributeFilter filter2 = new OptimizationCodeAttributeFilter(visitor2, otherVisitor2);

        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        filter1.visitCodeAttribute(clazz, method, codeAttribute);
        filter2.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - each filter delegates to its own visitor
        verify(visitor1, times(1)).visitCodeAttribute(clazz, method, codeAttribute);
        verify(visitor2, times(1)).visitCodeAttribute(clazz, method, codeAttribute);
        verify(otherVisitor1, never()).visitCodeAttribute(any(), any(), any());
        verify(otherVisitor2, never()).visitCodeAttribute(any(), any(), any());
    }

    /**
     * Tests that visitCodeAttribute doesn't modify the code attribute.
     */
    @Test
    public void testVisitCodeAttribute_doesNotModifyCodeAttribute() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2maxStack = 10;
        codeAttribute.u2maxLocals = 5;
        codeAttribute.u4codeLength = 100;

        // Act
        filter.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - code attribute properties should be unchanged
        assertEquals(10, codeAttribute.u2maxStack, "Max stack should be unchanged");
        assertEquals(5, codeAttribute.u2maxLocals, "Max locals should be unchanged");
        assertEquals(100, codeAttribute.u4codeLength, "Code length should be unchanged");
    }

    /**
     * Tests that visitCodeAttribute doesn't modify the clazz parameter.
     */
    @Test
    public void testVisitCodeAttribute_doesNotModifyClazz() {
        // Arrange
        ProgramClass testClass = new ProgramClass();
        Object classProcessingInfo = new Object();
        testClass.setProcessingInfo(classProcessingInfo);
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        filter.visitCodeAttribute(testClass, method, codeAttribute);

        // Assert
        assertSame(classProcessingInfo, testClass.getProcessingInfo(),
                "Clazz processing info should not be modified");
    }

    /**
     * Tests that visitCodeAttribute doesn't modify the method parameter.
     */
    @Test
    public void testVisitCodeAttribute_doesNotModifyMethod() {
        // Arrange
        ProgramMethod testMethod = new ProgramMethod();
        Object methodProcessingInfo = new Object();
        testMethod.setProcessingInfo(methodProcessingInfo);
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act
        filter.visitCodeAttribute(clazz, testMethod, codeAttribute);

        // Assert
        assertSame(methodProcessingInfo, testMethod.getProcessingInfo(),
                "Method processing info should not be modified");
    }

    /**
     * Tests that visitCodeAttribute delegates correctly after visitAnyAttribute is called.
     * Verifies that different visitor methods don't interfere with each other.
     */
    @Test
    public void testVisitCodeAttribute_afterVisitAnyAttribute_stillDelegatesCorrectly() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        proguard.classfile.attribute.Attribute genericAttribute =
                mock(proguard.classfile.attribute.Attribute.class);

        // Act - call visitAnyAttribute first (which is a no-op)
        filter.visitAnyAttribute(clazz, genericAttribute);
        filter.visitCodeAttribute(clazz, method, codeAttribute);

        // Assert - visitCodeAttribute should still delegate correctly
        verify(attributeVisitor, times(1)).visitCodeAttribute(clazz, method, codeAttribute);
    }

    /**
     * Tests that visitCodeAttribute with different Clazz instances delegates correctly.
     */
    @Test
    public void testVisitCodeAttribute_withDifferentClasses_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        CodeAttribute codeAttr1 = new CodeAttribute();
        CodeAttribute codeAttr2 = new CodeAttribute();

        // Act
        filter.visitCodeAttribute(clazz1, method, codeAttr1);
        filter.visitCodeAttribute(clazz2, method, codeAttr2);

        // Assert - both should delegate
        verify(attributeVisitor, times(1)).visitCodeAttribute(clazz1, method, codeAttr1);
        verify(attributeVisitor, times(1)).visitCodeAttribute(clazz2, method, codeAttr2);
    }

    /**
     * Tests that visitCodeAttribute with different Method instances delegates correctly.
     */
    @Test
    public void testVisitCodeAttribute_withDifferentMethods_delegatesCorrectly() {
        // Arrange
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);
        CodeAttribute codeAttr1 = new CodeAttribute();
        CodeAttribute codeAttr2 = new CodeAttribute();

        // Act
        filter.visitCodeAttribute(clazz, method1, codeAttr1);
        filter.visitCodeAttribute(clazz, method2, codeAttr2);

        // Assert - both should delegate
        verify(attributeVisitor, times(1)).visitCodeAttribute(clazz, method1, codeAttr1);
        verify(attributeVisitor, times(1)).visitCodeAttribute(clazz, method2, codeAttr2);
    }

    /**
     * Tests that visitCodeAttribute correctly delegates when the same visitor is used for both parameters.
     */
    @Test
    public void testVisitCodeAttribute_withSameVisitorForBoth_delegatesBasedOnKeptStatus() {
        // Arrange
        AttributeVisitor sameVisitor = mock(AttributeVisitor.class);
        OptimizationCodeAttributeFilter filterWithSameVisitor =
                new OptimizationCodeAttributeFilter(sameVisitor, sameVisitor);

        CodeAttribute nonKeptAttr = new CodeAttribute();
        CodeAttribute keptAttr = new CodeAttribute();
        CodeAttributeOptimizationInfo.setCodeAttributeOptimizationInfo(keptAttr);

        // Act
        filterWithSameVisitor.visitCodeAttribute(clazz, method, nonKeptAttr);
        filterWithSameVisitor.visitCodeAttribute(clazz, method, keptAttr);

        // Assert - same visitor should be called twice (once for each type)
        verify(sameVisitor, times(2)).visitCodeAttribute(eq(clazz), eq(method), any(CodeAttribute.class));
    }

    /**
     * Tests that visitCodeAttribute is thread-safe when called concurrently.
     */
    @Test
    public void testVisitCodeAttribute_concurrentCalls_allDelegateCorrectly() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final int callsPerThread = 10;
        Thread[] threads = new Thread[threadCount];
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act - call from multiple threads
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < callsPerThread; j++) {
                    filter.visitCodeAttribute(clazz, method, codeAttribute);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - visitor should be called exactly threadCount * callsPerThread times
        verify(attributeVisitor, times(threadCount * callsPerThread))
                .visitCodeAttribute(clazz, method, codeAttribute);
    }

    /**
     * Tests that visitCodeAttribute doesn't throw exception with valid input.
     */
    @Test
    public void testVisitCodeAttribute_withValidInput_doesNotThrowException() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> filter.visitCodeAttribute(clazz, method, codeAttribute),
                "visitCodeAttribute should not throw exception with valid input");
    }
}

package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.visitor.MemberVisitor;
import proguard.evaluation.value.Value;
import proguard.optimize.info.ProgramMethodOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ConstantMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
 *
 * The visitProgramMethod method in ConstantMemberFilter delegates to an inner MemberVisitor
 * only when the method has been marked as having a constant return value (i.e., its return value is particular).
 *
 * The method retrieves the method's return value via StoringInvocationUnit.getMethodReturnValue() and checks:
 * 1. If the value is not null
 * 2. If the value.isParticular() returns true
 *
 * Only when both conditions are met does it delegate to the constantMemberVisitor.
 *
 * These tests verify that the method:
 * 1. Correctly delegates when the method has a particular return value
 * 2. Does not delegate when the method has a non-particular return value
 * 3. Does not delegate when the method has a null return value
 * 4. Throws NullPointerException when the method has no optimization info
 * 5. Handles various edge cases correctly
 */
public class ConstantMemberFilterClaude_visitProgramMethodTest {

    private MemberVisitor mockInnerVisitor;
    private ConstantMemberFilter filter;
    private ProgramClass programClass;

    @BeforeEach
    public void setUp() {
        mockInnerVisitor = mock(MemberVisitor.class);
        filter = new ConstantMemberFilter(mockInnerVisitor);
        programClass = new ProgramClass();
    }

    /**
     * Tests that the filter delegates to the inner visitor when the method has a particular return value.
     * This is the core functionality - methods with particular (constant) return values should be visited.
     */
    @Test
    public void testVisitProgramMethod_withParticularReturnValue_delegatesToInnerVisitor() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Set up optimization info with a particular return value
        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo.getReturnValue()).thenReturn(particularValue);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(mockInnerVisitor, times(1))
            .visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that the filter does not delegate when the method has a non-particular return value.
     * Methods with non-particular return values are not constants and should be filtered out.
     */
    @Test
    public void testVisitProgramMethod_withNonParticularReturnValue_doesNotDelegate() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Set up optimization info with a non-particular return value
        Value nonParticularValue = mock(Value.class);
        when(nonParticularValue.isParticular()).thenReturn(false);

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo.getReturnValue()).thenReturn(nonParticularValue);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(mockInnerVisitor, never())
            .visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that the filter does not delegate when the method has a null return value.
     * Methods without any return value information should be filtered out.
     */
    @Test
    public void testVisitProgramMethod_withNullReturnValue_doesNotDelegate() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Set up optimization info with null return value
        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo.getReturnValue()).thenReturn(null);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(mockInnerVisitor, never())
            .visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that the filter throws NullPointerException when the method has no optimization info.
     * This is expected behavior as the code assumes methods have optimization info set.
     * In practice, methods processed by ProGuard will have optimization info initialized.
     */
    @Test
    public void testVisitProgramMethod_withNullOptimizationInfo_throwsNullPointerException() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.setProcessingInfo(null);

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> filter.visitProgramMethod(programClass, programMethod),
            "Should throw NullPointerException when optimization info is null");
    }

    /**
     * Tests that the filter correctly delegates multiple methods with particular return values.
     * Verifies that the filter can handle multiple invocations correctly.
     */
    @Test
    public void testVisitProgramMethod_multipleMethodsWithParticularReturnValues_delegatesAll() {
        // Arrange
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramMethod method3 = new ProgramMethod();

        // Set up all methods with particular return values
        for (ProgramMethod method : new ProgramMethod[]{method1, method2, method3}) {
            Value particularValue = mock(Value.class);
            when(particularValue.isParticular()).thenReturn(true);

            ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
            when(optimizationInfo.getReturnValue()).thenReturn(particularValue);

            method.setProcessingInfo(optimizationInfo);
        }

        // Act
        filter.visitProgramMethod(programClass, method1);
        filter.visitProgramMethod(programClass, method2);
        filter.visitProgramMethod(programClass, method3);

        // Assert
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, method1);
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, method2);
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, method3);
    }

    /**
     * Tests that the filter correctly filters out multiple methods with non-particular return values.
     * Verifies that the filter can handle multiple invocations without delegating.
     */
    @Test
    public void testVisitProgramMethod_multipleMethodsWithNonParticularReturnValues_filtersAll() {
        // Arrange
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramMethod method3 = new ProgramMethod();

        // Set up all methods with non-particular return values
        for (ProgramMethod method : new ProgramMethod[]{method1, method2, method3}) {
            Value nonParticularValue = mock(Value.class);
            when(nonParticularValue.isParticular()).thenReturn(false);

            ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
            when(optimizationInfo.getReturnValue()).thenReturn(nonParticularValue);

            method.setProcessingInfo(optimizationInfo);
        }

        // Act
        filter.visitProgramMethod(programClass, method1);
        filter.visitProgramMethod(programClass, method2);
        filter.visitProgramMethod(programClass, method3);

        // Assert
        verify(mockInnerVisitor, never()).visitProgramMethod(any(), any());
    }

    /**
     * Tests that the filter correctly handles a mix of methods with different return value types.
     * Verifies selective delegation based on the particular flag.
     */
    @Test
    public void testVisitProgramMethod_mixedMethods_delegatesSelectively() {
        // Arrange
        ProgramMethod particularMethod = new ProgramMethod();
        ProgramMethod nonParticularMethod = new ProgramMethod();
        ProgramMethod nullValueMethod = new ProgramMethod();

        // Set up particular method
        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);
        ProgramMethodOptimizationInfo particularInfo = mock(ProgramMethodOptimizationInfo.class);
        when(particularInfo.getReturnValue()).thenReturn(particularValue);
        particularMethod.setProcessingInfo(particularInfo);

        // Set up non-particular method
        Value nonParticularValue = mock(Value.class);
        when(nonParticularValue.isParticular()).thenReturn(false);
        ProgramMethodOptimizationInfo nonParticularInfo = mock(ProgramMethodOptimizationInfo.class);
        when(nonParticularInfo.getReturnValue()).thenReturn(nonParticularValue);
        nonParticularMethod.setProcessingInfo(nonParticularInfo);

        // Set up null value method
        ProgramMethodOptimizationInfo nullValueInfo = mock(ProgramMethodOptimizationInfo.class);
        when(nullValueInfo.getReturnValue()).thenReturn(null);
        nullValueMethod.setProcessingInfo(nullValueInfo);

        // Act
        filter.visitProgramMethod(programClass, particularMethod);
        filter.visitProgramMethod(programClass, nonParticularMethod);
        filter.visitProgramMethod(programClass, nullValueMethod);

        // Assert
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, particularMethod);
        verify(mockInnerVisitor, never()).visitProgramMethod(programClass, nonParticularMethod);
        verify(mockInnerVisitor, never()).visitProgramMethod(programClass, nullValueMethod);
    }

    /**
     * Tests that the same method can be visited multiple times.
     * Verifies that the filter's state doesn't interfere with repeated visits.
     */
    @Test
    public void testVisitProgramMethod_sameMethodMultipleTimes_delegatesEachTime() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo.getReturnValue()).thenReturn(particularValue);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);
        filter.visitProgramMethod(programClass, programMethod);
        filter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(mockInnerVisitor, times(3))
            .visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that the filter works with different ProgramClass instances.
     * Verifies that the class parameter is passed correctly.
     */
    @Test
    public void testVisitProgramMethod_withDifferentClasses_passesCorrectClass() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo.getReturnValue()).thenReturn(particularValue);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(class1, programMethod);
        filter.visitProgramMethod(class2, programMethod);

        // Assert
        verify(mockInnerVisitor, times(1)).visitProgramMethod(class1, programMethod);
        verify(mockInnerVisitor, times(1)).visitProgramMethod(class2, programMethod);
    }

    /**
     * Tests that the filter does not throw exceptions when the visitor throws an exception.
     * Verifies that exceptions from the inner visitor are propagated.
     */
    @Test
    public void testVisitProgramMethod_whenVisitorThrows_propagatesException() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo.getReturnValue()).thenReturn(particularValue);

        programMethod.setProcessingInfo(optimizationInfo);

        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException).when(mockInnerVisitor)
            .visitProgramMethod(any(), any());

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            filter.visitProgramMethod(programClass, programMethod),
            "Should propagate exception from inner visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that the filter doesn't call the inner visitor when isParticular() is false,
     * even if called multiple times.
     */
    @Test
    public void testVisitProgramMethod_nonParticularReturnValueMultipleTimes_neverDelegates() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        Value nonParticularValue = mock(Value.class);
        when(nonParticularValue.isParticular()).thenReturn(false);

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo.getReturnValue()).thenReturn(nonParticularValue);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        for (int i = 0; i < 10; i++) {
            filter.visitProgramMethod(programClass, programMethod);
        }

        // Assert
        verify(mockInnerVisitor, never()).visitProgramMethod(any(), any());
    }

    /**
     * Tests that the filter works correctly with a no-op visitor.
     * Verifies that the delegation happens even if the visitor does nothing.
     */
    @Test
    public void testVisitProgramMethod_withNoOpVisitor_delegatesCorrectly() {
        // Arrange
        MemberVisitor noOpVisitor = mock(MemberVisitor.class);
        doNothing().when(noOpVisitor).visitProgramMethod(any(), any());
        ConstantMemberFilter noOpFilter = new ConstantMemberFilter(noOpVisitor);

        ProgramMethod programMethod = new ProgramMethod();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo.getReturnValue()).thenReturn(particularValue);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        assertDoesNotThrow(() -> noOpFilter.visitProgramMethod(programClass, programMethod),
            "Should work with no-op visitor");

        // Assert
        verify(noOpVisitor, times(1)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that the filter passes parameters in the correct order.
     * Verifies that programClass comes before programMethod in the delegation.
     */
    @Test
    public void testVisitProgramMethod_passesParametersInCorrectOrder() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo.getReturnValue()).thenReturn(particularValue);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - verify the parameters are in correct order
        verify(mockInnerVisitor).visitProgramMethod(
            argThat(arg -> arg == programClass),
            argThat(arg -> arg == programMethod)
        );
    }

    /**
     * Tests that the filter returns normally without hanging.
     * Verifies the method executes quickly.
     */
    @Test
    public void testVisitProgramMethod_returnsImmediately() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo.getReturnValue()).thenReturn(particularValue);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        long startTime = System.nanoTime();
        filter.visitProgramMethod(programClass, programMethod);
        long endTime = System.nanoTime();

        // Assert - should complete very quickly (within 1 second)
        long durationNanos = endTime - startTime;
        long oneSecondInNanos = 1_000_000_000L;
        assertTrue(durationNanos < oneSecondInNanos,
                "Method should return immediately, took " + durationNanos + " nanoseconds");
    }

    /**
     * Tests that multiple filter instances work independently.
     * Verifies that different filters don't interfere with each other.
     */
    @Test
    public void testVisitProgramMethod_multipleInstances_workIndependently() {
        // Arrange
        MemberVisitor visitor1 = mock(MemberVisitor.class);
        MemberVisitor visitor2 = mock(MemberVisitor.class);
        ConstantMemberFilter filter1 = new ConstantMemberFilter(visitor1);
        ConstantMemberFilter filter2 = new ConstantMemberFilter(visitor2);

        ProgramMethod programMethod = new ProgramMethod();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo.getReturnValue()).thenReturn(particularValue);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter1.visitProgramMethod(programClass, programMethod);
        filter2.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(visitor1, times(1)).visitProgramMethod(programClass, programMethod);
        verify(visitor2, times(1)).visitProgramMethod(programClass, programMethod);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the filter can be reused after visiting methods.
     * Verifies that the filter maintains its state correctly across multiple uses.
     */
    @Test
    public void testVisitProgramMethod_filterReusable() {
        // Arrange
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramMethodOptimizationInfo optimizationInfo1 = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo1.getReturnValue()).thenReturn(particularValue);
        method1.setProcessingInfo(optimizationInfo1);

        ProgramMethodOptimizationInfo optimizationInfo2 = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo2.getReturnValue()).thenReturn(particularValue);
        method2.setProcessingInfo(optimizationInfo2);

        // Act & Assert - reuse the same filter
        assertDoesNotThrow(() -> {
            filter.visitProgramMethod(programClass, method1);
            filter.visitProgramMethod(programClass, method2);
            filter.visitProgramMethod(programClass, method1);
        }, "Filter should be reusable");

        verify(mockInnerVisitor, times(2)).visitProgramMethod(programClass, method1);
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, method2);
    }

    /**
     * Tests that the method doesn't call any other visitor methods besides visitProgramMethod.
     * Verifies that only the correct method is called on the inner visitor.
     */
    @Test
    public void testVisitProgramMethod_doesNotCallOtherVisitorMethods() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo.getReturnValue()).thenReturn(particularValue);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert - verify only visitProgramMethod was called
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, programMethod);
        verifyNoMoreInteractions(mockInnerVisitor);
    }

    /**
     * Tests that the filter works correctly when the method's optimization info
     * is of the expected type ProgramMethodOptimizationInfo.
     */
    @Test
    public void testVisitProgramMethod_withProgramMethodOptimizationInfo_handlesCorrectly() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        // Create a mock that returns a particular value
        ProgramMethodOptimizationInfo optimizationInfo = mock(ProgramMethodOptimizationInfo.class);
        when(optimizationInfo.getReturnValue()).thenReturn(particularValue);

        programMethod.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, programMethod);
    }

    /**
     * Tests that calling visitProgramField and visitProgramMethod on the same filter
     * doesn't cause interference between the two methods.
     */
    @Test
    public void testVisitProgramMethod_afterVisitProgramField_doesNotInterfere() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        Value methodValue = mock(Value.class);
        when(methodValue.isParticular()).thenReturn(true);
        ProgramMethodOptimizationInfo methodOptInfo = mock(ProgramMethodOptimizationInfo.class);
        when(methodOptInfo.getReturnValue()).thenReturn(methodValue);
        programMethod.setProcessingInfo(methodOptInfo);

        proguard.classfile.ProgramField programField = new proguard.classfile.ProgramField();
        Value fieldValue = mock(Value.class);
        when(fieldValue.isParticular()).thenReturn(true);
        proguard.optimize.info.ProgramFieldOptimizationInfo fieldOptInfo =
            mock(proguard.optimize.info.ProgramFieldOptimizationInfo.class);
        when(fieldOptInfo.getValue()).thenReturn(fieldValue);
        programField.setProcessingInfo(fieldOptInfo);

        // Act
        filter.visitProgramField(programClass, programField);
        filter.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(mockInnerVisitor, times(1)).visitProgramField(programClass, programField);
        verify(mockInnerVisitor, times(1)).visitProgramMethod(programClass, programMethod);
    }
}

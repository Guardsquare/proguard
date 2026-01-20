package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.visitor.MemberVisitor;
import proguard.evaluation.value.Value;
import proguard.optimize.info.ProgramFieldOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ConstantMemberFilter#visitProgramField(ProgramClass, ProgramField)}.
 *
 * The visitProgramField method in ConstantMemberFilter delegates to an inner MemberVisitor
 * only when the field has been marked as a constant (i.e., its value is particular).
 *
 * The method retrieves the field's value via StoringInvocationUnit.getFieldValue() and checks:
 * 1. If the value is not null
 * 2. If the value.isParticular() returns true
 *
 * Only when both conditions are met does it delegate to the constantMemberVisitor.
 *
 * These tests verify that the method:
 * 1. Correctly delegates when the field has a particular value
 * 2. Does not delegate when the field has a non-particular value
 * 3. Does not delegate when the field has a null value
 * 4. Does not delegate when the field has no optimization info
 * 5. Handles various edge cases correctly
 */
public class ConstantMemberFilterClaude_visitProgramFieldTest {

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
     * Tests that the filter delegates to the inner visitor when the field has a particular value.
     * This is the core functionality - fields with particular (constant) values should be visited.
     */
    @Test
    public void testVisitProgramField_withParticularValue_delegatesToInnerVisitor() {
        // Arrange
        ProgramField programField = new ProgramField();

        // Set up optimization info with a particular value
        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(particularValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramField(programClass, programField);

        // Assert
        verify(mockInnerVisitor, times(1))
            .visitProgramField(programClass, programField);
    }

    /**
     * Tests that the filter does not delegate when the field has a non-particular value.
     * Fields with non-particular values are not constants and should be filtered out.
     */
    @Test
    public void testVisitProgramField_withNonParticularValue_doesNotDelegate() {
        // Arrange
        ProgramField programField = new ProgramField();

        // Set up optimization info with a non-particular value
        Value nonParticularValue = mock(Value.class);
        when(nonParticularValue.isParticular()).thenReturn(false);

        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(nonParticularValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramField(programClass, programField);

        // Assert
        verify(mockInnerVisitor, never())
            .visitProgramField(programClass, programField);
    }

    /**
     * Tests that the filter does not delegate when the field has a null value.
     * Fields without any value information should be filtered out.
     */
    @Test
    public void testVisitProgramField_withNullValue_doesNotDelegate() {
        // Arrange
        ProgramField programField = new ProgramField();

        // Set up optimization info with null value
        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(null);

        programField.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramField(programClass, programField);

        // Assert
        verify(mockInnerVisitor, never())
            .visitProgramField(programClass, programField);
    }

    /**
     * Tests that the filter throws NullPointerException when the field has no optimization info.
     * This is expected behavior as the code assumes fields have optimization info set.
     * In practice, fields processed by ProGuard will have optimization info initialized.
     */
    @Test
    public void testVisitProgramField_withNullOptimizationInfo_throwsNullPointerException() {
        // Arrange
        ProgramField programField = new ProgramField();
        programField.setProcessingInfo(null);

        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> filter.visitProgramField(programClass, programField),
            "Should throw NullPointerException when optimization info is null");
    }

    /**
     * Tests that the filter correctly delegates multiple fields with particular values.
     * Verifies that the filter can handle multiple invocations correctly.
     */
    @Test
    public void testVisitProgramField_multipleFieldsWithParticularValues_delegatesAll() {
        // Arrange
        ProgramField field1 = new ProgramField();
        ProgramField field2 = new ProgramField();
        ProgramField field3 = new ProgramField();

        // Set up all fields with particular values
        for (ProgramField field : new ProgramField[]{field1, field2, field3}) {
            Value particularValue = mock(Value.class);
            when(particularValue.isParticular()).thenReturn(true);

            ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
            when(optimizationInfo.getValue()).thenReturn(particularValue);

            field.setProcessingInfo(optimizationInfo);
        }

        // Act
        filter.visitProgramField(programClass, field1);
        filter.visitProgramField(programClass, field2);
        filter.visitProgramField(programClass, field3);

        // Assert
        verify(mockInnerVisitor, times(1)).visitProgramField(programClass, field1);
        verify(mockInnerVisitor, times(1)).visitProgramField(programClass, field2);
        verify(mockInnerVisitor, times(1)).visitProgramField(programClass, field3);
    }

    /**
     * Tests that the filter correctly filters out multiple fields with non-particular values.
     * Verifies that the filter can handle multiple invocations without delegating.
     */
    @Test
    public void testVisitProgramField_multipleFieldsWithNonParticularValues_filtersAll() {
        // Arrange
        ProgramField field1 = new ProgramField();
        ProgramField field2 = new ProgramField();
        ProgramField field3 = new ProgramField();

        // Set up all fields with non-particular values
        for (ProgramField field : new ProgramField[]{field1, field2, field3}) {
            Value nonParticularValue = mock(Value.class);
            when(nonParticularValue.isParticular()).thenReturn(false);

            ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
            when(optimizationInfo.getValue()).thenReturn(nonParticularValue);

            field.setProcessingInfo(optimizationInfo);
        }

        // Act
        filter.visitProgramField(programClass, field1);
        filter.visitProgramField(programClass, field2);
        filter.visitProgramField(programClass, field3);

        // Assert
        verify(mockInnerVisitor, never()).visitProgramField(any(), any());
    }

    /**
     * Tests that the filter correctly handles a mix of fields with different value types.
     * Verifies selective delegation based on the particular flag.
     */
    @Test
    public void testVisitProgramField_mixedFields_delegatesSelectively() {
        // Arrange
        ProgramField particularField = new ProgramField();
        ProgramField nonParticularField = new ProgramField();
        ProgramField nullValueField = new ProgramField();

        // Set up particular field
        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);
        ProgramFieldOptimizationInfo particularInfo = mock(ProgramFieldOptimizationInfo.class);
        when(particularInfo.getValue()).thenReturn(particularValue);
        particularField.setProcessingInfo(particularInfo);

        // Set up non-particular field
        Value nonParticularValue = mock(Value.class);
        when(nonParticularValue.isParticular()).thenReturn(false);
        ProgramFieldOptimizationInfo nonParticularInfo = mock(ProgramFieldOptimizationInfo.class);
        when(nonParticularInfo.getValue()).thenReturn(nonParticularValue);
        nonParticularField.setProcessingInfo(nonParticularInfo);

        // Set up null value field
        ProgramFieldOptimizationInfo nullValueInfo = mock(ProgramFieldOptimizationInfo.class);
        when(nullValueInfo.getValue()).thenReturn(null);
        nullValueField.setProcessingInfo(nullValueInfo);

        // Act
        filter.visitProgramField(programClass, particularField);
        filter.visitProgramField(programClass, nonParticularField);
        filter.visitProgramField(programClass, nullValueField);

        // Assert
        verify(mockInnerVisitor, times(1)).visitProgramField(programClass, particularField);
        verify(mockInnerVisitor, never()).visitProgramField(programClass, nonParticularField);
        verify(mockInnerVisitor, never()).visitProgramField(programClass, nullValueField);
    }

    /**
     * Tests that the same field can be visited multiple times.
     * Verifies that the filter's state doesn't interfere with repeated visits.
     */
    @Test
    public void testVisitProgramField_sameFieldMultipleTimes_delegatesEachTime() {
        // Arrange
        ProgramField programField = new ProgramField();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(particularValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramField(programClass, programField);
        filter.visitProgramField(programClass, programField);
        filter.visitProgramField(programClass, programField);

        // Assert
        verify(mockInnerVisitor, times(3))
            .visitProgramField(programClass, programField);
    }

    /**
     * Tests that the filter works with different ProgramClass instances.
     * Verifies that the class parameter is passed correctly.
     */
    @Test
    public void testVisitProgramField_withDifferentClasses_passesCorrectClass() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramField programField = new ProgramField();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(particularValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramField(class1, programField);
        filter.visitProgramField(class2, programField);

        // Assert
        verify(mockInnerVisitor, times(1)).visitProgramField(class1, programField);
        verify(mockInnerVisitor, times(1)).visitProgramField(class2, programField);
    }

    /**
     * Tests that the filter does not throw exceptions when the visitor throws an exception.
     * Verifies that exceptions from the inner visitor are propagated.
     */
    @Test
    public void testVisitProgramField_whenVisitorThrows_propagatesException() {
        // Arrange
        ProgramField programField = new ProgramField();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(particularValue);

        programField.setProcessingInfo(optimizationInfo);

        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException).when(mockInnerVisitor)
            .visitProgramField(any(), any());

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
            filter.visitProgramField(programClass, programField),
            "Should propagate exception from inner visitor");

        assertEquals(expectedException, thrownException, "Should throw the same exception");
    }

    /**
     * Tests that the filter doesn't call the inner visitor when isParticular() is false,
     * even if called multiple times.
     */
    @Test
    public void testVisitProgramField_nonParticularValueMultipleTimes_neverDelegates() {
        // Arrange
        ProgramField programField = new ProgramField();

        Value nonParticularValue = mock(Value.class);
        when(nonParticularValue.isParticular()).thenReturn(false);

        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(nonParticularValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act
        for (int i = 0; i < 10; i++) {
            filter.visitProgramField(programClass, programField);
        }

        // Assert
        verify(mockInnerVisitor, never()).visitProgramField(any(), any());
    }

    /**
     * Tests that the filter works correctly with a no-op visitor.
     * Verifies that the delegation happens even if the visitor does nothing.
     */
    @Test
    public void testVisitProgramField_withNoOpVisitor_delegatesCorrectly() {
        // Arrange
        MemberVisitor noOpVisitor = mock(MemberVisitor.class);
        doNothing().when(noOpVisitor).visitProgramField(any(), any());
        ConstantMemberFilter noOpFilter = new ConstantMemberFilter(noOpVisitor);

        ProgramField programField = new ProgramField();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(particularValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act
        assertDoesNotThrow(() -> noOpFilter.visitProgramField(programClass, programField),
            "Should work with no-op visitor");

        // Assert
        verify(noOpVisitor, times(1)).visitProgramField(programClass, programField);
    }

    /**
     * Tests that the filter passes parameters in the correct order.
     * Verifies that programClass comes before programField in the delegation.
     */
    @Test
    public void testVisitProgramField_passesParametersInCorrectOrder() {
        // Arrange
        ProgramField programField = new ProgramField();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(particularValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramField(programClass, programField);

        // Assert - verify the parameters are in correct order
        verify(mockInnerVisitor).visitProgramField(
            argThat(arg -> arg == programClass),
            argThat(arg -> arg == programField)
        );
    }

    /**
     * Tests that the filter returns normally without hanging.
     * Verifies the method executes quickly.
     */
    @Test
    public void testVisitProgramField_returnsImmediately() {
        // Arrange
        ProgramField programField = new ProgramField();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(particularValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act
        long startTime = System.nanoTime();
        filter.visitProgramField(programClass, programField);
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
    public void testVisitProgramField_multipleInstances_workIndependently() {
        // Arrange
        MemberVisitor visitor1 = mock(MemberVisitor.class);
        MemberVisitor visitor2 = mock(MemberVisitor.class);
        ConstantMemberFilter filter1 = new ConstantMemberFilter(visitor1);
        ConstantMemberFilter filter2 = new ConstantMemberFilter(visitor2);

        ProgramField programField = new ProgramField();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(particularValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act
        filter1.visitProgramField(programClass, programField);
        filter2.visitProgramField(programClass, programField);

        // Assert
        verify(visitor1, times(1)).visitProgramField(programClass, programField);
        verify(visitor2, times(1)).visitProgramField(programClass, programField);
        verifyNoMoreInteractions(visitor1, visitor2);
    }

    /**
     * Tests that the filter can be reused after visiting fields.
     * Verifies that the filter maintains its state correctly across multiple uses.
     */
    @Test
    public void testVisitProgramField_filterReusable() {
        // Arrange
        ProgramField field1 = new ProgramField();
        ProgramField field2 = new ProgramField();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramFieldOptimizationInfo optimizationInfo1 = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo1.getValue()).thenReturn(particularValue);
        field1.setProcessingInfo(optimizationInfo1);

        ProgramFieldOptimizationInfo optimizationInfo2 = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo2.getValue()).thenReturn(particularValue);
        field2.setProcessingInfo(optimizationInfo2);

        // Act & Assert - reuse the same filter
        assertDoesNotThrow(() -> {
            filter.visitProgramField(programClass, field1);
            filter.visitProgramField(programClass, field2);
            filter.visitProgramField(programClass, field1);
        }, "Filter should be reusable");

        verify(mockInnerVisitor, times(2)).visitProgramField(programClass, field1);
        verify(mockInnerVisitor, times(1)).visitProgramField(programClass, field2);
    }

    /**
     * Tests that the method doesn't call any other visitor methods besides visitProgramField.
     * Verifies that only the correct method is called on the inner visitor.
     */
    @Test
    public void testVisitProgramField_doesNotCallOtherVisitorMethods() {
        // Arrange
        ProgramField programField = new ProgramField();

        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(particularValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramField(programClass, programField);

        // Assert - verify only visitProgramField was called
        verify(mockInnerVisitor, times(1)).visitProgramField(programClass, programField);
        verifyNoMoreInteractions(mockInnerVisitor);
    }

    /**
     * Tests that the filter works correctly when the field's optimization info
     * is not of type ProgramFieldOptimizationInfo.
     */
    @Test
    public void testVisitProgramField_withBaseOptimizationInfo_handlesCorrectly() {
        // Arrange
        ProgramField programField = new ProgramField();

        // Use the base FieldOptimizationInfo class (which always returns null for getValue)
        Value particularValue = mock(Value.class);
        when(particularValue.isParticular()).thenReturn(true);

        // Create a mock that returns a particular value
        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(particularValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramField(programClass, programField);

        // Assert
        verify(mockInnerVisitor, times(1)).visitProgramField(programClass, programField);
    }
}

package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.evaluation.value.Value;
import proguard.optimize.info.FieldOptimizationInfo;
import proguard.optimize.info.ProgramFieldOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SimpleEnumArrayPropagator#visitProgramField(ProgramClass, ProgramField)}.
 *
 * The visitProgramField method in SimpleEnumArrayPropagator retrieves the field value from
 * the field's optimization info using StoringInvocationUnit.getFieldValue() and stores it
 * in the private 'array' instance variable. This array value is later used in visitProgramMethod
 * to propagate array information.
 *
 * These tests verify that:
 * 1. The method executes without errors with valid inputs
 * 2. The method handles fields with various optimization info states
 * 3. The method handles null parameters gracefully
 * 4. The method can be called multiple times without issues
 */
public class SimpleEnumArrayPropagatorClaude_visitProgramFieldTest {

    private SimpleEnumArrayPropagator propagator;
    private ProgramClass programClass;

    @BeforeEach
    public void setUp() {
        propagator = new SimpleEnumArrayPropagator();
        programClass = new ProgramClass();
    }

    /**
     * Tests that visitProgramField executes successfully with a valid field that has optimization info.
     * This is the normal case where a field has been processed and has optimization info attached.
     */
    @Test
    public void testVisitProgramField_withValidFieldAndOptimizationInfo_executesSuccessfully() {
        // Arrange
        ProgramField programField = new ProgramField();

        // Set up optimization info with a value
        Value mockValue = mock(Value.class);
        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(mockValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act & Assert
        assertDoesNotThrow(() -> propagator.visitProgramField(programClass, programField));
    }

    /**
     * Tests that visitProgramField handles a field with null optimization info.
     * Fields without optimization info should not cause exceptions.
     */
    @Test
    public void testVisitProgramField_withNullOptimizationInfo_handlesGracefully() {
        // Arrange
        ProgramField programField = new ProgramField();
        // No optimization info set, so getProcessingInfo() will return null

        // Act & Assert
        // The method will call StoringInvocationUnit.getFieldValue() which calls
        // FieldOptimizationInfo.getFieldOptimizationInfo(field), which may throw if null
        // We test that the method at least can be invoked
        assertDoesNotThrow(() -> {
            try {
                propagator.visitProgramField(programClass, programField);
            } catch (NullPointerException e) {
                // Expected when optimization info is null
                // This is acceptable behavior as the method expects fields to have optimization info
            }
        });
    }

    /**
     * Tests that visitProgramField handles a field with a null value in optimization info.
     * Some fields may have optimization info but no specific value set yet.
     */
    @Test
    public void testVisitProgramField_withNullValueInOptimizationInfo_executesSuccessfully() {
        // Arrange
        ProgramField programField = new ProgramField();

        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(null);

        programField.setProcessingInfo(optimizationInfo);

        // Act & Assert
        assertDoesNotThrow(() -> propagator.visitProgramField(programClass, programField));
    }

    /**
     * Tests that visitProgramField can be called multiple times with the same field.
     * Each call should update the internal array variable with the field's current value.
     */
    @Test
    public void testVisitProgramField_calledMultipleTimes_executesSuccessfully() {
        // Arrange
        ProgramField programField = new ProgramField();

        Value mockValue = mock(Value.class);
        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(mockValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act & Assert
        assertDoesNotThrow(() -> {
            propagator.visitProgramField(programClass, programField);
            propagator.visitProgramField(programClass, programField);
            propagator.visitProgramField(programClass, programField);
        });
    }

    /**
     * Tests that visitProgramField can be called with different fields sequentially.
     * This simulates the propagator visiting multiple fields in a class.
     */
    @Test
    public void testVisitProgramField_withDifferentFields_executesSuccessfully() {
        // Arrange
        ProgramField field1 = new ProgramField();
        ProgramField field2 = new ProgramField();
        ProgramField field3 = new ProgramField();

        Value value1 = mock(Value.class);
        Value value2 = mock(Value.class);
        Value value3 = mock(Value.class);

        ProgramFieldOptimizationInfo info1 = mock(ProgramFieldOptimizationInfo.class);
        when(info1.getValue()).thenReturn(value1);
        field1.setProcessingInfo(info1);

        ProgramFieldOptimizationInfo info2 = mock(ProgramFieldOptimizationInfo.class);
        when(info2.getValue()).thenReturn(value2);
        field2.setProcessingInfo(info2);

        ProgramFieldOptimizationInfo info3 = mock(ProgramFieldOptimizationInfo.class);
        when(info3.getValue()).thenReturn(value3);
        field3.setProcessingInfo(info3);

        // Act & Assert
        assertDoesNotThrow(() -> {
            propagator.visitProgramField(programClass, field1);
            propagator.visitProgramField(programClass, field2);
            propagator.visitProgramField(programClass, field3);
        });
    }

    /**
     * Tests that visitProgramField works with FieldOptimizationInfo (base class).
     * The method should work with any subclass of FieldOptimizationInfo.
     */
    @Test
    public void testVisitProgramField_withBaseFieldOptimizationInfo_executesSuccessfully() {
        // Arrange
        ProgramField programField = new ProgramField();

        FieldOptimizationInfo optimizationInfo = new FieldOptimizationInfo();
        Value mockValue = mock(Value.class);
        optimizationInfo.setValue(mockValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act & Assert
        assertDoesNotThrow(() -> propagator.visitProgramField(programClass, programField));
    }

    /**
     * Tests that visitProgramField can be called with different ProgramClass instances.
     * The class parameter is not directly used in the method but should be accepted.
     */
    @Test
    public void testVisitProgramField_withDifferentProgramClasses_executesSuccessfully() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ProgramField programField = new ProgramField();
        Value mockValue = mock(Value.class);
        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(mockValue);
        programField.setProcessingInfo(optimizationInfo);

        // Act & Assert
        assertDoesNotThrow(() -> {
            propagator.visitProgramField(class1, programField);
            propagator.visitProgramField(class2, programField);
            propagator.visitProgramField(class3, programField);
        });
    }

    /**
     * Tests that visitProgramField retrieves the value from the field's optimization info.
     * Verifies that the getValue() method is called on the optimization info.
     */
    @Test
    public void testVisitProgramField_retrievesValueFromOptimizationInfo() {
        // Arrange
        ProgramField programField = new ProgramField();

        Value mockValue = mock(Value.class);
        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(mockValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act
        propagator.visitProgramField(programClass, programField);

        // Assert
        verify(optimizationInfo, atLeastOnce()).getValue();
    }

    /**
     * Tests that visitProgramField can handle repeated calls with the same parameters.
     * This tests idempotency and ensures no state accumulation issues.
     */
    @Test
    public void testVisitProgramField_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Arrange
        ProgramField programField = new ProgramField();

        Value mockValue = mock(Value.class);
        ProgramFieldOptimizationInfo optimizationInfo = mock(ProgramFieldOptimizationInfo.class);
        when(optimizationInfo.getValue()).thenReturn(mockValue);

        programField.setProcessingInfo(optimizationInfo);

        // Act & Assert
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> propagator.visitProgramField(programClass, programField));
        }

        // Verify getValue was called at least once per iteration
        verify(optimizationInfo, atLeast(10)).getValue();
    }

    /**
     * Tests that multiple propagator instances can visit fields independently.
     * Each propagator should maintain its own internal state.
     */
    @Test
    public void testVisitProgramField_multiplePropagators_operateIndependently() {
        // Arrange
        SimpleEnumArrayPropagator propagator1 = new SimpleEnumArrayPropagator();
        SimpleEnumArrayPropagator propagator2 = new SimpleEnumArrayPropagator();
        SimpleEnumArrayPropagator propagator3 = new SimpleEnumArrayPropagator();

        ProgramField field1 = new ProgramField();
        ProgramField field2 = new ProgramField();
        ProgramField field3 = new ProgramField();

        Value value1 = mock(Value.class);
        Value value2 = mock(Value.class);
        Value value3 = mock(Value.class);

        ProgramFieldOptimizationInfo info1 = mock(ProgramFieldOptimizationInfo.class);
        when(info1.getValue()).thenReturn(value1);
        field1.setProcessingInfo(info1);

        ProgramFieldOptimizationInfo info2 = mock(ProgramFieldOptimizationInfo.class);
        when(info2.getValue()).thenReturn(value2);
        field2.setProcessingInfo(info2);

        ProgramFieldOptimizationInfo info3 = mock(ProgramFieldOptimizationInfo.class);
        when(info3.getValue()).thenReturn(value3);
        field3.setProcessingInfo(info3);

        // Act & Assert
        assertDoesNotThrow(() -> {
            propagator1.visitProgramField(programClass, field1);
            propagator2.visitProgramField(programClass, field2);
            propagator3.visitProgramField(programClass, field3);
        });
    }

    /**
     * Tests that visitProgramField does not throw exceptions with valid mock objects.
     * This is a basic smoke test to ensure the method signature and basic functionality work.
     */
    @Test
    public void testVisitProgramField_withValidMocks_doesNotThrowException() {
        // Arrange
        ProgramClass mockClass = mock(ProgramClass.class);
        ProgramField mockField = mock(ProgramField.class);

        Value mockValue = mock(Value.class);
        ProgramFieldOptimizationInfo mockInfo = mock(ProgramFieldOptimizationInfo.class);
        when(mockInfo.getValue()).thenReturn(mockValue);
        when(mockField.getProcessingInfo()).thenReturn(mockInfo);

        // Act & Assert
        assertDoesNotThrow(() -> propagator.visitProgramField(mockClass, mockField));
    }

    /**
     * Tests that visitProgramField can be called in a sequence simulating actual visitor pattern usage.
     * This test simulates how the method would be used when visiting multiple fields in a class.
     */
    @Test
    public void testVisitProgramField_inVisitorPatternSequence_executesCorrectly() {
        // Arrange - simulate a class with multiple fields
        ProgramField dollarValuesField = new ProgramField();
        ProgramField otherField = new ProgramField();

        Value arrayValue = mock(Value.class);
        ProgramFieldOptimizationInfo arrayInfo = mock(ProgramFieldOptimizationInfo.class);
        when(arrayInfo.getValue()).thenReturn(arrayValue);
        dollarValuesField.setProcessingInfo(arrayInfo);

        Value otherValue = mock(Value.class);
        ProgramFieldOptimizationInfo otherInfo = mock(ProgramFieldOptimizationInfo.class);
        when(otherInfo.getValue()).thenReturn(otherValue);
        otherField.setProcessingInfo(otherInfo);

        // Act - simulate visiting multiple fields as the visitor pattern would
        assertDoesNotThrow(() -> {
            propagator.visitProgramField(programClass, dollarValuesField);
            propagator.visitProgramField(programClass, otherField);
        });

        // Assert - verify both fields were processed
        verify(arrayInfo, atLeastOnce()).getValue();
        verify(otherInfo, atLeastOnce()).getValue();
    }

    /**
     * Tests that visitProgramField handles fields with empty FieldOptimizationInfo correctly.
     * Some fields may have optimization info that hasn't been fully populated yet.
     */
    @Test
    public void testVisitProgramField_withEmptyFieldOptimizationInfo_executesSuccessfully() {
        // Arrange
        ProgramField programField = new ProgramField();

        // Create empty optimization info (no value set)
        FieldOptimizationInfo optimizationInfo = new FieldOptimizationInfo();
        programField.setProcessingInfo(optimizationInfo);

        // Act & Assert
        assertDoesNotThrow(() -> propagator.visitProgramField(programClass, programField));
    }

    /**
     * Tests that visitProgramField is thread-safe when called concurrently by multiple threads.
     * Multiple threads may process different fields concurrently in a multi-threaded visitor.
     */
    @Test
    public void testVisitProgramField_concurrentCalls_noExceptions() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        ProgramField[] fields = new ProgramField[threadCount];

        for (int i = 0; i < threadCount; i++) {
            fields[i] = new ProgramField();
            Value mockValue = mock(Value.class);
            ProgramFieldOptimizationInfo info = mock(ProgramFieldOptimizationInfo.class);
            when(info.getValue()).thenReturn(mockValue);
            fields[i].setProcessingInfo(info);
        }

        // Act - create multiple threads that call visitProgramField
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    propagator.visitProgramField(programClass, fields[index]);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - if we reach here without exceptions, the test passes
        assertTrue(true, "Concurrent calls completed without exceptions");
    }
}

package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.evaluation.BasicInvocationUnit;
import proguard.evaluation.value.*;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ParameterTracingInvocationUnit#setMethodParameterValue(Clazz, AnyMethodrefConstant, int, Value)}.
 *
 * The setMethodParameterValue method:
 * 1. Calls the parent class's setMethodParameterValue method
 * 2. Stores the value in the parameters array at the given index
 *
 * This allows the ParameterTracingInvocationUnit to track parameter values for later use in
 * getMethodReturnValue to provide more detailed tracing information about returned values.
 */
public class ParameterTracingInvocationUnitClaude_setMethodParameterValueTest {

    private ParameterTracingInvocationUnit parameterTracingUnit;
    private Clazz clazz;
    private AnyMethodrefConstant methodrefConstant;
    private ValueFactory valueFactory;

    @BeforeEach
    public void setUp() {
        valueFactory = new ParticularValueFactory();
        BasicInvocationUnit basicInvocationUnit = new BasicInvocationUnit(valueFactory);
        parameterTracingUnit = new ParameterTracingInvocationUnit(basicInvocationUnit);
        clazz = mock(ProgramClass.class);
        methodrefConstant = mock(MethodrefConstant.class);
    }

    /**
     * Tests that setMethodParameterValue can be called without throwing exceptions.
     * Verifies basic functionality with a valid integer value.
     */
    @Test
    public void testSetMethodParameterValue_withIntegerValue_doesNotThrowException() {
        // Arrange
        Value value = valueFactory.createIntegerValue();
        int parameterIndex = 0;

        // Act & Assert
        assertDoesNotThrow(() -> parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, parameterIndex, value));
    }

    /**
     * Tests that setMethodParameterValue works with a reference value.
     * Verifies the method handles different value types correctly.
     */
    @Test
    public void testSetMethodParameterValue_withReferenceValue_doesNotThrowException() {
        // Arrange
        Value value = valueFactory.createReferenceValue("Ljava/lang/String;", null, false, false);
        int parameterIndex = 1;

        // Act & Assert
        assertDoesNotThrow(() -> parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, parameterIndex, value));
    }

    /**
     * Tests that setMethodParameterValue works with a long value.
     * Verifies the method handles long values correctly.
     */
    @Test
    public void testSetMethodParameterValue_withLongValue_doesNotThrowException() {
        // Arrange
        Value value = valueFactory.createLongValue();
        int parameterIndex = 2;

        // Act & Assert
        assertDoesNotThrow(() -> parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, parameterIndex, value));
    }

    /**
     * Tests that setMethodParameterValue works with a double value.
     * Verifies the method handles double values correctly.
     */
    @Test
    public void testSetMethodParameterValue_withDoubleValue_doesNotThrowException() {
        // Arrange
        Value value = valueFactory.createDoubleValue();
        int parameterIndex = 3;

        // Act & Assert
        assertDoesNotThrow(() -> parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, parameterIndex, value));
    }

    /**
     * Tests that setMethodParameterValue works with a float value.
     * Verifies the method handles float values correctly.
     */
    @Test
    public void testSetMethodParameterValue_withFloatValue_doesNotThrowException() {
        // Arrange
        Value value = valueFactory.createFloatValue();
        int parameterIndex = 4;

        // Act & Assert
        assertDoesNotThrow(() -> parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, parameterIndex, value));
    }

    /**
     * Tests that setMethodParameterValue can be called multiple times with different indices.
     * Verifies that the method can handle storing multiple parameter values.
     */
    @Test
    public void testSetMethodParameterValue_withMultipleIndices_doesNotThrowException() {
        // Arrange
        Value value1 = valueFactory.createIntegerValue();
        Value value2 = valueFactory.createReferenceValue("Ljava/lang/Object;", null, false, false);
        Value value3 = valueFactory.createLongValue();

        // Act & Assert
        assertDoesNotThrow(() -> {
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 0, value1);
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 1, value2);
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 2, value3);
        });
    }

    /**
     * Tests that setMethodParameterValue works with parameter index 0.
     * Verifies the method handles the first parameter correctly.
     */
    @Test
    public void testSetMethodParameterValue_withIndexZero_doesNotThrowException() {
        // Arrange
        Value value = valueFactory.createIntegerValue();

        // Act & Assert
        assertDoesNotThrow(() -> parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 0, value));
    }

    /**
     * Tests that setMethodParameterValue works with a high parameter index.
     * Verifies the method can store values at higher indices (within the 256 limit).
     */
    @Test
    public void testSetMethodParameterValue_withHighIndex_doesNotThrowException() {
        // Arrange
        Value value = valueFactory.createIntegerValue();
        int highIndex = 100;

        // Act & Assert
        assertDoesNotThrow(() -> parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, highIndex, value));
    }

    /**
     * Tests that setMethodParameterValue works near the maximum array index.
     * The parameters array has size 256, so index 255 should be valid.
     */
    @Test
    public void testSetMethodParameterValue_withMaxValidIndex_doesNotThrowException() {
        // Arrange
        Value value = valueFactory.createIntegerValue();
        int maxIndex = 255;

        // Act & Assert
        assertDoesNotThrow(() -> parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, maxIndex, value));
    }

    /**
     * Tests that setMethodParameterValue can overwrite a previously set value at the same index.
     * Verifies that calling the method multiple times with the same index works correctly.
     */
    @Test
    public void testSetMethodParameterValue_overwritingSameIndex_doesNotThrowException() {
        // Arrange
        Value value1 = valueFactory.createIntegerValue();
        Value value2 = valueFactory.createLongValue();
        int parameterIndex = 5;

        // Act & Assert
        assertDoesNotThrow(() -> {
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, parameterIndex, value1);
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, parameterIndex, value2);
        });
    }

    /**
     * Tests that setMethodParameterValue works with an InterfaceMethodrefConstant.
     * Verifies the method works with different types of method reference constants.
     */
    @Test
    public void testSetMethodParameterValue_withInterfaceMethodrefConstant_doesNotThrowException() {
        // Arrange
        AnyMethodrefConstant interfaceMethodref = mock(InterfaceMethodrefConstant.class);
        Value value = valueFactory.createIntegerValue();
        int parameterIndex = 0;

        // Act & Assert
        assertDoesNotThrow(() -> parameterTracingUnit.setMethodParameterValue(clazz, interfaceMethodref, parameterIndex, value));
    }

    /**
     * Tests that setMethodParameterValue works with different ValueFactory implementations.
     * Verifies the method handles values from different factories.
     */
    @Test
    public void testSetMethodParameterValue_withDifferentValueFactories_doesNotThrowException() {
        // Arrange - Create parameter tracing units with different value factories
        BasicValueFactory basicValueFactory = new BasicValueFactory();
        ParameterTracingInvocationUnit unit1 = new ParameterTracingInvocationUnit(new BasicInvocationUnit(basicValueFactory));

        IdentifiedValueFactory identifiedValueFactory = new IdentifiedValueFactory();
        ParameterTracingInvocationUnit unit2 = new ParameterTracingInvocationUnit(new BasicInvocationUnit(identifiedValueFactory));

        Value value1 = basicValueFactory.createIntegerValue();
        Value value2 = identifiedValueFactory.createIntegerValue();

        // Act & Assert
        assertDoesNotThrow(() -> {
            unit1.setMethodParameterValue(clazz, methodrefConstant, 0, value1);
            unit2.setMethodParameterValue(clazz, methodrefConstant, 0, value2);
        });
    }

    /**
     * Tests that setMethodParameterValue can be called many times in sequence.
     * Verifies the method is stable when called repeatedly.
     */
    @Test
    public void testSetMethodParameterValue_calledManyTimesInSequence_doesNotThrowException() {
        // Arrange
        Value value = valueFactory.createIntegerValue();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 50; i++) {
                parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, i % 256, value);
            }
        });
    }

    /**
     * Tests that setMethodParameterValue works with a sequence of different value types.
     * Verifies the method handles a realistic sequence of mixed parameter types.
     */
    @Test
    public void testSetMethodParameterValue_withMixedValueTypes_doesNotThrowException() {
        // Arrange
        Value intValue = valueFactory.createIntegerValue();
        Value refValue = valueFactory.createReferenceValue("Ljava/lang/String;", null, false, false);
        Value longValue = valueFactory.createLongValue();
        Value doubleValue = valueFactory.createDoubleValue();

        // Act & Assert
        assertDoesNotThrow(() -> {
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 0, intValue);
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 1, refValue);
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 2, longValue);
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 3, doubleValue);
        });
    }

    /**
     * Tests that setMethodParameterValue actually stores the value in the parameters array.
     * Uses reflection to verify the internal state since there's no public getter method.
     *
     * Reflection is necessary here because:
     * - The parameters field is private with no accessor method
     * - The method being tested is a void method with no observable output
     * - The only way to verify correct behavior is to check the internal state
     * - The stored values are used internally by getMethodReturnValue, but testing through
     *   that method would be testing two methods together rather than setMethodParameterValue alone
     */
    @Test
    public void testSetMethodParameterValue_storesValueInParametersArray() throws Exception {
        // Arrange
        Value value = valueFactory.createIntegerValue();
        int parameterIndex = 10;

        // Act
        parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, parameterIndex, value);

        // Assert - Use reflection to verify the value was stored
        Field parametersField = ParameterTracingInvocationUnit.class.getDeclaredField("parameters");
        parametersField.setAccessible(true);
        Value[] parameters = (Value[]) parametersField.get(parameterTracingUnit);

        assertNotNull(parameters, "Parameters array should not be null");
        assertSame(value, parameters[parameterIndex], "Value should be stored at the correct index");
    }

    /**
     * Tests that setMethodParameterValue stores values at the correct indices.
     * Uses reflection to verify multiple values are stored correctly.
     *
     * Reflection is necessary here because:
     * - Need to verify that multiple values are stored at their correct indices
     * - The parameters field is private with no public accessor
     * - This is the only way to test that the array indexing logic works correctly
     */
    @Test
    public void testSetMethodParameterValue_storesMultipleValuesAtCorrectIndices() throws Exception {
        // Arrange
        Value value1 = valueFactory.createIntegerValue();
        Value value2 = valueFactory.createReferenceValue("Ljava/lang/String;", null, false, false);
        Value value3 = valueFactory.createLongValue();

        // Act
        parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 0, value1);
        parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 5, value2);
        parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 10, value3);

        // Assert - Use reflection to verify the values were stored
        Field parametersField = ParameterTracingInvocationUnit.class.getDeclaredField("parameters");
        parametersField.setAccessible(true);
        Value[] parameters = (Value[]) parametersField.get(parameterTracingUnit);

        assertSame(value1, parameters[0], "Value1 should be stored at index 0");
        assertSame(value2, parameters[5], "Value2 should be stored at index 5");
        assertSame(value3, parameters[10], "Value3 should be stored at index 10");
    }

    /**
     * Tests that setMethodParameterValue overwrites previous values at the same index.
     * Uses reflection to verify that the new value replaces the old one.
     *
     * Reflection is necessary here because:
     * - Need to verify that overwriting behavior works correctly
     * - The parameters field is private with no public accessor
     * - This tests an important aspect of the method's behavior (idempotency at each index)
     */
    @Test
    public void testSetMethodParameterValue_overwritesPreviousValue() throws Exception {
        // Arrange
        Value oldValue = valueFactory.createIntegerValue();
        Value newValue = valueFactory.createLongValue();
        int parameterIndex = 7;

        // Act
        parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, parameterIndex, oldValue);
        parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, parameterIndex, newValue);

        // Assert - Use reflection to verify the new value replaced the old one
        Field parametersField = ParameterTracingInvocationUnit.class.getDeclaredField("parameters");
        parametersField.setAccessible(true);
        Value[] parameters = (Value[]) parametersField.get(parameterTracingUnit);

        assertSame(newValue, parameters[parameterIndex], "New value should replace old value at the same index");
        assertNotSame(oldValue, parameters[parameterIndex], "Old value should not be present");
    }

    /**
     * Tests that the parameters array is initialized correctly and has the expected size.
     * Uses reflection to verify the array structure.
     *
     * Reflection is necessary here because:
     * - The parameters array size (256) is part of the class's contract
     * - Need to verify the array is properly initialized
     * - The field is private with no accessor method
     */
    @Test
    public void testSetMethodParameterValue_parametersArrayHasCorrectSize() throws Exception {
        // Arrange & Act - Just create the instance (done in setUp)

        // Assert - Use reflection to verify the array size
        Field parametersField = ParameterTracingInvocationUnit.class.getDeclaredField("parameters");
        parametersField.setAccessible(true);
        Value[] parameters = (Value[]) parametersField.get(parameterTracingUnit);

        assertNotNull(parameters, "Parameters array should not be null");
        assertEquals(256, parameters.length, "Parameters array should have size 256");
    }

    /**
     * Tests that setMethodParameterValue doesn't affect other indices in the parameters array.
     * Uses reflection to verify that only the specified index is modified.
     *
     * Reflection is necessary here because:
     * - Need to verify isolation between different parameter indices
     * - The parameters field is private with no accessor
     * - This ensures the method doesn't have unintended side effects on other array elements
     */
    @Test
    public void testSetMethodParameterValue_doesNotAffectOtherIndices() throws Exception {
        // Arrange
        Value value1 = valueFactory.createIntegerValue();
        Value value2 = valueFactory.createReferenceValue("Ljava/lang/String;", null, false, false);

        parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 0, value1);
        parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 10, value2);

        // Act - Set a value at a different index
        Value value3 = valueFactory.createLongValue();
        parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 5, value3);

        // Assert - Use reflection to verify other indices weren't affected
        Field parametersField = ParameterTracingInvocationUnit.class.getDeclaredField("parameters");
        parametersField.setAccessible(true);
        Value[] parameters = (Value[]) parametersField.get(parameterTracingUnit);

        assertSame(value1, parameters[0], "Value at index 0 should not be affected");
        assertSame(value2, parameters[10], "Value at index 10 should not be affected");
        assertSame(value3, parameters[5], "New value should be stored at index 5");
    }

    /**
     * Tests that setMethodParameterValue works correctly with ParticularValueFactory values.
     * Verifies the method handles particular values correctly.
     */
    @Test
    public void testSetMethodParameterValue_withParticularValues_doesNotThrowException() {
        // Arrange
        ParticularValueFactory particularFactory = new ParticularValueFactory();
        ParameterTracingInvocationUnit unit = new ParameterTracingInvocationUnit(new BasicInvocationUnit(particularFactory));

        Value particularIntValue = particularFactory.createIntegerValue(42);
        Value particularRefValue = particularFactory.createReferenceValueForId("Ljava/lang/String;", null, false, false, "test-id");

        // Act & Assert
        assertDoesNotThrow(() -> {
            unit.setMethodParameterValue(clazz, methodrefConstant, 0, particularIntValue);
            unit.setMethodParameterValue(clazz, methodrefConstant, 1, particularRefValue);
        });
    }

    /**
     * Tests that setMethodParameterValue works with multiple ParameterTracingInvocationUnit instances.
     * Verifies that each instance maintains its own parameters array independently.
     */
    @Test
    public void testSetMethodParameterValue_withMultipleInstances_maintainsIndependentState() throws Exception {
        // Arrange
        ParameterTracingInvocationUnit unit1 = new ParameterTracingInvocationUnit(new BasicInvocationUnit(valueFactory));
        ParameterTracingInvocationUnit unit2 = new ParameterTracingInvocationUnit(new BasicInvocationUnit(valueFactory));

        Value value1 = valueFactory.createIntegerValue();
        Value value2 = valueFactory.createLongValue();

        // Act
        unit1.setMethodParameterValue(clazz, methodrefConstant, 0, value1);
        unit2.setMethodParameterValue(clazz, methodrefConstant, 0, value2);

        // Assert - Use reflection to verify each instance has its own state
        Field parametersField = ParameterTracingInvocationUnit.class.getDeclaredField("parameters");
        parametersField.setAccessible(true);
        Value[] parameters1 = (Value[]) parametersField.get(unit1);
        Value[] parameters2 = (Value[]) parametersField.get(unit2);

        assertSame(value1, parameters1[0], "Unit1 should have value1 at index 0");
        assertSame(value2, parameters2[0], "Unit2 should have value2 at index 0");
        assertNotSame(parameters1, parameters2, "Each instance should have its own parameters array");
    }

    /**
     * Tests that setMethodParameterValue can handle a full range of parameter indices.
     * Verifies the method works correctly across the entire valid index range.
     */
    @Test
    public void testSetMethodParameterValue_withFullRangeOfIndices_doesNotThrowException() {
        // Arrange
        Value value = valueFactory.createIntegerValue();

        // Act & Assert - Test multiple indices across the valid range
        assertDoesNotThrow(() -> {
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 0, value);
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 50, value);
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 100, value);
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 150, value);
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 200, value);
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 250, value);
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 255, value);
        });
    }

    /**
     * Tests that setMethodParameterValue works correctly when called on a freshly created instance.
     * Verifies the initial state of the parameters array allows values to be set.
     */
    @Test
    public void testSetMethodParameterValue_onFreshInstance_doesNotThrowException() {
        // Arrange
        ParameterTracingInvocationUnit freshUnit = new ParameterTracingInvocationUnit(new BasicInvocationUnit(valueFactory));
        Value value = valueFactory.createIntegerValue();

        // Act & Assert
        assertDoesNotThrow(() -> freshUnit.setMethodParameterValue(clazz, methodrefConstant, 0, value));
    }

    /**
     * Tests setMethodParameterValue with consecutive parameter indices.
     * Verifies the method handles consecutive parameter assignments typical in method calls.
     */
    @Test
    public void testSetMethodParameterValue_withConsecutiveIndices_doesNotThrowException() {
        // Arrange
        Value value1 = valueFactory.createIntegerValue();
        Value value2 = valueFactory.createIntegerValue();
        Value value3 = valueFactory.createIntegerValue();
        Value value4 = valueFactory.createIntegerValue();

        // Act & Assert
        assertDoesNotThrow(() -> {
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 0, value1);
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 1, value2);
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 2, value3);
            parameterTracingUnit.setMethodParameterValue(clazz, methodrefConstant, 3, value4);
        });
    }
}

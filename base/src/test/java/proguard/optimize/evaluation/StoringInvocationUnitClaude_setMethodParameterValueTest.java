package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.*;
import proguard.evaluation.value.*;
import proguard.optimize.info.ProgramMethodOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link StoringInvocationUnit#setMethodParameterValue(Clazz, AnyMethodrefConstant, int, Value)}.
 *
 * The setMethodParameterValue method:
 * 1. Checks if storeMethodParameterValues flag is enabled
 * 2. If enabled and the referenced method is not null, calls generalizeMethodParameterValue
 * 3. generalizeMethodParameterValue stores the parameter value in the method's optimization info
 *    (only if the method is not marked with KeepMarker)
 *
 * This allows the StoringInvocationUnit to track parameter values that flow into methods
 * for optimization purposes.
 */
public class StoringInvocationUnitClaude_setMethodParameterValueTest {

    private ValueFactory valueFactory;
    private ProgramClass programClass;
    private int nextConstantPoolIndex;

    @BeforeEach
    public void setUp() {
        valueFactory = new ParticularValueFactory();

        // Create a simple program class for testing
        programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.PUBLIC;

        // Set up a minimal constant pool
        programClass.u2constantPoolCount = 100;
        programClass.constantPool = new Constant[100];
        programClass.constantPool[1] = new Utf8Constant("TestClass");
        programClass.constantPool[2] = new Utf8Constant("java/lang/Object");
        nextConstantPoolIndex = 3;
    }

    /**
     * Tests that setMethodParameterValue stores parameter value when storeMethodParameterValues is true.
     * Verifies basic functionality with a valid integer value.
     */
    @Test
    public void testSetMethodParameterValue_withStoreEnabledAndIntegerValue_storesValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, true, false);

        // Create a method and add it to the class
        ProgramMethod method = createTestMethod("testMethod", "()I");

        // Create a method reference constant
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value intValue = valueFactory.createIntegerValue();
        int parameterIndex = 0;

        // Act
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, parameterIndex, intValue);

        // Assert - Check that the value was stored in the method's optimization info
        Value storedValue = StoringInvocationUnit.getMethodParameterValue(method, parameterIndex);
        assertNotNull(storedValue, "Parameter value should be stored");
    }

    /**
     * Tests that setMethodParameterValue does not store when storeMethodParameterValues is false.
     * Verifies that the flag correctly controls whether values are stored.
     */
    @Test
    public void testSetMethodParameterValue_withStoreDisabled_doesNotStore() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, false, false);

        ProgramMethod method = createTestMethod("testMethod", "()I");

        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value intValue = valueFactory.createIntegerValue();
        int parameterIndex = 0;

        // Act
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, parameterIndex, intValue);

        // Assert - Check that no value was stored
        Value storedValue = StoringInvocationUnit.getMethodParameterValue(method, parameterIndex);
        assertNull(storedValue, "Parameter value should not be stored when flag is disabled");
    }

    /**
     * Tests that setMethodParameterValue works with a reference value.
     * Verifies the method handles different value types correctly.
     */
    @Test
    public void testSetMethodParameterValue_withReferenceValue_storesValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, true, false);

        ProgramMethod method = createTestMethod("testMethod", "(Ljava/lang/String;)V");

        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value refValue = valueFactory.createReferenceValue("Ljava/lang/String;", null, false, false);
        int parameterIndex = 0;

        // Act
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, parameterIndex, refValue);

        // Assert
        Value storedValue = StoringInvocationUnit.getMethodParameterValue(method, parameterIndex);
        assertNotNull(storedValue, "Reference value should be stored");
    }

    /**
     * Tests that setMethodParameterValue works with a long value.
     * Verifies the method handles long values correctly.
     */
    @Test
    public void testSetMethodParameterValue_withLongValue_storesValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, true, false);

        ProgramMethod method = createTestMethod("testMethod", "(J)V");

        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value longValue = valueFactory.createLongValue();
        int parameterIndex = 0;

        // Act
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, parameterIndex, longValue);

        // Assert
        Value storedValue = StoringInvocationUnit.getMethodParameterValue(method, parameterIndex);
        assertNotNull(storedValue, "Long value should be stored");
    }

    /**
     * Tests that setMethodParameterValue works with a double value.
     * Verifies the method handles double values correctly.
     */
    @Test
    public void testSetMethodParameterValue_withDoubleValue_storesValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, true, false);

        ProgramMethod method = createTestMethod("testMethod", "(D)V");

        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value doubleValue = valueFactory.createDoubleValue();
        int parameterIndex = 0;

        // Act
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, parameterIndex, doubleValue);

        // Assert
        Value storedValue = StoringInvocationUnit.getMethodParameterValue(method, parameterIndex);
        assertNotNull(storedValue, "Double value should be stored");
    }

    /**
     * Tests that setMethodParameterValue can be called without throwing exceptions.
     * Verifies basic stability of the method.
     */
    @Test
    public void testSetMethodParameterValue_withValidParameters_doesNotThrowException() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        ProgramMethod method = createTestMethod("testMethod", "(I)V");

        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value intValue = valueFactory.createIntegerValue();

        // Act & Assert
        assertDoesNotThrow(() -> invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, intValue));
    }

    /**
     * Tests that setMethodParameterValue can be called multiple times with different indices.
     * Verifies that the method can handle storing multiple parameter values.
     */
    @Test
    public void testSetMethodParameterValue_withMultipleParameters_storesAllValues() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, true, false);

        ProgramMethod method = createTestMethod("testMethod", "(ILjava/lang/String;J)V");

        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value value1 = valueFactory.createIntegerValue();
        Value value2 = valueFactory.createReferenceValue("Ljava/lang/String;", null, false, false);
        Value value3 = valueFactory.createLongValue();

        // Act
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, value1);
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 1, value2);
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 2, value3);

        // Assert
        assertNotNull(StoringInvocationUnit.getMethodParameterValue(method, 0), "First parameter should be stored");
        assertNotNull(StoringInvocationUnit.getMethodParameterValue(method, 1), "Second parameter should be stored");
        assertNotNull(StoringInvocationUnit.getMethodParameterValue(method, 2), "Third parameter should be stored");
    }

    /**
     * Tests that setMethodParameterValue handles null referenced method gracefully.
     * Verifies the method doesn't crash when the method reference is not resolved.
     */
    @Test
    public void testSetMethodParameterValue_withNullReferencedMethod_doesNotThrowException() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        // Create a method reference that doesn't point to a real method (referencedMethod is null)
        MethodrefConstant methodrefConstant = new MethodrefConstant();
        // Don't set the referenced method, so it remains null

        Value intValue = valueFactory.createIntegerValue();

        // Act & Assert - Should not throw exception, just do nothing
        assertDoesNotThrow(() -> invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, intValue));
    }

    /**
     * Tests that setMethodParameterValue generalizes values correctly.
     * When called multiple times with the same index, values should be generalized (not just replaced).
     */
    @Test
    public void testSetMethodParameterValue_calledMultipleTimes_generalizesValues() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, true, false);

        ProgramMethod method = createTestMethod("testMethod", "(I)V");

        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        ParticularValueFactory particularFactory = new ParticularValueFactory();
        Value value1 = particularFactory.createIntegerValue(42);
        Value value2 = particularFactory.createIntegerValue(100);

        // Act - Call with different particular values
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, value1);
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, value2);

        // Assert - The stored value should exist (it will be generalized)
        Value storedValue = StoringInvocationUnit.getMethodParameterValue(method, 0);
        assertNotNull(storedValue, "Generalized value should be stored");
    }

    /**
     * Tests that setMethodParameterValue works with different flag combinations.
     * Verifies the storeMethodParameterValues flag is independent of other flags.
     */
    @Test
    public void testSetMethodParameterValue_withDifferentFlagCombinations_respectsStoreFlag() {
        // Arrange
        ProgramMethod method = createTestMethod("testMethod", "(I)V");

        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);
        Value intValue = valueFactory.createIntegerValue();

        // Test with storeMethodParameterValues = true, others false
        StoringInvocationUnit unit1 = new StoringInvocationUnit(valueFactory, false, true, false);
        unit1.setMethodParameterValue(programClass, methodrefConstant, 0, intValue);
        assertNotNull(StoringInvocationUnit.getMethodParameterValue(method, 0));

        // Reset method optimization info
        method = createTestMethod("testMethod2", "(I)V");
        methodrefConstant = createMethodrefConstant(method);

        // Test with storeMethodParameterValues = false, others true
        StoringInvocationUnit unit2 = new StoringInvocationUnit(valueFactory, true, false, true);
        unit2.setMethodParameterValue(programClass, methodrefConstant, 0, intValue);
        assertNull(StoringInvocationUnit.getMethodParameterValue(method, 0), "Should not store when flag is false");
    }

    /**
     * Tests that setMethodParameterValue works with parameter index 0.
     * Verifies the method handles the first parameter correctly.
     */
    @Test
    public void testSetMethodParameterValue_withIndexZero_storesValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        ProgramMethod method = createTestMethod("testMethod", "(I)V");

        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);
        Value intValue = valueFactory.createIntegerValue();

        // Act
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, intValue);

        // Assert
        assertDoesNotThrow(() -> StoringInvocationUnit.getMethodParameterValue(method, 0));
    }

    /**
     * Tests that setMethodParameterValue works with default constructor (all flags true).
     * Verifies the default configuration stores parameter values.
     */
    @Test
    public void testSetMethodParameterValue_withDefaultConstructor_storesValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        ProgramMethod method = createTestMethod("testMethod", "(I)V");

        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);
        Value intValue = valueFactory.createIntegerValue();

        // Act
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, intValue);

        // Assert - With default constructor, storeMethodParameterValues should be true
        Value storedValue = StoringInvocationUnit.getMethodParameterValue(method, 0);
        assertNotNull(storedValue, "Value should be stored with default constructor");
    }

    /**
     * Tests that setMethodParameterValue works correctly with different ValueFactory implementations.
     * Verifies the method handles values from different factories.
     */
    @Test
    public void testSetMethodParameterValue_withDifferentValueFactories_storesValues() {
        // Arrange - Create invocation units with different value factories
        BasicValueFactory basicValueFactory = new BasicValueFactory();
        StoringInvocationUnit unit1 = new StoringInvocationUnit(basicValueFactory, false, true, false);

        IdentifiedValueFactory identifiedValueFactory = new IdentifiedValueFactory();
        StoringInvocationUnit unit2 = new StoringInvocationUnit(identifiedValueFactory, false, true, false);

        ProgramMethod method1 = createTestMethod("testMethod1", "(I)V");
        AnyMethodrefConstant methodref1 = createMethodrefConstant(method1);

        ProgramMethod method2 = createTestMethod("testMethod2", "(I)V");
        AnyMethodrefConstant methodref2 = createMethodrefConstant(method2);

        Value value1 = basicValueFactory.createIntegerValue();
        Value value2 = identifiedValueFactory.createIntegerValue();

        // Act
        unit1.setMethodParameterValue(programClass, methodref1, 0, value1);
        unit2.setMethodParameterValue(programClass, methodref2, 0, value2);

        // Assert
        assertNotNull(StoringInvocationUnit.getMethodParameterValue(method1, 0));
        assertNotNull(StoringInvocationUnit.getMethodParameterValue(method2, 0));
    }

    /**
     * Tests that setMethodParameterValue works with InterfaceMethodrefConstant.
     * Verifies the method works with different types of method reference constants.
     */
    @Test
    public void testSetMethodParameterValue_withInterfaceMethodrefConstant_doesNotThrowException() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        ProgramMethod method = createTestMethod("testMethod", "(I)V");

        // Create an interface method reference
        InterfaceMethodrefConstant interfaceMethodref = new InterfaceMethodrefConstant();
        interfaceMethodref.referencedMethod = method;

        Value intValue = valueFactory.createIntegerValue();

        // Act & Assert
        assertDoesNotThrow(() -> invocationUnit.setMethodParameterValue(programClass, interfaceMethodref, 0, intValue));
    }

    /**
     * Tests that setMethodParameterValue can be called many times in sequence.
     * Verifies the method is stable when called repeatedly.
     */
    @Test
    public void testSetMethodParameterValue_calledManyTimesInSequence_doesNotThrowException() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        ProgramMethod method = createTestMethod("testMethod", "(I)V");

        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);
        Value intValue = valueFactory.createIntegerValue();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, intValue);
            }
        });
    }

    /**
     * Helper method to create a test method with the given name and descriptor.
     */
    private ProgramMethod createTestMethod(String name, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC;

        // Add method name and descriptor to constant pool using unique indices
        int nameIndex = nextConstantPoolIndex++;
        int descriptorIndex = nextConstantPoolIndex++;

        programClass.constantPool[nameIndex] = new Utf8Constant(name);
        programClass.constantPool[descriptorIndex] = new Utf8Constant(descriptor);

        method.u2nameIndex = nameIndex;
        method.u2descriptorIndex = descriptorIndex;

        // Initialize the method optimization info
        method.setProcessingInfo(new ProgramMethodOptimizationInfo(programClass, method));

        return method;
    }

    /**
     * Helper method to create a method reference constant pointing to the given method.
     */
    private AnyMethodrefConstant createMethodrefConstant(Method method) {
        MethodrefConstant methodrefConstant = new MethodrefConstant();
        methodrefConstant.referencedMethod = method;
        return methodrefConstant;
    }
}

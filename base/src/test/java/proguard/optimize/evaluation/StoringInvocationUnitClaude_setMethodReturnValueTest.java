package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.*;
import proguard.evaluation.value.*;
import proguard.optimize.info.ProgramMethodOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link StoringInvocationUnit#setMethodReturnValue(Clazz, Method, Value)}.
 *
 * The setMethodReturnValue method:
 * 1. Checks if storeMethodReturnValues flag is enabled
 * 2. If enabled, calls generalizeMethodReturnValue
 * 3. generalizeMethodReturnValue stores the return value in the method's optimization info
 *    (only if the method is not marked with KeepMarker)
 *
 * This allows the StoringInvocationUnit to track return values from methods
 * for optimization purposes.
 */
public class StoringInvocationUnitClaude_setMethodReturnValueTest {

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
     * Tests that setMethodReturnValue stores return value when storeMethodReturnValues is true.
     * Verifies basic functionality with a valid integer value.
     */
    @Test
    public void testSetMethodReturnValue_withStoreEnabledAndIntegerValue_storesValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, false, true);

        ProgramMethod method = createTestMethod("testMethod", "()I");

        Value intValue = valueFactory.createIntegerValue();

        // Act
        invocationUnit.setMethodReturnValue(programClass, method, intValue);

        // Assert - Check that the value was stored in the method's optimization info
        Value storedValue = StoringInvocationUnit.getMethodReturnValue(method);
        assertNotNull(storedValue, "Return value should be stored");
    }

    /**
     * Tests that setMethodReturnValue does not store when storeMethodReturnValues is false.
     * Verifies that the flag correctly controls whether values are stored.
     */
    @Test
    public void testSetMethodReturnValue_withStoreDisabled_doesNotStore() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, false, false);

        ProgramMethod method = createTestMethod("testMethod", "()I");

        Value intValue = valueFactory.createIntegerValue();

        // Act
        invocationUnit.setMethodReturnValue(programClass, method, intValue);

        // Assert - Check that no value was stored
        Value storedValue = StoringInvocationUnit.getMethodReturnValue(method);
        assertNull(storedValue, "Return value should not be stored when flag is disabled");
    }

    /**
     * Tests that setMethodReturnValue works with a reference value.
     * Verifies the method handles different value types correctly.
     */
    @Test
    public void testSetMethodReturnValue_withReferenceValue_storesValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, false, true);

        ProgramMethod method = createTestMethod("testMethod", "()Ljava/lang/String;");

        Value refValue = valueFactory.createReferenceValue("Ljava/lang/String;", null, false, false);

        // Act
        invocationUnit.setMethodReturnValue(programClass, method, refValue);

        // Assert
        Value storedValue = StoringInvocationUnit.getMethodReturnValue(method);
        assertNotNull(storedValue, "Reference value should be stored");
    }

    /**
     * Tests that setMethodReturnValue works with a long value.
     * Verifies the method handles long values correctly.
     */
    @Test
    public void testSetMethodReturnValue_withLongValue_storesValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, false, true);

        ProgramMethod method = createTestMethod("testMethod", "()J");

        Value longValue = valueFactory.createLongValue();

        // Act
        invocationUnit.setMethodReturnValue(programClass, method, longValue);

        // Assert
        Value storedValue = StoringInvocationUnit.getMethodReturnValue(method);
        assertNotNull(storedValue, "Long value should be stored");
    }

    /**
     * Tests that setMethodReturnValue works with a double value.
     * Verifies the method handles double values correctly.
     */
    @Test
    public void testSetMethodReturnValue_withDoubleValue_storesValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, false, true);

        ProgramMethod method = createTestMethod("testMethod", "()D");

        Value doubleValue = valueFactory.createDoubleValue();

        // Act
        invocationUnit.setMethodReturnValue(programClass, method, doubleValue);

        // Assert
        Value storedValue = StoringInvocationUnit.getMethodReturnValue(method);
        assertNotNull(storedValue, "Double value should be stored");
    }

    /**
     * Tests that setMethodReturnValue works with a float value.
     * Verifies the method handles float values correctly.
     */
    @Test
    public void testSetMethodReturnValue_withFloatValue_storesValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, false, true);

        ProgramMethod method = createTestMethod("testMethod", "()F");

        Value floatValue = valueFactory.createFloatValue();

        // Act
        invocationUnit.setMethodReturnValue(programClass, method, floatValue);

        // Assert
        Value storedValue = StoringInvocationUnit.getMethodReturnValue(method);
        assertNotNull(storedValue, "Float value should be stored");
    }

    /**
     * Tests that setMethodReturnValue can be called without throwing exceptions.
     * Verifies basic stability of the method.
     */
    @Test
    public void testSetMethodReturnValue_withValidParameters_doesNotThrowException() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        ProgramMethod method = createTestMethod("testMethod", "()I");

        Value intValue = valueFactory.createIntegerValue();

        // Act & Assert
        assertDoesNotThrow(() -> invocationUnit.setMethodReturnValue(programClass, method, intValue));
    }

    /**
     * Tests that setMethodReturnValue generalizes values correctly.
     * When called multiple times, values should be generalized (not just replaced).
     */
    @Test
    public void testSetMethodReturnValue_calledMultipleTimes_generalizesValues() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, false, true);

        ProgramMethod method = createTestMethod("testMethod", "()I");

        ParticularValueFactory particularFactory = new ParticularValueFactory();
        Value value1 = particularFactory.createIntegerValue(42);
        Value value2 = particularFactory.createIntegerValue(100);

        // Act - Call with different particular values
        invocationUnit.setMethodReturnValue(programClass, method, value1);
        invocationUnit.setMethodReturnValue(programClass, method, value2);

        // Assert - The stored value should exist (it will be generalized)
        Value storedValue = StoringInvocationUnit.getMethodReturnValue(method);
        assertNotNull(storedValue, "Generalized value should be stored");
    }

    /**
     * Tests that setMethodReturnValue works with different flag combinations.
     * Verifies the storeMethodReturnValues flag is independent of other flags.
     */
    @Test
    public void testSetMethodReturnValue_withDifferentFlagCombinations_respectsStoreFlag() {
        // Arrange
        ProgramMethod method1 = createTestMethod("testMethod1", "()I");
        ProgramMethod method2 = createTestMethod("testMethod2", "()I");

        Value intValue = valueFactory.createIntegerValue();

        // Test with storeMethodReturnValues = true, others false
        StoringInvocationUnit unit1 = new StoringInvocationUnit(valueFactory, false, false, true);
        unit1.setMethodReturnValue(programClass, method1, intValue);
        assertNotNull(StoringInvocationUnit.getMethodReturnValue(method1), "Should store when flag is true");

        // Test with storeMethodReturnValues = false, others true
        StoringInvocationUnit unit2 = new StoringInvocationUnit(valueFactory, true, true, false);
        unit2.setMethodReturnValue(programClass, method2, intValue);
        assertNull(StoringInvocationUnit.getMethodReturnValue(method2), "Should not store when flag is false");
    }

    /**
     * Tests that setMethodReturnValue works with default constructor (all flags true).
     * Verifies the default configuration stores return values.
     */
    @Test
    public void testSetMethodReturnValue_withDefaultConstructor_storesValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        ProgramMethod method = createTestMethod("testMethod", "()I");

        Value intValue = valueFactory.createIntegerValue();

        // Act
        invocationUnit.setMethodReturnValue(programClass, method, intValue);

        // Assert - With default constructor, storeMethodReturnValues should be true
        Value storedValue = StoringInvocationUnit.getMethodReturnValue(method);
        assertNotNull(storedValue, "Value should be stored with default constructor");
    }

    /**
     * Tests that setMethodReturnValue works correctly with different ValueFactory implementations.
     * Verifies the method handles values from different factories.
     */
    @Test
    public void testSetMethodReturnValue_withDifferentValueFactories_storesValues() {
        // Arrange - Create invocation units with different value factories
        BasicValueFactory basicValueFactory = new BasicValueFactory();
        StoringInvocationUnit unit1 = new StoringInvocationUnit(basicValueFactory, false, false, true);

        IdentifiedValueFactory identifiedValueFactory = new IdentifiedValueFactory();
        StoringInvocationUnit unit2 = new StoringInvocationUnit(identifiedValueFactory, false, false, true);

        ProgramMethod method1 = createTestMethod("testMethod1", "()I");
        ProgramMethod method2 = createTestMethod("testMethod2", "()I");

        Value value1 = basicValueFactory.createIntegerValue();
        Value value2 = identifiedValueFactory.createIntegerValue();

        // Act
        unit1.setMethodReturnValue(programClass, method1, value1);
        unit2.setMethodReturnValue(programClass, method2, value2);

        // Assert
        assertNotNull(StoringInvocationUnit.getMethodReturnValue(method1), "BasicValueFactory value should be stored");
        assertNotNull(StoringInvocationUnit.getMethodReturnValue(method2), "IdentifiedValueFactory value should be stored");
    }

    /**
     * Tests that setMethodReturnValue can be called many times in sequence.
     * Verifies the method is stable when called repeatedly.
     */
    @Test
    public void testSetMethodReturnValue_calledManyTimesInSequence_doesNotThrowException() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        ProgramMethod method = createTestMethod("testMethod", "()I");

        Value intValue = valueFactory.createIntegerValue();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                invocationUnit.setMethodReturnValue(programClass, method, intValue);
            }
        });
    }

    /**
     * Tests that setMethodReturnValue works with void methods (no return value).
     * Verifies the method handles void return type correctly.
     */
    @Test
    public void testSetMethodReturnValue_withVoidMethod_doesNotThrowException() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        ProgramMethod method = createTestMethod("testMethod", "()V");

        // For void methods, we might still call setMethodReturnValue with a value
        // (e.g., in case of exceptional returns or internal tracking)
        Value intValue = valueFactory.createIntegerValue();

        // Act & Assert
        assertDoesNotThrow(() -> invocationUnit.setMethodReturnValue(programClass, method, intValue));
    }

    /**
     * Tests that setMethodReturnValue works with methods returning arrays.
     * Verifies the method handles array return types correctly.
     */
    @Test
    public void testSetMethodReturnValue_withArrayReturnType_storesValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, false, true);

        ProgramMethod method = createTestMethod("testMethod", "()[I");

        Value arrayValue = valueFactory.createReferenceValue("[I", null, false, false);

        // Act
        invocationUnit.setMethodReturnValue(programClass, method, arrayValue);

        // Assert
        Value storedValue = StoringInvocationUnit.getMethodReturnValue(method);
        assertNotNull(storedValue, "Array return value should be stored");
    }

    /**
     * Tests that setMethodReturnValue works with methods having parameters.
     * Verifies that return value storage is independent of method parameters.
     */
    @Test
    public void testSetMethodReturnValue_withMethodWithParameters_storesValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, false, true);

        ProgramMethod method = createTestMethod("testMethod", "(ILjava/lang/String;)Ljava/lang/Object;");

        Value returnValue = valueFactory.createReferenceValue("Ljava/lang/Object;", null, false, false);

        // Act
        invocationUnit.setMethodReturnValue(programClass, method, returnValue);

        // Assert
        Value storedValue = StoringInvocationUnit.getMethodReturnValue(method);
        assertNotNull(storedValue, "Return value should be stored regardless of parameters");
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
}

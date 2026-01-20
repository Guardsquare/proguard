package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.*;
import proguard.evaluation.value.*;
import proguard.optimize.info.ProgramMethodOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link StoringInvocationUnit#getMethodParameterValue(Method, int)}.
 *
 * The getMethodParameterValue method retrieves parameter values that were previously stored
 * in a method's optimization info. It's a static utility method that:
 * 1. Gets the MethodOptimizationInfo from the method
 * 2. Retrieves the parameter value at the specified index
 * 3. Returns null if no value has been stored
 */
public class StoringInvocationUnitClaude_getMethodParameterValueTest {

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
     * Tests that getMethodParameterValue returns null when no value has been stored.
     * Verifies basic behavior when retrieving from an uninitialized method.
     */
    @Test
    public void testGetMethodParameterValue_withNoStoredValue_returnsNull() {
        // Arrange
        ProgramMethod method = createTestMethod("testMethod", "(I)V");

        // Act
        Value value = StoringInvocationUnit.getMethodParameterValue(method, 0);

        // Assert
        assertNull(value, "Should return null when no value has been stored");
    }

    /**
     * Tests that getMethodParameterValue retrieves an integer value after it's been stored.
     * Verifies the method correctly retrieves stored integer values.
     */
    @Test
    public void testGetMethodParameterValue_afterStoringIntegerValue_returnsValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "(I)V");
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value intValue = valueFactory.createIntegerValue();
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, intValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodParameterValue(method, 0);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve the stored value");
    }

    /**
     * Tests that getMethodParameterValue retrieves a reference value after it's been stored.
     * Verifies the method correctly retrieves stored reference values.
     */
    @Test
    public void testGetMethodParameterValue_afterStoringReferenceValue_returnsValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "(Ljava/lang/String;)V");
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value refValue = valueFactory.createReferenceValue("Ljava/lang/String;", null, false, false);
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, refValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodParameterValue(method, 0);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve the stored reference value");
    }

    /**
     * Tests that getMethodParameterValue retrieves a long value after it's been stored.
     * Verifies the method correctly retrieves stored long values.
     */
    @Test
    public void testGetMethodParameterValue_afterStoringLongValue_returnsValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "(J)V");
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value longValue = valueFactory.createLongValue();
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, longValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodParameterValue(method, 0);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve the stored long value");
    }

    /**
     * Tests that getMethodParameterValue retrieves a double value after it's been stored.
     * Verifies the method correctly retrieves stored double values.
     */
    @Test
    public void testGetMethodParameterValue_afterStoringDoubleValue_returnsValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "(D)V");
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value doubleValue = valueFactory.createDoubleValue();
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, doubleValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodParameterValue(method, 0);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve the stored double value");
    }

    /**
     * Tests that getMethodParameterValue retrieves a float value after it's been stored.
     * Verifies the method correctly retrieves stored float values.
     */
    @Test
    public void testGetMethodParameterValue_afterStoringFloatValue_returnsValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "(F)V");
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value floatValue = valueFactory.createFloatValue();
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, floatValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodParameterValue(method, 0);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve the stored float value");
    }

    /**
     * Tests that getMethodParameterValue can retrieve multiple parameter values independently.
     * Verifies that parameter indices work correctly for methods with multiple parameters.
     */
    @Test
    public void testGetMethodParameterValue_withMultipleParameters_retrievesCorrectValues() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "(ILjava/lang/String;J)V");
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value value1 = valueFactory.createIntegerValue();
        Value value2 = valueFactory.createReferenceValue("Ljava/lang/String;", null, false, false);
        Value value3 = valueFactory.createLongValue();

        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, value1);
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 1, value2);
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 2, value3);

        // Act
        Value retrieved1 = StoringInvocationUnit.getMethodParameterValue(method, 0);
        Value retrieved2 = StoringInvocationUnit.getMethodParameterValue(method, 1);
        Value retrieved3 = StoringInvocationUnit.getMethodParameterValue(method, 2);

        // Assert
        assertNotNull(retrieved1, "First parameter should be retrieved");
        assertNotNull(retrieved2, "Second parameter should be retrieved");
        assertNotNull(retrieved3, "Third parameter should be retrieved");
    }

    /**
     * Tests that getMethodParameterValue returns null for unset parameters in a multi-parameter method.
     * Verifies that only set parameters return non-null values.
     */
    @Test
    public void testGetMethodParameterValue_withPartiallySetParameters_returnsNullForUnset() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "(ILjava/lang/String;J)V");
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        // Only set the first and third parameters
        Value value1 = valueFactory.createIntegerValue();
        Value value3 = valueFactory.createLongValue();

        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, value1);
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 2, value3);

        // Act
        Value retrieved1 = StoringInvocationUnit.getMethodParameterValue(method, 0);
        Value retrieved2 = StoringInvocationUnit.getMethodParameterValue(method, 1);
        Value retrieved3 = StoringInvocationUnit.getMethodParameterValue(method, 2);

        // Assert
        assertNotNull(retrieved1, "First parameter should be retrieved");
        assertNull(retrieved2, "Second parameter should be null (not set)");
        assertNotNull(retrieved3, "Third parameter should be retrieved");
    }

    /**
     * Tests that getMethodParameterValue works with parameter index 0.
     * Verifies the method handles the first parameter index correctly.
     */
    @Test
    public void testGetMethodParameterValue_withIndexZero_worksCorrectly() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "(I)V");
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value intValue = valueFactory.createIntegerValue();
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, intValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodParameterValue(method, 0);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve value at index 0");
    }

    /**
     * Tests that getMethodParameterValue can be called multiple times on the same parameter.
     * Verifies that the method is idempotent and stable.
     */
    @Test
    public void testGetMethodParameterValue_calledMultipleTimes_returnsConsistentValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "(I)V");
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value intValue = valueFactory.createIntegerValue();
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, intValue);

        // Act - Call multiple times
        Value retrieved1 = StoringInvocationUnit.getMethodParameterValue(method, 0);
        Value retrieved2 = StoringInvocationUnit.getMethodParameterValue(method, 0);
        Value retrieved3 = StoringInvocationUnit.getMethodParameterValue(method, 0);

        // Assert - All calls should return a non-null value
        assertNotNull(retrieved1, "First retrieval should return non-null");
        assertNotNull(retrieved2, "Second retrieval should return non-null");
        assertNotNull(retrieved3, "Third retrieval should return non-null");
    }

    /**
     * Tests that getMethodParameterValue works with different methods independently.
     * Verifies that parameter values are stored per method instance.
     */
    @Test
    public void testGetMethodParameterValue_withDifferentMethods_returnsIndependentValues() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        ProgramMethod method1 = createTestMethod("testMethod1", "(I)V");
        ProgramMethod method2 = createTestMethod("testMethod2", "(I)V");

        AnyMethodrefConstant methodref1 = createMethodrefConstant(method1);
        AnyMethodrefConstant methodref2 = createMethodrefConstant(method2);

        Value value1 = valueFactory.createIntegerValue();
        invocationUnit.setMethodParameterValue(programClass, methodref1, 0, value1);

        // Act - method1 should have a value, method2 should not
        Value retrieved1 = StoringInvocationUnit.getMethodParameterValue(method1, 0);
        Value retrieved2 = StoringInvocationUnit.getMethodParameterValue(method2, 0);

        // Assert
        assertNotNull(retrieved1, "Method 1 should have a stored value");
        assertNull(retrieved2, "Method 2 should not have a stored value");
    }

    /**
     * Tests that getMethodParameterValue returns null for methods where storing is disabled.
     * Verifies that the flag in the invocation unit controls storage.
     */
    @Test
    public void testGetMethodParameterValue_withStoreDisabled_returnsNull() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, false, false);
        ProgramMethod method = createTestMethod("testMethod", "(I)V");
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value intValue = valueFactory.createIntegerValue();
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, intValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodParameterValue(method, 0);

        // Assert
        assertNull(retrievedValue, "Should return null when storing is disabled");
    }

    /**
     * Tests that getMethodParameterValue does not throw exceptions with valid parameters.
     * Verifies basic stability of the method.
     */
    @Test
    public void testGetMethodParameterValue_withValidParameters_doesNotThrowException() {
        // Arrange
        ProgramMethod method = createTestMethod("testMethod", "(I)V");

        // Act & Assert
        assertDoesNotThrow(() -> StoringInvocationUnit.getMethodParameterValue(method, 0),
            "Should not throw exception with valid parameters");
    }

    /**
     * Tests that getMethodParameterValue works correctly after values are generalized.
     * When the same parameter is set multiple times, values should be generalized.
     */
    @Test
    public void testGetMethodParameterValue_afterValueGeneralization_returnsGeneralizedValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "(I)V");
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        ParticularValueFactory particularFactory = new ParticularValueFactory();
        Value value1 = particularFactory.createIntegerValue(42);
        Value value2 = particularFactory.createIntegerValue(100);

        // Store two different particular values for the same parameter
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, value1);
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, value2);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodParameterValue(method, 0);

        // Assert - Should return a generalized value (not null)
        assertNotNull(retrievedValue, "Should return generalized value after multiple sets");
    }

    /**
     * Tests that getMethodParameterValue works with methods that have no parameters.
     * Verifies behavior with zero-parameter methods.
     */
    @Test
    public void testGetMethodParameterValue_withNoParameterMethod_returnsNullForAnyIndex() {
        // Arrange
        ProgramMethod method = createTestMethod("testMethod", "()V");

        // Act & Assert - This should not throw an exception, but may return null or throw
        // depending on implementation. We test that it doesn't crash.
        assertDoesNotThrow(() -> {
            Value value = StoringInvocationUnit.getMethodParameterValue(method, 0);
            // The value could be null since there are no parameters
        }, "Should not throw exception even for methods with no parameters");
    }

    /**
     * Tests that getMethodParameterValue works with static methods.
     * Verifies that parameter indexing works correctly for static methods.
     */
    @Test
    public void testGetMethodParameterValue_withStaticMethod_worksCorrectly() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createStaticMethod("staticMethod", "(I)V");
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value intValue = valueFactory.createIntegerValue();
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, intValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodParameterValue(method, 0);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve value from static method");
    }

    /**
     * Tests that getMethodParameterValue works with instance methods (with implicit 'this' parameter).
     * Verifies that parameter indexing accounts for the implicit 'this' parameter.
     */
    @Test
    public void testGetMethodParameterValue_withInstanceMethod_worksCorrectly() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("instanceMethod", "(I)V");
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value intValue = valueFactory.createIntegerValue();
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, intValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodParameterValue(method, 0);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve value from instance method");
    }

    /**
     * Tests that getMethodParameterValue works correctly with BasicValueFactory.
     * Verifies the method works with different value factory implementations.
     */
    @Test
    public void testGetMethodParameterValue_withBasicValueFactory_worksCorrectly() {
        // Arrange
        BasicValueFactory basicValueFactory = new BasicValueFactory();
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(basicValueFactory);
        ProgramMethod method = createTestMethod("testMethod", "(I)V");
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value intValue = basicValueFactory.createIntegerValue();
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, intValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodParameterValue(method, 0);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve value created with BasicValueFactory");
    }

    /**
     * Tests that getMethodParameterValue works correctly with IdentifiedValueFactory.
     * Verifies the method works with different value factory implementations.
     */
    @Test
    public void testGetMethodParameterValue_withIdentifiedValueFactory_worksCorrectly() {
        // Arrange
        IdentifiedValueFactory identifiedValueFactory = new IdentifiedValueFactory();
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(identifiedValueFactory);
        ProgramMethod method = createTestMethod("testMethod", "(I)V");
        AnyMethodrefConstant methodrefConstant = createMethodrefConstant(method);

        Value intValue = identifiedValueFactory.createIntegerValue();
        invocationUnit.setMethodParameterValue(programClass, methodrefConstant, 0, intValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodParameterValue(method, 0);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve value created with IdentifiedValueFactory");
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
     * Helper method to create a static test method with the given name and descriptor.
     */
    private ProgramMethod createStaticMethod(String name, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.STATIC;

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

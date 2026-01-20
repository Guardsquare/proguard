package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.*;
import proguard.evaluation.value.*;
import proguard.optimize.info.ProgramMethodOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link StoringInvocationUnit#getMethodReturnValue(Method)}.
 *
 * The getMethodReturnValue method retrieves return values that were previously stored
 * in a method's optimization info. It's a static utility method that:
 * 1. Gets the MethodOptimizationInfo from the method
 * 2. Retrieves the return value
 * 3. Returns null if no value has been stored
 */
public class StoringInvocationUnitClaude_getMethodReturnValueTest {

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
     * Tests that getMethodReturnValue returns null when no value has been stored.
     * Verifies basic behavior when retrieving from an uninitialized method.
     */
    @Test
    public void testGetMethodReturnValue_withNoStoredValue_returnsNull() {
        // Arrange
        ProgramMethod method = createTestMethod("testMethod", "()I");

        // Act
        Value value = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert
        assertNull(value, "Should return null when no value has been stored");
    }

    /**
     * Tests that getMethodReturnValue retrieves an integer value after it's been stored.
     * Verifies the method correctly retrieves stored integer values.
     */
    @Test
    public void testGetMethodReturnValue_afterStoringIntegerValue_returnsValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "()I");

        Value intValue = valueFactory.createIntegerValue();
        invocationUnit.setMethodReturnValue(programClass, method, intValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve the stored integer value");
    }

    /**
     * Tests that getMethodReturnValue retrieves a reference value after it's been stored.
     * Verifies the method correctly retrieves stored reference values.
     */
    @Test
    public void testGetMethodReturnValue_afterStoringReferenceValue_returnsValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "()Ljava/lang/String;");

        Value refValue = valueFactory.createReferenceValue("Ljava/lang/String;", null, false, false);
        invocationUnit.setMethodReturnValue(programClass, method, refValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve the stored reference value");
    }

    /**
     * Tests that getMethodReturnValue retrieves a long value after it's been stored.
     * Verifies the method correctly retrieves stored long values.
     */
    @Test
    public void testGetMethodReturnValue_afterStoringLongValue_returnsValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "()J");

        Value longValue = valueFactory.createLongValue();
        invocationUnit.setMethodReturnValue(programClass, method, longValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve the stored long value");
    }

    /**
     * Tests that getMethodReturnValue retrieves a double value after it's been stored.
     * Verifies the method correctly retrieves stored double values.
     */
    @Test
    public void testGetMethodReturnValue_afterStoringDoubleValue_returnsValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "()D");

        Value doubleValue = valueFactory.createDoubleValue();
        invocationUnit.setMethodReturnValue(programClass, method, doubleValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve the stored double value");
    }

    /**
     * Tests that getMethodReturnValue retrieves a float value after it's been stored.
     * Verifies the method correctly retrieves stored float values.
     */
    @Test
    public void testGetMethodReturnValue_afterStoringFloatValue_returnsValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "()F");

        Value floatValue = valueFactory.createFloatValue();
        invocationUnit.setMethodReturnValue(programClass, method, floatValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve the stored float value");
    }

    /**
     * Tests that getMethodReturnValue can be called multiple times on the same method.
     * Verifies that the method is idempotent and stable.
     */
    @Test
    public void testGetMethodReturnValue_calledMultipleTimes_returnsConsistentValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "()I");

        Value intValue = valueFactory.createIntegerValue();
        invocationUnit.setMethodReturnValue(programClass, method, intValue);

        // Act - Call multiple times
        Value retrieved1 = StoringInvocationUnit.getMethodReturnValue(method);
        Value retrieved2 = StoringInvocationUnit.getMethodReturnValue(method);
        Value retrieved3 = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert - All calls should return a non-null value
        assertNotNull(retrieved1, "First retrieval should return non-null");
        assertNotNull(retrieved2, "Second retrieval should return non-null");
        assertNotNull(retrieved3, "Third retrieval should return non-null");
    }

    /**
     * Tests that getMethodReturnValue works with different methods independently.
     * Verifies that return values are stored per method instance.
     */
    @Test
    public void testGetMethodReturnValue_withDifferentMethods_returnsIndependentValues() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        ProgramMethod method1 = createTestMethod("testMethod1", "()I");
        ProgramMethod method2 = createTestMethod("testMethod2", "()I");

        Value value1 = valueFactory.createIntegerValue();
        invocationUnit.setMethodReturnValue(programClass, method1, value1);

        // Act - method1 should have a value, method2 should not
        Value retrieved1 = StoringInvocationUnit.getMethodReturnValue(method1);
        Value retrieved2 = StoringInvocationUnit.getMethodReturnValue(method2);

        // Assert
        assertNotNull(retrieved1, "Method 1 should have a stored value");
        assertNull(retrieved2, "Method 2 should not have a stored value");
    }

    /**
     * Tests that getMethodReturnValue returns null for methods where storing is disabled.
     * Verifies that the flag in the invocation unit controls storage.
     */
    @Test
    public void testGetMethodReturnValue_withStoreDisabled_returnsNull() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, false, false);
        ProgramMethod method = createTestMethod("testMethod", "()I");

        Value intValue = valueFactory.createIntegerValue();
        invocationUnit.setMethodReturnValue(programClass, method, intValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert
        assertNull(retrievedValue, "Should return null when storing is disabled");
    }

    /**
     * Tests that getMethodReturnValue does not throw exceptions with valid parameters.
     * Verifies basic stability of the method.
     */
    @Test
    public void testGetMethodReturnValue_withValidParameters_doesNotThrowException() {
        // Arrange
        ProgramMethod method = createTestMethod("testMethod", "()I");

        // Act & Assert
        assertDoesNotThrow(() -> StoringInvocationUnit.getMethodReturnValue(method),
            "Should not throw exception with valid parameters");
    }

    /**
     * Tests that getMethodReturnValue works correctly after values are generalized.
     * When the return value is set multiple times, values should be generalized.
     */
    @Test
    public void testGetMethodReturnValue_afterValueGeneralization_returnsGeneralizedValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "()I");

        ParticularValueFactory particularFactory = new ParticularValueFactory();
        Value value1 = particularFactory.createIntegerValue(42);
        Value value2 = particularFactory.createIntegerValue(100);

        // Store two different particular values
        invocationUnit.setMethodReturnValue(programClass, method, value1);
        invocationUnit.setMethodReturnValue(programClass, method, value2);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert - Should return a generalized value (not null)
        assertNotNull(retrievedValue, "Should return generalized value after multiple sets");
    }

    /**
     * Tests that getMethodReturnValue works with void methods.
     * Verifies behavior with void return type methods.
     */
    @Test
    public void testGetMethodReturnValue_withVoidMethod_returnsNullOrStoredValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "()V");

        // Act - Before storing anything
        Value beforeValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Store a value (even though method is void)
        Value intValue = valueFactory.createIntegerValue();
        invocationUnit.setMethodReturnValue(programClass, method, intValue);

        Value afterValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert
        assertNull(beforeValue, "Should return null before storing");
        assertNotNull(afterValue, "Should return stored value after storing");
    }

    /**
     * Tests that getMethodReturnValue works with static methods.
     * Verifies that return value retrieval works correctly for static methods.
     */
    @Test
    public void testGetMethodReturnValue_withStaticMethod_worksCorrectly() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createStaticMethod("staticMethod", "()I");

        Value intValue = valueFactory.createIntegerValue();
        invocationUnit.setMethodReturnValue(programClass, method, intValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve value from static method");
    }

    /**
     * Tests that getMethodReturnValue works with instance methods.
     * Verifies that return value retrieval works correctly for instance methods.
     */
    @Test
    public void testGetMethodReturnValue_withInstanceMethod_worksCorrectly() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("instanceMethod", "()I");

        Value intValue = valueFactory.createIntegerValue();
        invocationUnit.setMethodReturnValue(programClass, method, intValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve value from instance method");
    }

    /**
     * Tests that getMethodReturnValue works correctly with BasicValueFactory.
     * Verifies the method works with different value factory implementations.
     */
    @Test
    public void testGetMethodReturnValue_withBasicValueFactory_worksCorrectly() {
        // Arrange
        BasicValueFactory basicValueFactory = new BasicValueFactory();
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(basicValueFactory);
        ProgramMethod method = createTestMethod("testMethod", "()I");

        Value intValue = basicValueFactory.createIntegerValue();
        invocationUnit.setMethodReturnValue(programClass, method, intValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve value created with BasicValueFactory");
    }

    /**
     * Tests that getMethodReturnValue works correctly with IdentifiedValueFactory.
     * Verifies the method works with different value factory implementations.
     */
    @Test
    public void testGetMethodReturnValue_withIdentifiedValueFactory_worksCorrectly() {
        // Arrange
        IdentifiedValueFactory identifiedValueFactory = new IdentifiedValueFactory();
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(identifiedValueFactory);
        ProgramMethod method = createTestMethod("testMethod", "()I");

        Value intValue = identifiedValueFactory.createIntegerValue();
        invocationUnit.setMethodReturnValue(programClass, method, intValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve value created with IdentifiedValueFactory");
    }

    /**
     * Tests that getMethodReturnValue works with array return types.
     * Verifies the method handles array values correctly.
     */
    @Test
    public void testGetMethodReturnValue_withArrayReturnType_returnsValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "()[I");

        Value arrayValue = valueFactory.createReferenceValue("[I", null, false, false);
        invocationUnit.setMethodReturnValue(programClass, method, arrayValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve array return value");
    }

    /**
     * Tests that getMethodReturnValue works with methods having parameters.
     * Verifies that return value retrieval is independent of method parameters.
     */
    @Test
    public void testGetMethodReturnValue_withMethodWithParameters_returnsValue() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "(ILjava/lang/String;)Ljava/lang/Object;");

        Value returnValue = valueFactory.createReferenceValue("Ljava/lang/Object;", null, false, false);
        invocationUnit.setMethodReturnValue(programClass, method, returnValue);

        // Act
        Value retrievedValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve return value regardless of parameters");
    }

    /**
     * Tests that getMethodReturnValue returns consistent results across multiple invocation units.
     * Verifies that stored values persist across different StoringInvocationUnit instances.
     */
    @Test
    public void testGetMethodReturnValue_acrossDifferentInvocationUnits_returnsConsistentValue() {
        // Arrange
        StoringInvocationUnit unit1 = new StoringInvocationUnit(valueFactory);
        ProgramMethod method = createTestMethod("testMethod", "()I");

        Value intValue = valueFactory.createIntegerValue();
        unit1.setMethodReturnValue(programClass, method, intValue);

        // Create a different invocation unit
        StoringInvocationUnit unit2 = new StoringInvocationUnit(valueFactory);

        // Act - Retrieve using the static method (not tied to any particular unit)
        Value retrievedValue = StoringInvocationUnit.getMethodReturnValue(method);

        // Assert
        assertNotNull(retrievedValue, "Should retrieve value stored by any unit");
    }

    /**
     * Tests that getMethodReturnValue works with methods of different access modifiers.
     * Verifies the method works regardless of access modifier.
     */
    @Test
    public void testGetMethodReturnValue_withDifferentAccessModifiers_worksCorrectly() {
        // Arrange
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        ProgramMethod publicMethod = createMethodWithAccessFlags("publicMethod", "()I", AccessConstants.PUBLIC);
        ProgramMethod privateMethod = createMethodWithAccessFlags("privateMethod", "()I", AccessConstants.PRIVATE);
        ProgramMethod protectedMethod = createMethodWithAccessFlags("protectedMethod", "()I", AccessConstants.PROTECTED);

        Value value1 = valueFactory.createIntegerValue();
        Value value2 = valueFactory.createIntegerValue();
        Value value3 = valueFactory.createIntegerValue();

        invocationUnit.setMethodReturnValue(programClass, publicMethod, value1);
        invocationUnit.setMethodReturnValue(programClass, privateMethod, value2);
        invocationUnit.setMethodReturnValue(programClass, protectedMethod, value3);

        // Act
        Value retrieved1 = StoringInvocationUnit.getMethodReturnValue(publicMethod);
        Value retrieved2 = StoringInvocationUnit.getMethodReturnValue(privateMethod);
        Value retrieved3 = StoringInvocationUnit.getMethodReturnValue(protectedMethod);

        // Assert
        assertNotNull(retrieved1, "Should retrieve from public method");
        assertNotNull(retrieved2, "Should retrieve from private method");
        assertNotNull(retrieved3, "Should retrieve from protected method");
    }

    /**
     * Helper method to create a test method with the given name and descriptor.
     */
    private ProgramMethod createTestMethod(String name, String descriptor) {
        return createMethodWithAccessFlags(name, descriptor, AccessConstants.PUBLIC);
    }

    /**
     * Helper method to create a static test method with the given name and descriptor.
     */
    private ProgramMethod createStaticMethod(String name, String descriptor) {
        return createMethodWithAccessFlags(name, descriptor, AccessConstants.PUBLIC | AccessConstants.STATIC);
    }

    /**
     * Helper method to create a test method with specific access flags.
     */
    private ProgramMethod createMethodWithAccessFlags(String name, String descriptor, int accessFlags) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = accessFlags;

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

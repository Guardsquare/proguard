package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParameterUsageMarker#isParameterUsed(Method, int)}.
 *
 * The isParameterUsed static method checks whether a specific parameter is marked as being used
 * by the method. It delegates to MethodOptimizationInfo.getMethodOptimizationInfo(method).isParameterUsed(variableIndex).
 *
 * The variable index takes into account that long and double parameters occupy two stack slots.
 * Parameter usage is tracked using a bitmask (long value), allowing tracking of up to 64 parameter slots.
 *
 * Examples:
 * - Instance method(int a): variableIndex 0 = 'this', variableIndex 1 = 'a'
 * - Instance method(long a): variableIndex 0 = 'this', variableIndex 1-2 = 'a' (long takes 2 slots)
 * - Static method(int a, int b): variableIndex 0 = 'a', variableIndex 1 = 'b'
 *
 * The method returns true if the parameter at the given variable index has been marked as used,
 * or if the index is >= 64 (beyond the tracking capacity, conservatively treated as used).
 */
public class ParameterUsageMarkerClaude_isParameterUsedTest {

    private ProgramClass testClass;

    @BeforeEach
    public void setUp() {
        testClass = createProgramClassWithConstantPool();
    }

    /**
     * Tests isParameterUsed returns false for an unused parameter.
     */
    @Test
    public void testIsParameterUsed_withUnusedParameter_returnsFalse() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Initially, parameters should not be used
        // Act & Assert
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 should not be used initially");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 should not be used initially");
    }

    /**
     * Tests isParameterUsed returns true for a parameter that has been marked as used.
     */
    @Test
    public void testIsParameterUsed_withUsedParameter_returnsTrue() {
        // Arrange - create method and mark parameter as used
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act & Assert
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 should be used");
    }

    /**
     * Tests isParameterUsed with parameter at index 0 ('this' for instance methods).
     */
    @Test
    public void testIsParameterUsed_withIndexZero_checksThisParameter() {
        // Arrange - create instance method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Initially not used
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 (this) should not be used initially");

        // Mark 'this' as used
        ParameterUsageMarker.markParameterUsed(method, 0);

        // Act & Assert
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 (this) should be used after marking");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 should not be used");
    }

    /**
     * Tests isParameterUsed with static method parameters.
     * Static methods don't have 'this', so index 0 is the first actual parameter.
     */
    @Test
    public void testIsParameterUsed_withStaticMethod_checksFirstParameter() {
        // Arrange - create static method with int parameter
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Initially not used
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 should not be used initially");

        // Mark parameter at index 0 (first parameter in static method)
        ParameterUsageMarker.markParameterUsed(method, 0);

        // Act & Assert
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 should be used after marking");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 should not be used");
    }

    /**
     * Tests isParameterUsed with multiple parameters, some used and some unused.
     */
    @Test
    public void testIsParameterUsed_withMultipleParameters_checksIndependently() {
        // Arrange - create method with multiple parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(III)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameters at indices 0 and 2
        ParameterUsageMarker.markParameterUsed(method, 0); // 'this'
        ParameterUsageMarker.markParameterUsed(method, 2); // second int parameter

        // Act & Assert
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 2), "Parameter 2 should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 3), "Parameter 3 should not be used");
    }

    /**
     * Tests isParameterUsed with all parameters marked as used.
     */
    @Test
    public void testIsParameterUsed_withAllParametersUsed_returnsTrueForAll() {
        // Arrange - create method with two int parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(II)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark all parameters (this + 2 ints)
        ParameterUsageMarker.markParameterUsed(method, 0); // 'this'
        ParameterUsageMarker.markParameterUsed(method, 1); // first int
        ParameterUsageMarker.markParameterUsed(method, 2); // second int

        // Act & Assert
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 should be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 should be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 2), "Parameter 2 should be used");
    }

    /**
     * Tests isParameterUsed with long parameter that takes 2 slots.
     */
    @Test
    public void testIsParameterUsed_withLongParameter_checksCorrectSlot() {
        // Arrange - create method with long parameter
        // Instance method(long a) -> this is at 0, long 'a' occupies slots 1-2
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(J)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark the long parameter (starts at index 1)
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act & Assert
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 (this) should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 (long first slot) should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 2), "Parameter 2 (long second slot) should not be used");
    }

    /**
     * Tests isParameterUsed with both slots of a long parameter marked.
     */
    @Test
    public void testIsParameterUsed_withLongParameterBothSlots_checksIndependently() {
        // Arrange - create method with long parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(J)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark both slots of the long parameter
        ParameterUsageMarker.markParameterUsed(method, 1);
        ParameterUsageMarker.markParameterUsed(method, 2);

        // Act & Assert
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 (long first slot) should be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 2), "Parameter 2 (long second slot) should be used");
    }

    /**
     * Tests isParameterUsed with double parameter that takes 2 slots.
     */
    @Test
    public void testIsParameterUsed_withDoubleParameter_checksCorrectSlot() {
        // Arrange - create method with double parameter
        // Instance method(double a) -> this is at 0, double 'a' occupies slots 1-2
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(D)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark the double parameter (starts at index 1)
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act & Assert
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 (this) should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 (double first slot) should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 2), "Parameter 2 (double second slot) should not be used");
    }

    /**
     * Tests isParameterUsed with mixed parameter types.
     */
    @Test
    public void testIsParameterUsed_withMixedParameterTypes_checksCorrectly() {
        // Arrange - create method with int, long, int parameters
        // Instance method(int a, long b, int c):
        // this=0, int a=1, long b=2-3, int c=4
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(IJI)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark int a (index 1) and int c (index 4)
        ParameterUsageMarker.markParameterUsed(method, 1); // int a
        ParameterUsageMarker.markParameterUsed(method, 4); // int c

        // Act & Assert
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 (this) should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 (int a) should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 2), "Parameter 2 (long b first slot) should not be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 3), "Parameter 3 (long b second slot) should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 4), "Parameter 4 (int c) should be used");
    }

    /**
     * Tests isParameterUsed with large variable index near boundary (63).
     * Index 63 is the highest index that can be tracked in a 64-bit long.
     */
    @Test
    public void testIsParameterUsed_withIndex63_checksCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameter at index 63 (boundary value)
        ParameterUsageMarker.markParameterUsed(method, 63);

        // Act & Assert
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 63), "Parameter 63 should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 62), "Parameter 62 should not be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 should not be used");
    }

    /**
     * Tests isParameterUsed with variable index 64 and beyond.
     * Indices >= 64 exceed the bitmask capacity and are conservatively treated as used.
     */
    @Test
    public void testIsParameterUsed_withIndex64_returnsTrueByDefault() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act & Assert - index 64 is beyond the bitmask, so it's considered "used" by default
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 64), "Parameter 64 should be considered used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 65), "Parameter 65 should be considered used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 100), "Parameter 100 should be considered used");
    }

    /**
     * Tests isParameterUsed with constructor method.
     */
    @Test
    public void testIsParameterUsed_withConstructor_worksCorrectly() {
        // Arrange - create constructor
        ProgramMethod method = createMethodWithDescriptor(testClass, "<init>", "(Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark 'this' and String parameter
        ParameterUsageMarker.markParameterUsed(method, 0); // 'this'
        ParameterUsageMarker.markParameterUsed(method, 1); // String parameter

        // Act & Assert
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 (this) should be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 (String) should be used");
    }

    /**
     * Tests isParameterUsed with abstract method.
     */
    @Test
    public void testIsParameterUsed_withAbstractMethod_worksCorrectly() {
        // Arrange - create abstract method
        ProgramMethod method = createAbstractMethodWithDescriptor(testClass, "abstractMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameter
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act & Assert
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 should be used");
    }

    /**
     * Tests isParameterUsed with multiple methods independently.
     */
    @Test
    public void testIsParameterUsed_withMultipleMethods_maintainsIndependentState() {
        // Arrange - create three methods
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);

        ProgramMethod method3 = createMethodWithDescriptor(testClass, "method3", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method3);

        // Mark different parameters in each method
        ParameterUsageMarker.markParameterUsed(method1, 0); // mark 'this' in method1
        ParameterUsageMarker.markParameterUsed(method2, 1); // mark int parameter in method2
        // Don't mark anything in method3

        // Act & Assert - each method should have independent state
        assertTrue(ParameterUsageMarker.isParameterUsed(method1, 0), "Method1 parameter 0 should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method1, 1), "Method1 parameter 1 should not be used");

        assertFalse(ParameterUsageMarker.isParameterUsed(method2, 0), "Method2 parameter 0 should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method2, 1), "Method2 parameter 1 should be used");

        assertFalse(ParameterUsageMarker.isParameterUsed(method3, 0), "Method3 parameter 0 should not be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method3, 1), "Method3 parameter 1 should not be used");
    }

    /**
     * Tests isParameterUsed consistency across multiple calls.
     */
    @Test
    public void testIsParameterUsed_multipleCalls_returnsConsistentValue() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(II)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameter 1
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act & Assert - multiple calls should return consistent value
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "First call: parameter 1 should be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Second call: parameter 1 should be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Third call: parameter 1 should be used");

        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 should not be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 2), "Parameter 2 should not be used");
    }

    /**
     * Tests isParameterUsed with array parameter.
     * Arrays are references and take 1 slot.
     */
    @Test
    public void testIsParameterUsed_withArrayParameter_countsAsOneSlot() {
        // Arrange - create method with array parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "([I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark array parameter (index 1, after 'this')
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act & Assert
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 (this) should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 (array) should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 2), "Parameter 2 should not be used");
    }

    /**
     * Tests isParameterUsed with object reference parameter.
     */
    @Test
    public void testIsParameterUsed_withObjectParameter_countsAsOneSlot() {
        // Arrange - create method with Object parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark String parameter
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act & Assert
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 (this) should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 (String) should be used");
    }

    /**
     * Tests isParameterUsed with complex descriptor having many parameters.
     */
    @Test
    public void testIsParameterUsed_withComplexDescriptor_checksCorrectly() {
        // Arrange - create method with complex descriptor
        // Instance method(String, int[], int, long, double, Object):
        // this=0, String=1, int[]=2, int=3, long=4-5, double=6-7, Object=8
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;[IIJDLjava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark some parameters
        ParameterUsageMarker.markParameterUsed(method, 1); // String
        ParameterUsageMarker.markParameterUsed(method, 4); // long (first slot)
        ParameterUsageMarker.markParameterUsed(method, 8); // Object

        // Act & Assert
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 (this) should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 (String) should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 2), "Parameter 2 (int[]) should not be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 3), "Parameter 3 (int) should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 4), "Parameter 4 (long first slot) should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 5), "Parameter 5 (long second slot) should not be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 6), "Parameter 6 (double first slot) should not be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 7), "Parameter 7 (double second slot) should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 8), "Parameter 8 (Object) should be used");
    }

    /**
     * Tests isParameterUsed delegates correctly to MethodOptimizationInfo.
     */
    @Test
    public void testIsParameterUsed_delegatesToMethodOptimizationInfo() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Verify initial state
        assertFalse(info.isParameterUsed(1), "Initially parameter should not be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 1), "ParameterUsageMarker should also return false");

        // Mark parameter
        info.setParameterUsed(1);

        // Act & Assert - verify delegation worked
        assertTrue(info.isParameterUsed(1), "Parameter should be marked in info");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "ParameterUsageMarker should delegate correctly");
    }

    /**
     * Tests isParameterUsed with all category-2 types (long and double).
     */
    @Test
    public void testIsParameterUsed_withAllCategory2Types_checksSlots() {
        // Arrange - create method with long and double
        // Static method(long a, double b): long=0-1, double=2-3
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "testMethod", "(JD)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark first slot of each
        ParameterUsageMarker.markParameterUsed(method, 0); // long first slot
        ParameterUsageMarker.markParameterUsed(method, 2); // double first slot

        // Act & Assert
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 (long first slot) should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 (long second slot) should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 2), "Parameter 2 (double first slot) should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 3), "Parameter 3 (double second slot) should not be used");
    }

    /**
     * Tests isParameterUsed with varargs method.
     * Varargs are compiled as arrays, so they take 1 slot.
     */
    @Test
    public void testIsParameterUsed_withVarargsMethod_countsArrayAsOneSlot() {
        // Arrange - create varargs method (compiled as array)
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;[Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark varargs parameter (index 2, after 'this' and String)
        ParameterUsageMarker.markParameterUsed(method, 2);

        // Act & Assert
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 (this) should not be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 (String) should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 2), "Parameter 2 (varargs/array) should be used");
    }

    /**
     * Tests isParameterUsed with no parameters (empty method).
     */
    @Test
    public void testIsParameterUsed_withNoParameters_canCheckThis() {
        // Arrange - create method with no parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark 'this' parameter
        ParameterUsageMarker.markParameterUsed(method, 0);

        // Act & Assert
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 (this) should be used");
    }

    /**
     * Tests isParameterUsed with synchronized method.
     */
    @Test
    public void testIsParameterUsed_withSynchronizedMethod_worksCorrectly() {
        // Arrange - create synchronized method
        ProgramMethod method = createSynchronizedMethodWithDescriptor(testClass, "syncMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameter
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act & Assert
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 should be used");
    }

    /**
     * Tests isParameterUsed with native method.
     */
    @Test
    public void testIsParameterUsed_withNativeMethod_worksCorrectly() {
        // Arrange - create native method
        ProgramMethod method = createNativeMethodWithDescriptor(testClass, "nativeMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameter
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act & Assert
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 should be used");
    }

    /**
     * Tests isParameterUsed state is preserved across other operations.
     */
    @Test
    public void testIsParameterUsed_preservesStateAcrossOperations() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(II)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter
        ParameterUsageMarker.markParameterUsed(method, 1);
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 should be used after marking");

        // Do other operations
        info.setParameterSize(3);
        info.setSideEffects();
        info.setCatchesExceptions();

        // Act & Assert - parameter should still be marked
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 should still be used after other operations");
    }

    /**
     * Tests isParameterUsed with sparse marking pattern.
     */
    @Test
    public void testIsParameterUsed_sparseMarking_checksCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameters sparsely (0, 5, 10, 20, 40)
        int[] indicesToMark = {0, 5, 10, 20, 40};
        for (int index : indicesToMark) {
            ParameterUsageMarker.markParameterUsed(method, index);
        }

        // Act & Assert - only marked indices should be used
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 0), "Parameter 0 should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter 1 should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 5), "Parameter 5 should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 6), "Parameter 6 should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 10), "Parameter 10 should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 15), "Parameter 15 should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 20), "Parameter 20 should be used");
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 30), "Parameter 30 should not be used");
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 40), "Parameter 40 should be used");
    }

    /**
     * Tests isParameterUsed with indices throughout the range 0-63.
     */
    @Test
    public void testIsParameterUsed_fullRange_checksAllIndices() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark all indices from 0 to 63
        for (int i = 0; i < 64; i++) {
            ParameterUsageMarker.markParameterUsed(method, i);
        }

        // Act & Assert - all indices should be marked
        for (int i = 0; i < 64; i++) {
            assertTrue(ParameterUsageMarker.isParameterUsed(method, i), "Parameter " + i + " should be used");
        }
    }

    /**
     * Tests isParameterUsed with alternating marked/unmarked pattern.
     */
    @Test
    public void testIsParameterUsed_alternatingPattern_checksCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark every other parameter (0, 2, 4, 6, 8, 10)
        for (int i = 0; i <= 10; i += 2) {
            ParameterUsageMarker.markParameterUsed(method, i);
        }

        // Act & Assert
        for (int i = 0; i <= 10; i++) {
            if (i % 2 == 0) {
                assertTrue(ParameterUsageMarker.isParameterUsed(method, i), "Parameter " + i + " (even) should be used");
            } else {
                assertFalse(ParameterUsageMarker.isParameterUsed(method, i), "Parameter " + i + " (odd) should not be used");
            }
        }
    }

    /**
     * Tests isParameterUsed with parameters marked in reverse order.
     */
    @Test
    public void testIsParameterUsed_reverseOrder_checksCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(IIIII)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameters in reverse order (5, 4, 3, 2, 1, 0)
        for (int i = 5; i >= 0; i--) {
            ParameterUsageMarker.markParameterUsed(method, i);
        }

        // Act & Assert - all should be marked regardless of order
        for (int i = 0; i <= 5; i++) {
            assertTrue(ParameterUsageMarker.isParameterUsed(method, i), "Parameter " + i + " should be used");
        }
    }

    /**
     * Tests isParameterUsed before and after marking.
     */
    @Test
    public void testIsParameterUsed_beforeAndAfterMarking_reflectsChange() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act & Assert - before marking
        assertFalse(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter should not be used before marking");

        // Mark parameter
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act & Assert - after marking
        assertTrue(ParameterUsageMarker.isParameterUsed(method, 1), "Parameter should be used after marking");
    }

    /**
     * Tests isParameterUsed with method that has no optimization info should throw.
     */
    @Test
    public void testIsParameterUsed_withoutOptimizationInfo_throwsException() {
        // Arrange - create method without setting optimization info
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");

        // Act & Assert - should throw NullPointerException because no optimization info is set
        assertThrows(NullPointerException.class, () -> {
            ParameterUsageMarker.isParameterUsed(method, 1);
        }, "Should throw NullPointerException when optimization info is not set");
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    /**
     * Creates a ProgramClass with a minimal constant pool setup.
     */
    private ProgramClass createProgramClassWithConstantPool() {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a constant pool with enough space
        Constant[] constantPool = new Constant[100];
        constantPool[0] = null; // Index 0 is always null
        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 100;

        return programClass;
    }

    /**
     * Creates a ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC;

        // Find next available constant pool index
        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        // Add method name to constant pool
        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        // Add descriptor to constant pool
        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Creates a static ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createStaticMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.STATIC;

        // Find next available constant pool index
        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        // Add method name to constant pool
        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        // Add descriptor to constant pool
        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Creates an abstract ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createAbstractMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.ABSTRACT;

        // Find next available constant pool index
        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        // Add method name to constant pool
        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        // Add descriptor to constant pool
        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Creates a synchronized ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createSynchronizedMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.SYNCHRONIZED;

        // Find next available constant pool index
        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        // Add method name to constant pool
        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        // Add descriptor to constant pool
        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Creates a native ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createNativeMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.NATIVE;

        // Find next available constant pool index
        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        // Add method name to constant pool
        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        // Add descriptor to constant pool
        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Finds the next available index in the constant pool.
     */
    private int findNextAvailableConstantPoolIndex(ProgramClass programClass) {
        for (int i = 1; i < programClass.constantPool.length; i++) {
            if (programClass.constantPool[i] == null) {
                return i;
            }
        }
        throw new IllegalStateException("No available constant pool index");
    }
}

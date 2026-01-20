package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParameterUsageMarker#markParameterUsed(Method, int)}.
 *
 * The markParameterUsed static method marks a specific parameter as being used by the method.
 * It delegates to ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method).setParameterUsed(variableIndex).
 *
 * The variable index takes into account that long and double parameters occupy two stack slots.
 * Parameter usage is tracked using a bitmask (long value), allowing tracking of up to 64 parameter slots.
 *
 * Examples:
 * - Instance method(int a): variableIndex 0 = 'this', variableIndex 1 = 'a'
 * - Instance method(long a): variableIndex 0 = 'this', variableIndex 1-2 = 'a' (long takes 2 slots)
 * - Static method(int a, int b): variableIndex 0 = 'a', variableIndex 1 = 'b'
 *
 * The method is used during parameter usage analysis to track which parameters are actually
 * used in the method body. This information is later used for optimization (e.g., removing unused parameters).
 */
public class ParameterUsageMarkerClaude_markParameterUsedTest {

    private ProgramClass testClass;

    @BeforeEach
    public void setUp() {
        testClass = createProgramClassWithConstantPool();
    }

    /**
     * Tests markParameterUsed marks a single parameter as used.
     */
    @Test
    public void testMarkParameterUsed_withSingleParameter_marksAsUsed() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially, parameter should not be used
        assertFalse(info.isParameterUsed(0), "Parameter 0 should not be used initially");
        assertFalse(info.isParameterUsed(1), "Parameter 1 should not be used initially");

        // Act - mark parameter at index 1 as used (index 0 is 'this', index 1 is the int parameter)
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Assert - parameter should now be marked as used
        assertFalse(info.isParameterUsed(0), "Parameter 0 should still not be used");
        assertTrue(info.isParameterUsed(1), "Parameter 1 should be marked as used");
    }

    /**
     * Tests markParameterUsed with parameter at index 0 ('this' for instance methods).
     */
    @Test
    public void testMarkParameterUsed_withIndexZero_marksThisAsUsed() {
        // Arrange - create instance method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark parameter at index 0 (this) as used
        ParameterUsageMarker.markParameterUsed(method, 0);

        // Assert
        assertTrue(info.isParameterUsed(0), "Parameter 0 (this) should be marked as used");
        assertFalse(info.isParameterUsed(1), "Parameter 1 should not be used");
    }

    /**
     * Tests markParameterUsed with static method parameters.
     * Static methods don't have 'this', so index 0 is the first actual parameter.
     */
    @Test
    public void testMarkParameterUsed_withStaticMethod_marksFirstParameter() {
        // Arrange - create static method with int parameter
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark parameter at index 0 (first parameter in static method)
        ParameterUsageMarker.markParameterUsed(method, 0);

        // Assert
        assertTrue(info.isParameterUsed(0), "Parameter 0 should be marked as used");
        assertFalse(info.isParameterUsed(1), "Parameter 1 should not be used");
    }

    /**
     * Tests markParameterUsed with multiple parameters marked independently.
     */
    @Test
    public void testMarkParameterUsed_withMultipleParameters_marksIndependently() {
        // Arrange - create method with multiple parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(III)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark parameters at indices 0 and 2
        ParameterUsageMarker.markParameterUsed(method, 0); // 'this'
        ParameterUsageMarker.markParameterUsed(method, 2); // second int parameter

        // Assert
        assertTrue(info.isParameterUsed(0), "Parameter 0 should be marked as used");
        assertFalse(info.isParameterUsed(1), "Parameter 1 should not be used");
        assertTrue(info.isParameterUsed(2), "Parameter 2 should be marked as used");
        assertFalse(info.isParameterUsed(3), "Parameter 3 should not be used");
    }

    /**
     * Tests markParameterUsed with all parameters marked.
     */
    @Test
    public void testMarkParameterUsed_withAllParameters_marksAllAsUsed() {
        // Arrange - create method with two int parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(II)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark all parameters (this + 2 ints)
        ParameterUsageMarker.markParameterUsed(method, 0); // 'this'
        ParameterUsageMarker.markParameterUsed(method, 1); // first int
        ParameterUsageMarker.markParameterUsed(method, 2); // second int

        // Assert
        assertTrue(info.isParameterUsed(0), "Parameter 0 should be marked as used");
        assertTrue(info.isParameterUsed(1), "Parameter 1 should be marked as used");
        assertTrue(info.isParameterUsed(2), "Parameter 2 should be marked as used");
    }

    /**
     * Tests markParameterUsed is idempotent - marking the same parameter multiple times.
     */
    @Test
    public void testMarkParameterUsed_calledMultipleTimes_remainsUsed() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark the same parameter multiple times
        ParameterUsageMarker.markParameterUsed(method, 1);
        ParameterUsageMarker.markParameterUsed(method, 1);
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Assert - parameter should be marked as used (not duplicated)
        assertTrue(info.isParameterUsed(1), "Parameter 1 should be marked as used");
    }

    /**
     * Tests markParameterUsed with long parameter that takes 2 slots.
     * The variable index should account for the long taking 2 slots.
     */
    @Test
    public void testMarkParameterUsed_withLongParameter_accountsForTwoSlots() {
        // Arrange - create method with long parameter
        // Instance method(long a) -> this is at 0, long 'a' occupies slots 1-2
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(J)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark the long parameter (starts at index 1)
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Assert
        assertTrue(info.isParameterUsed(1), "Parameter 1 (long first slot) should be marked as used");
        assertFalse(info.isParameterUsed(2), "Parameter 2 (long second slot) is not automatically marked");
    }

    /**
     * Tests markParameterUsed with double parameter that takes 2 slots.
     */
    @Test
    public void testMarkParameterUsed_withDoubleParameter_accountsForTwoSlots() {
        // Arrange - create method with double parameter
        // Instance method(double a) -> this is at 0, double 'a' occupies slots 1-2
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(D)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark the double parameter (starts at index 1)
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Assert
        assertTrue(info.isParameterUsed(1), "Parameter 1 (double first slot) should be marked as used");
        assertFalse(info.isParameterUsed(2), "Parameter 2 (double second slot) is not automatically marked");
    }

    /**
     * Tests markParameterUsed with mixed parameter types.
     */
    @Test
    public void testMarkParameterUsed_withMixedParameterTypes_tracksCorrectly() {
        // Arrange - create method with int, long, int parameters
        // Instance method(int a, long b, int c):
        // this=0, int a=1, long b=2-3, int c=4
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(IJI)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark int a (index 1) and int c (index 4)
        ParameterUsageMarker.markParameterUsed(method, 1); // int a
        ParameterUsageMarker.markParameterUsed(method, 4); // int c

        // Assert
        assertFalse(info.isParameterUsed(0), "Parameter 0 (this) should not be used");
        assertTrue(info.isParameterUsed(1), "Parameter 1 (int a) should be marked as used");
        assertFalse(info.isParameterUsed(2), "Parameter 2 (long b first slot) should not be used");
        assertFalse(info.isParameterUsed(3), "Parameter 3 (long b second slot) should not be used");
        assertTrue(info.isParameterUsed(4), "Parameter 4 (int c) should be marked as used");
    }

    /**
     * Tests markParameterUsed with large variable index near boundary (63).
     * Index 63 is the highest index that can be tracked in a 64-bit long.
     */
    @Test
    public void testMarkParameterUsed_withIndex63_marksCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark parameter at index 63 (boundary value)
        ParameterUsageMarker.markParameterUsed(method, 63);

        // Assert
        assertTrue(info.isParameterUsed(63), "Parameter 63 should be marked as used");
        assertFalse(info.isParameterUsed(62), "Parameter 62 should not be used");
    }

    /**
     * Tests markParameterUsed with variable index 64 and beyond.
     * Indices >= 64 exceed the bitmask capacity and have special handling.
     */
    @Test
    public void testMarkParameterUsed_withIndex64_handledGracefully() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark parameter at index 64 (beyond bitmask)
        ParameterUsageMarker.markParameterUsed(method, 64);

        // Assert - index 64 is beyond the bitmask, so it's considered "used" by default
        assertTrue(info.isParameterUsed(64), "Parameter 64 should be considered used");
    }

    /**
     * Tests markParameterUsed with constructor method.
     */
    @Test
    public void testMarkParameterUsed_withConstructor_worksCorrectly() {
        // Arrange - create constructor
        ProgramMethod method = createMethodWithDescriptor(testClass, "<init>", "(Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark 'this' and String parameter
        ParameterUsageMarker.markParameterUsed(method, 0); // 'this'
        ParameterUsageMarker.markParameterUsed(method, 1); // String parameter

        // Assert
        assertTrue(info.isParameterUsed(0), "Parameter 0 (this) should be marked as used");
        assertTrue(info.isParameterUsed(1), "Parameter 1 (String) should be marked as used");
    }

    /**
     * Tests markParameterUsed with abstract method.
     */
    @Test
    public void testMarkParameterUsed_withAbstractMethod_worksCorrectly() {
        // Arrange - create abstract method
        ProgramMethod method = createAbstractMethodWithDescriptor(testClass, "abstractMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark parameter
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Assert
        assertTrue(info.isParameterUsed(1), "Parameter 1 should be marked as used");
    }

    /**
     * Tests markParameterUsed with multiple methods independently.
     */
    @Test
    public void testMarkParameterUsed_withMultipleMethods_maintainsIndependentState() {
        // Arrange - create three methods
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);
        ProgramMethodOptimizationInfo info1 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method1);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);
        ProgramMethodOptimizationInfo info2 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method2);

        ProgramMethod method3 = createMethodWithDescriptor(testClass, "method3", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method3);
        ProgramMethodOptimizationInfo info3 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method3);

        // Act - mark different parameters in each method
        ParameterUsageMarker.markParameterUsed(method1, 0); // mark 'this' in method1
        ParameterUsageMarker.markParameterUsed(method2, 1); // mark int parameter in method2
        // Don't mark anything in method3

        // Assert - each method should have independent state
        assertTrue(info1.isParameterUsed(0), "Method1 parameter 0 should be used");
        assertFalse(info1.isParameterUsed(1), "Method1 parameter 1 should not be used");

        assertFalse(info2.isParameterUsed(0), "Method2 parameter 0 should not be used");
        assertTrue(info2.isParameterUsed(1), "Method2 parameter 1 should be used");

        assertFalse(info3.isParameterUsed(0), "Method3 parameter 0 should not be used");
        assertFalse(info3.isParameterUsed(1), "Method3 parameter 1 should not be used");
    }

    /**
     * Tests markParameterUsed consistency across multiple calls.
     */
    @Test
    public void testMarkParameterUsed_multipleCalls_maintainsConsistentState() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(II)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark parameters in sequence
        ParameterUsageMarker.markParameterUsed(method, 1);
        assertTrue(info.isParameterUsed(1), "After first call, parameter 1 should be used");

        ParameterUsageMarker.markParameterUsed(method, 2);
        assertTrue(info.isParameterUsed(1), "After second call, parameter 1 should still be used");
        assertTrue(info.isParameterUsed(2), "After second call, parameter 2 should be used");

        ParameterUsageMarker.markParameterUsed(method, 0);
        assertTrue(info.isParameterUsed(0), "After third call, parameter 0 should be used");
        assertTrue(info.isParameterUsed(1), "After third call, parameter 1 should still be used");
        assertTrue(info.isParameterUsed(2), "After third call, parameter 2 should still be used");
    }

    /**
     * Tests markParameterUsed with array parameter.
     * Arrays are references and take 1 slot.
     */
    @Test
    public void testMarkParameterUsed_withArrayParameter_countsAsOneSlot() {
        // Arrange - create method with array parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "([I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark array parameter (index 1, after 'this')
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Assert
        assertTrue(info.isParameterUsed(1), "Parameter 1 (array) should be marked as used");
        assertFalse(info.isParameterUsed(2), "Parameter 2 should not exist");
    }

    /**
     * Tests markParameterUsed with object reference parameter.
     */
    @Test
    public void testMarkParameterUsed_withObjectParameter_countsAsOneSlot() {
        // Arrange - create method with Object parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark String parameter
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Assert
        assertTrue(info.isParameterUsed(1), "Parameter 1 (String) should be marked as used");
    }

    /**
     * Tests markParameterUsed with complex descriptor having many parameters.
     */
    @Test
    public void testMarkParameterUsed_withComplexDescriptor_tracksCorrectly() {
        // Arrange - create method with complex descriptor
        // Instance method(String, int[], int, long, double, Object):
        // this=0, String=1, int[]=2, int=3, long=4-5, double=6-7, Object=8
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;[IIJDLjava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark some parameters
        ParameterUsageMarker.markParameterUsed(method, 1); // String
        ParameterUsageMarker.markParameterUsed(method, 4); // long (first slot)
        ParameterUsageMarker.markParameterUsed(method, 8); // Object

        // Assert
        assertFalse(info.isParameterUsed(0), "Parameter 0 (this) should not be used");
        assertTrue(info.isParameterUsed(1), "Parameter 1 (String) should be marked as used");
        assertFalse(info.isParameterUsed(2), "Parameter 2 (int[]) should not be used");
        assertFalse(info.isParameterUsed(3), "Parameter 3 (int) should not be used");
        assertTrue(info.isParameterUsed(4), "Parameter 4 (long first slot) should be marked as used");
        assertFalse(info.isParameterUsed(5), "Parameter 5 (long second slot) should not be used");
        assertFalse(info.isParameterUsed(6), "Parameter 6 (double first slot) should not be used");
        assertFalse(info.isParameterUsed(7), "Parameter 7 (double second slot) should not be used");
        assertTrue(info.isParameterUsed(8), "Parameter 8 (Object) should be marked as used");
    }

    /**
     * Tests markParameterUsed delegates correctly to ProgramMethodOptimizationInfo.
     */
    @Test
    public void testMarkParameterUsed_delegatesToProgramMethodOptimizationInfo() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Verify initial state
        assertFalse(info.isParameterUsed(1), "Initially parameter should not be used");

        // Act - mark parameter
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Assert - verify delegation worked
        assertTrue(info.isParameterUsed(1), "Parameter should be marked through delegation");
    }

    /**
     * Tests markParameterUsed with all category-2 types (long and double).
     */
    @Test
    public void testMarkParameterUsed_withAllCategory2Types_tracksSlots() {
        // Arrange - create method with long and double
        // Static method(long a, double b): long=0-1, double=2-3
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "testMethod", "(JD)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark first slot of each
        ParameterUsageMarker.markParameterUsed(method, 0); // long first slot
        ParameterUsageMarker.markParameterUsed(method, 2); // double first slot

        // Assert
        assertTrue(info.isParameterUsed(0), "Parameter 0 (long first slot) should be marked as used");
        assertFalse(info.isParameterUsed(1), "Parameter 1 (long second slot) should not be marked");
        assertTrue(info.isParameterUsed(2), "Parameter 2 (double first slot) should be marked as used");
        assertFalse(info.isParameterUsed(3), "Parameter 3 (double second slot) should not be marked");
    }

    /**
     * Tests markParameterUsed with varargs method.
     * Varargs are compiled as arrays, so they take 1 slot.
     */
    @Test
    public void testMarkParameterUsed_withVarargsMethod_countsArrayAsOneSlot() {
        // Arrange - create varargs method (compiled as array)
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;[Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark varargs parameter (index 2, after 'this' and String)
        ParameterUsageMarker.markParameterUsed(method, 2);

        // Assert
        assertFalse(info.isParameterUsed(1), "Parameter 1 (String) should not be used");
        assertTrue(info.isParameterUsed(2), "Parameter 2 (varargs/array) should be marked as used");
    }

    /**
     * Tests markParameterUsed with no parameters (empty method).
     */
    @Test
    public void testMarkParameterUsed_withNoParameters_canMarkThis() {
        // Arrange - create method with no parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark 'this' parameter
        ParameterUsageMarker.markParameterUsed(method, 0);

        // Assert
        assertTrue(info.isParameterUsed(0), "Parameter 0 (this) should be marked as used");
    }

    /**
     * Tests markParameterUsed with synchronized method.
     */
    @Test
    public void testMarkParameterUsed_withSynchronizedMethod_worksCorrectly() {
        // Arrange - create synchronized method
        ProgramMethod method = createSynchronizedMethodWithDescriptor(testClass, "syncMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark parameter
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Assert
        assertTrue(info.isParameterUsed(1), "Parameter 1 should be marked as used");
    }

    /**
     * Tests markParameterUsed with native method.
     */
    @Test
    public void testMarkParameterUsed_withNativeMethod_worksCorrectly() {
        // Arrange - create native method
        ProgramMethod method = createNativeMethodWithDescriptor(testClass, "nativeMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark parameter
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Assert
        assertTrue(info.isParameterUsed(1), "Parameter 1 should be marked as used");
    }

    /**
     * Tests markParameterUsed state is preserved across other operations.
     */
    @Test
    public void testMarkParameterUsed_preservesStateAcrossOperations() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(II)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark parameter, then do other operations
        ParameterUsageMarker.markParameterUsed(method, 1);
        assertTrue(info.isParameterUsed(1), "Parameter 1 should be marked after marking");

        // Do other operations
        info.setParameterSize(3);
        info.setSideEffects();
        info.setCatchesExceptions();

        // Assert - parameter should still be marked
        assertTrue(info.isParameterUsed(1), "Parameter 1 should still be marked after other operations");
    }

    /**
     * Tests markParameterUsed with sequential marking pattern.
     */
    @Test
    public void testMarkParameterUsed_sequentialMarking_buildsCorrectMask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(IIIII)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark parameters sequentially
        for (int i = 0; i < 6; i++) {
            ParameterUsageMarker.markParameterUsed(method, i);
            assertTrue(info.isParameterUsed(i), "Parameter " + i + " should be marked immediately");
        }

        // Assert - all should be marked
        for (int i = 0; i < 6; i++) {
            assertTrue(info.isParameterUsed(i), "Parameter " + i + " should remain marked");
        }
    }

    /**
     * Tests markParameterUsed with sparse marking pattern.
     */
    @Test
    public void testMarkParameterUsed_sparseMarking_tracksCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark parameters sparsely (0, 5, 10, 20, 40)
        int[] indicesToMark = {0, 5, 10, 20, 40};
        for (int index : indicesToMark) {
            ParameterUsageMarker.markParameterUsed(method, index);
        }

        // Assert - only marked indices should be used
        assertTrue(info.isParameterUsed(0), "Parameter 0 should be marked");
        assertFalse(info.isParameterUsed(1), "Parameter 1 should not be marked");
        assertTrue(info.isParameterUsed(5), "Parameter 5 should be marked");
        assertFalse(info.isParameterUsed(6), "Parameter 6 should not be marked");
        assertTrue(info.isParameterUsed(10), "Parameter 10 should be marked");
        assertFalse(info.isParameterUsed(15), "Parameter 15 should not be marked");
        assertTrue(info.isParameterUsed(20), "Parameter 20 should be marked");
        assertFalse(info.isParameterUsed(30), "Parameter 30 should not be marked");
        assertTrue(info.isParameterUsed(40), "Parameter 40 should be marked");
    }

    /**
     * Tests markParameterUsed with indices throughout the range 0-63.
     */
    @Test
    public void testMarkParameterUsed_fullRange_tracksAllIndices() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark all indices from 0 to 63
        for (int i = 0; i < 64; i++) {
            ParameterUsageMarker.markParameterUsed(method, i);
        }

        // Assert - all indices should be marked
        for (int i = 0; i < 64; i++) {
            assertTrue(info.isParameterUsed(i), "Parameter " + i + " should be marked as used");
        }
    }

    /**
     * Tests markParameterUsed is thread-safe.
     */
    @Test
    public void testMarkParameterUsed_threadSafe_maintainsConsistency() throws InterruptedException {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - mark different parameters from different threads
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                ParameterUsageMarker.markParameterUsed(method, index);
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - all marked parameters should be set
        for (int i = 0; i < threadCount; i++) {
            assertTrue(info.isParameterUsed(i), "Parameter " + i + " should be marked as used");
        }
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

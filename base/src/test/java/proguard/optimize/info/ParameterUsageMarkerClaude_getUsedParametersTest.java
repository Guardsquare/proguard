package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParameterUsageMarker#getUsedParameters(Method)}.
 *
 * The getUsedParameters static method returns a bitmask (long value) representing which parameters
 * are marked as being used by the method. It delegates to
 * MethodOptimizationInfo.getMethodOptimizationInfo(method).getUsedParameters().
 *
 * The bitmask uses bits to represent parameter usage at each variable index:
 * - Bit 0 represents variable index 0 (typically 'this' for instance methods)
 * - Bit 1 represents variable index 1 (first parameter for instance methods)
 * - Bit N represents variable index N
 * - A set bit (1) indicates the parameter is used
 * - A clear bit (0) indicates the parameter is not used
 *
 * The variable index takes into account that long and double parameters occupy two stack slots.
 *
 * Examples:
 * - 0b001 (1L) means only parameter at index 0 is used
 * - 0b011 (3L) means parameters at indices 0 and 1 are used
 * - 0b101 (5L) means parameters at indices 0 and 2 are used
 * - 0L means no parameters are used
 * - -1L (all bits set) means all parameters are used
 *
 * The method is used during parameter usage analysis to get a complete picture of which
 * parameters are actually used in the method body.
 */
public class ParameterUsageMarkerClaude_getUsedParametersTest {

    private ProgramClass testClass;

    @BeforeEach
    public void setUp() {
        testClass = createProgramClassWithConstantPool();
    }

    /**
     * Tests getUsedParameters returns 0 when no parameters are marked as used.
     */
    @Test
    public void testGetUsedParameters_withNoParametersUsed_returnsZero() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Initially, no parameters should be used
        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert
        assertEquals(0L, usedParameters, "No parameters should be marked as used initially");
    }

    /**
     * Tests getUsedParameters returns correct bitmask when one parameter is used.
     */
    @Test
    public void testGetUsedParameters_withOneParameterUsed_returnsCorrectBitmask() {
        // Arrange - create method and mark one parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bit 1 should be set (0b10 = 2)
        assertEquals(0b10L, usedParameters, "Only parameter at index 1 should be marked");
        assertTrue((usedParameters & (1L << 1)) != 0, "Bit 1 should be set");
        assertTrue((usedParameters & (1L << 0)) == 0, "Bit 0 should not be set");
    }

    /**
     * Tests getUsedParameters with parameter at index 0 marked.
     */
    @Test
    public void testGetUsedParameters_withIndexZeroUsed_returnsOne() {
        // Arrange - create method and mark 'this' parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ParameterUsageMarker.markParameterUsed(method, 0);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bit 0 should be set (0b1 = 1)
        assertEquals(1L, usedParameters, "Only parameter at index 0 should be marked");
        assertTrue((usedParameters & (1L << 0)) != 0, "Bit 0 should be set");
    }

    /**
     * Tests getUsedParameters with multiple parameters marked.
     */
    @Test
    public void testGetUsedParameters_withMultipleParametersUsed_returnsCorrectBitmask() {
        // Arrange - create method with multiple parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(III)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameters at indices 0, 1, and 3 (this + first int + third int)
        ParameterUsageMarker.markParameterUsed(method, 0);
        ParameterUsageMarker.markParameterUsed(method, 1);
        ParameterUsageMarker.markParameterUsed(method, 3);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 0, 1, and 3 should be set (0b1011 = 11)
        assertEquals(0b1011L, usedParameters, "Parameters at indices 0, 1, and 3 should be marked");
        assertTrue((usedParameters & (1L << 0)) != 0, "Bit 0 should be set");
        assertTrue((usedParameters & (1L << 1)) != 0, "Bit 1 should be set");
        assertTrue((usedParameters & (1L << 2)) == 0, "Bit 2 should not be set");
        assertTrue((usedParameters & (1L << 3)) != 0, "Bit 3 should be set");
    }

    /**
     * Tests getUsedParameters with all parameters marked.
     */
    @Test
    public void testGetUsedParameters_withAllParametersUsed_returnsAllBitsSet() {
        // Arrange - create method with two int parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(II)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark all parameters (this + 2 ints = indices 0, 1, 2)
        ParameterUsageMarker.markParameterUsed(method, 0);
        ParameterUsageMarker.markParameterUsed(method, 1);
        ParameterUsageMarker.markParameterUsed(method, 2);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 0, 1, and 2 should be set (0b111 = 7)
        assertEquals(0b111L, usedParameters, "All three parameters should be marked");
        assertTrue((usedParameters & (1L << 0)) != 0, "Bit 0 should be set");
        assertTrue((usedParameters & (1L << 1)) != 0, "Bit 1 should be set");
        assertTrue((usedParameters & (1L << 2)) != 0, "Bit 2 should be set");
    }

    /**
     * Tests getUsedParameters with long parameter that takes 2 slots.
     */
    @Test
    public void testGetUsedParameters_withLongParameter_accountsForTwoSlots() {
        // Arrange - create method with long parameter
        // Instance method(long a) -> this is at 0, long 'a' occupies slots 1-2
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(J)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark the long parameter at indices 1 and 2
        ParameterUsageMarker.markParameterUsed(method, 1);
        ParameterUsageMarker.markParameterUsed(method, 2);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 1 and 2 should be set (0b110 = 6)
        assertEquals(0b110L, usedParameters, "Long parameter at indices 1-2 should be marked");
        assertTrue((usedParameters & (1L << 0)) == 0, "Bit 0 should not be set");
        assertTrue((usedParameters & (1L << 1)) != 0, "Bit 1 should be set");
        assertTrue((usedParameters & (1L << 2)) != 0, "Bit 2 should be set");
    }

    /**
     * Tests getUsedParameters with double parameter that takes 2 slots.
     */
    @Test
    public void testGetUsedParameters_withDoubleParameter_accountsForTwoSlots() {
        // Arrange - create method with double parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(D)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark the double parameter at indices 1 and 2
        ParameterUsageMarker.markParameterUsed(method, 1);
        ParameterUsageMarker.markParameterUsed(method, 2);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 1 and 2 should be set (0b110 = 6)
        assertEquals(0b110L, usedParameters, "Double parameter at indices 1-2 should be marked");
        assertEquals(6L, usedParameters, "Bitmask should equal 6");
    }

    /**
     * Tests getUsedParameters with mixed parameter types.
     */
    @Test
    public void testGetUsedParameters_withMixedParameterTypes_returnsCorrectBitmask() {
        // Arrange - create method with int, long, int parameters
        // Instance method(int a, long b, int c):
        // this=0, int a=1, long b=2-3, int c=4
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(IJI)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark 'this', int a, and int c
        ParameterUsageMarker.markParameterUsed(method, 0); // this
        ParameterUsageMarker.markParameterUsed(method, 1); // int a
        ParameterUsageMarker.markParameterUsed(method, 4); // int c

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 0, 1, and 4 should be set (0b10011 = 19)
        assertEquals(0b10011L, usedParameters, "Parameters at indices 0, 1, and 4 should be marked");
        assertTrue((usedParameters & (1L << 0)) != 0, "Bit 0 (this) should be set");
        assertTrue((usedParameters & (1L << 1)) != 0, "Bit 1 (int a) should be set");
        assertTrue((usedParameters & (1L << 2)) == 0, "Bit 2 (long b first slot) should not be set");
        assertTrue((usedParameters & (1L << 3)) == 0, "Bit 3 (long b second slot) should not be set");
        assertTrue((usedParameters & (1L << 4)) != 0, "Bit 4 (int c) should be set");
    }

    /**
     * Tests getUsedParameters with static method.
     */
    @Test
    public void testGetUsedParameters_withStaticMethod_doesNotHaveThisParameter() {
        // Arrange - create static method with int parameter
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameter at index 0 (first parameter in static method, no 'this')
        ParameterUsageMarker.markParameterUsed(method, 0);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bit 0 should be set (0b1 = 1)
        assertEquals(1L, usedParameters, "Only parameter at index 0 should be marked");
    }

    /**
     * Tests getUsedParameters with sparse parameter usage.
     */
    @Test
    public void testGetUsedParameters_withSparseUsage_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameters sparsely (0, 5, 10)
        ParameterUsageMarker.markParameterUsed(method, 0);
        ParameterUsageMarker.markParameterUsed(method, 5);
        ParameterUsageMarker.markParameterUsed(method, 10);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 0, 5, and 10 should be set
        long expected = (1L << 0) | (1L << 5) | (1L << 10);
        assertEquals(expected, usedParameters, "Parameters at indices 0, 5, and 10 should be marked");
        assertEquals(0b10000100001L, usedParameters, "Bitmask should have bits 0, 5, 10 set");
    }

    /**
     * Tests getUsedParameters with parameter at index 63 (boundary).
     */
    @Test
    public void testGetUsedParameters_withIndex63_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameter at index 63 (highest bit in 64-bit long)
        ParameterUsageMarker.markParameterUsed(method, 63);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bit 63 should be set
        long expected = 1L << 63;
        assertEquals(expected, usedParameters, "Parameter at index 63 should be marked");
        assertTrue(usedParameters < 0, "Bit 63 set makes the long negative");
        assertEquals(Long.MIN_VALUE, usedParameters, "Bit 63 set equals Long.MIN_VALUE");
    }

    /**
     * Tests getUsedParameters with indices 0 through 63 all marked.
     */
    @Test
    public void testGetUsedParameters_withAllIndicesMarked_returnsMinusOne() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark all indices from 0 to 63
        for (int i = 0; i < 64; i++) {
            ParameterUsageMarker.markParameterUsed(method, i);
        }

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - all bits should be set (-1L)
        assertEquals(-1L, usedParameters, "All 64 bits should be set");
    }

    /**
     * Tests getUsedParameters with constructor method.
     */
    @Test
    public void testGetUsedParameters_withConstructor_worksCorrectly() {
        // Arrange - create constructor
        ProgramMethod method = createMethodWithDescriptor(testClass, "<init>", "(Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark 'this' and String parameter
        ParameterUsageMarker.markParameterUsed(method, 0); // 'this'
        ParameterUsageMarker.markParameterUsed(method, 1); // String parameter

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 0 and 1 should be set (0b11 = 3)
        assertEquals(3L, usedParameters, "Both 'this' and String parameter should be marked");
    }

    /**
     * Tests getUsedParameters with abstract method.
     */
    @Test
    public void testGetUsedParameters_withAbstractMethod_worksCorrectly() {
        // Arrange - create abstract method
        ProgramMethod method = createAbstractMethodWithDescriptor(testClass, "abstractMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark only the int parameter, not 'this'
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - only bit 1 should be set (0b10 = 2)
        assertEquals(2L, usedParameters, "Only int parameter should be marked");
    }

    /**
     * Tests getUsedParameters consistency across multiple calls.
     */
    @Test
    public void testGetUsedParameters_multipleCalls_returnsConsistentValue() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(II)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameter 1
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act - call multiple times
        long result1 = ParameterUsageMarker.getUsedParameters(method);
        long result2 = ParameterUsageMarker.getUsedParameters(method);
        long result3 = ParameterUsageMarker.getUsedParameters(method);

        // Assert - all calls should return the same value
        assertEquals(result1, result2, "First and second calls should return same value");
        assertEquals(result2, result3, "Second and third calls should return same value");
        assertEquals(2L, result1, "All calls should return 2");
    }

    /**
     * Tests getUsedParameters with alternating parameter usage pattern.
     */
    @Test
    public void testGetUsedParameters_withAlternatingPattern_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark every other parameter (0, 2, 4, 6, 8)
        for (int i = 0; i <= 8; i += 2) {
            ParameterUsageMarker.markParameterUsed(method, i);
        }

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 0, 2, 4, 6, 8 should be set
        long expected = (1L << 0) | (1L << 2) | (1L << 4) | (1L << 6) | (1L << 8);
        assertEquals(expected, usedParameters, "Alternating parameters should be marked");
        assertEquals(0b101010101L, usedParameters, "Bitmask should have alternating pattern");
    }

    /**
     * Tests getUsedParameters with sequential parameter usage.
     */
    @Test
    public void testGetUsedParameters_withSequentialParameters_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(IIIII)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameters sequentially (0, 1, 2, 3, 4, 5)
        for (int i = 0; i <= 5; i++) {
            ParameterUsageMarker.markParameterUsed(method, i);
        }

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 0-5 should be set (0b111111 = 63)
        assertEquals(0b111111L, usedParameters, "First 6 parameters should be marked");
        assertEquals(63L, usedParameters, "Bitmask should equal 63");
    }

    /**
     * Tests getUsedParameters with array parameter.
     */
    @Test
    public void testGetUsedParameters_withArrayParameter_countsAsOneSlot() {
        // Arrange - create method with array parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "([I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark array parameter (index 1, after 'this')
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - only bit 1 should be set (0b10 = 2)
        assertEquals(2L, usedParameters, "Only array parameter should be marked");
    }

    /**
     * Tests getUsedParameters with object reference parameter.
     */
    @Test
    public void testGetUsedParameters_withObjectParameter_countsAsOneSlot() {
        // Arrange - create method with Object parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark 'this' and String parameter
        ParameterUsageMarker.markParameterUsed(method, 0);
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 0 and 1 should be set (0b11 = 3)
        assertEquals(3L, usedParameters, "Both 'this' and String should be marked");
    }

    /**
     * Tests getUsedParameters with complex descriptor having many parameters.
     */
    @Test
    public void testGetUsedParameters_withComplexDescriptor_returnsCorrectBitmask() {
        // Arrange - create method with complex descriptor
        // Instance method(String, int[], int, long, double, Object):
        // this=0, String=1, int[]=2, int=3, long=4-5, double=6-7, Object=8
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;[IIJDLjava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark String, int, and Object
        ParameterUsageMarker.markParameterUsed(method, 1); // String
        ParameterUsageMarker.markParameterUsed(method, 3); // int
        ParameterUsageMarker.markParameterUsed(method, 8); // Object

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 1, 3, and 8 should be set
        long expected = (1L << 1) | (1L << 3) | (1L << 8);
        assertEquals(expected, usedParameters, "String, int, and Object should be marked");
        assertEquals(0b100001010L, usedParameters, "Bitmask should have bits 1, 3, 8 set");
    }

    /**
     * Tests getUsedParameters delegates correctly to MethodOptimizationInfo.
     */
    @Test
    public void testGetUsedParameters_delegatesToMethodOptimizationInfo() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter via info directly
        info.setParameterUsed(1);

        // Act - call via ParameterUsageMarker
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - should match what info returns
        assertEquals(info.getUsedParameters(), usedParameters, "Should delegate to MethodOptimizationInfo");
        assertEquals(2L, usedParameters, "Parameter at index 1 should be marked");
    }

    /**
     * Tests getUsedParameters with all category-2 types (long and double).
     */
    @Test
    public void testGetUsedParameters_withAllCategory2Types_returnsCorrectBitmask() {
        // Arrange - create method with long and double
        // Static method(long a, double b): long=0-1, double=2-3
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "testMethod", "(JD)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark both long and double
        ParameterUsageMarker.markParameterUsed(method, 0); // long first slot
        ParameterUsageMarker.markParameterUsed(method, 1); // long second slot
        ParameterUsageMarker.markParameterUsed(method, 2); // double first slot
        ParameterUsageMarker.markParameterUsed(method, 3); // double second slot

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 0, 1, 2, 3 should be set (0b1111 = 15)
        assertEquals(15L, usedParameters, "All four slots should be marked");
        assertEquals(0b1111L, usedParameters, "Bitmask should have first 4 bits set");
    }

    /**
     * Tests getUsedParameters with no parameters (empty method).
     */
    @Test
    public void testGetUsedParameters_withNoParameters_canMarkThis() {
        // Arrange - create method with no parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark 'this' parameter
        ParameterUsageMarker.markParameterUsed(method, 0);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - only bit 0 should be set
        assertEquals(1L, usedParameters, "Only 'this' parameter should be marked");
    }

    /**
     * Tests getUsedParameters with synchronized method.
     */
    @Test
    public void testGetUsedParameters_withSynchronizedMethod_worksCorrectly() {
        // Arrange - create synchronized method
        ProgramMethod method = createSynchronizedMethodWithDescriptor(testClass, "syncMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark 'this' parameter (required for synchronized instance methods)
        ParameterUsageMarker.markParameterUsed(method, 0);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bit 0 should be set
        assertEquals(1L, usedParameters, "Only 'this' should be marked");
    }

    /**
     * Tests getUsedParameters with native method.
     */
    @Test
    public void testGetUsedParameters_withNativeMethod_worksCorrectly() {
        // Arrange - create native method
        ProgramMethod method = createNativeMethodWithDescriptor(testClass, "nativeMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark both parameters
        ParameterUsageMarker.markParameterUsed(method, 0);
        ParameterUsageMarker.markParameterUsed(method, 1);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 0 and 1 should be set (0b11 = 3)
        assertEquals(3L, usedParameters, "Both parameters should be marked");
    }

    /**
     * Tests getUsedParameters with multiple methods independently.
     */
    @Test
    public void testGetUsedParameters_withMultipleMethods_maintainsIndependentState() {
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
        ParameterUsageMarker.markParameterUsed(method3, 0); // mark 'this' in method3
        ParameterUsageMarker.markParameterUsed(method3, 1); // mark int parameter in method3

        // Act
        long usedParameters1 = ParameterUsageMarker.getUsedParameters(method1);
        long usedParameters2 = ParameterUsageMarker.getUsedParameters(method2);
        long usedParameters3 = ParameterUsageMarker.getUsedParameters(method3);

        // Assert - each method should have independent state
        assertEquals(1L, usedParameters1, "Method1 should have bit 0 set");
        assertEquals(2L, usedParameters2, "Method2 should have bit 1 set");
        assertEquals(3L, usedParameters3, "Method3 should have bits 0 and 1 set");
    }

    /**
     * Tests getUsedParameters state is preserved across other operations.
     */
    @Test
    public void testGetUsedParameters_preservesStateAcrossOperations() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(II)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters
        ParameterUsageMarker.markParameterUsed(method, 1);
        ParameterUsageMarker.markParameterUsed(method, 2);

        // Act - get initial value
        long initialValue = ParameterUsageMarker.getUsedParameters(method);

        // Do other operations
        info.setParameterSize(3);
        info.setSideEffects();
        info.setCatchesExceptions();

        // Act - get value again
        long afterOperations = ParameterUsageMarker.getUsedParameters(method);

        // Assert - value should be preserved
        assertEquals(initialValue, afterOperations, "Used parameters should be preserved");
        assertEquals(6L, afterOperations, "Bits 1 and 2 should still be set");
    }

    /**
     * Tests getUsedParameters with incremental parameter marking.
     */
    @Test
    public void testGetUsedParameters_withIncrementalMarking_accumulates() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(III)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act & Assert - mark parameters incrementally and check accumulation
        assertEquals(0L, ParameterUsageMarker.getUsedParameters(method), "Initially no parameters used");

        ParameterUsageMarker.markParameterUsed(method, 0);
        assertEquals(1L, ParameterUsageMarker.getUsedParameters(method), "After marking 0: bit 0 set");

        ParameterUsageMarker.markParameterUsed(method, 2);
        assertEquals(5L, ParameterUsageMarker.getUsedParameters(method), "After marking 2: bits 0,2 set");

        ParameterUsageMarker.markParameterUsed(method, 1);
        assertEquals(7L, ParameterUsageMarker.getUsedParameters(method), "After marking 1: bits 0,1,2 set");
    }

    /**
     * Tests getUsedParameters with high indices near boundary.
     */
    @Test
    public void testGetUsedParameters_withHighIndices_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameters at high indices (60, 61, 62, 63)
        ParameterUsageMarker.markParameterUsed(method, 60);
        ParameterUsageMarker.markParameterUsed(method, 61);
        ParameterUsageMarker.markParameterUsed(method, 62);
        ParameterUsageMarker.markParameterUsed(method, 63);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 60-63 should be set
        long expected = (1L << 60) | (1L << 61) | (1L << 62) | (1L << 63);
        assertEquals(expected, usedParameters, "Bits 60-63 should be set");
        assertTrue(usedParameters < 0, "Bit 63 set makes the value negative");
    }

    /**
     * Tests getUsedParameters with single high bit set.
     */
    @Test
    public void testGetUsedParameters_withSingleHighBit_returnsCorrectValue() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark only parameter at index 50
        ParameterUsageMarker.markParameterUsed(method, 50);

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - only bit 50 should be set
        long expected = 1L << 50;
        assertEquals(expected, usedParameters, "Only bit 50 should be set");
        assertTrue(usedParameters > 0, "Single bit below 63 keeps value positive");
    }

    /**
     * Tests getUsedParameters with varargs method.
     */
    @Test
    public void testGetUsedParameters_withVarargsMethod_countsArrayAsOneSlot() {
        // Arrange - create varargs method (compiled as array)
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;[Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark all parameters: this, String, and varargs array
        ParameterUsageMarker.markParameterUsed(method, 0); // this
        ParameterUsageMarker.markParameterUsed(method, 1); // String
        ParameterUsageMarker.markParameterUsed(method, 2); // varargs/array

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 0, 1, 2 should be set (0b111 = 7)
        assertEquals(7L, usedParameters, "All three parameters should be marked");
    }

    /**
     * Tests getUsedParameters returns zero for method without optimization info.
     * Note: This would actually throw NullPointerException, but we test the behavior.
     */
    @Test
    public void testGetUsedParameters_withoutOptimizationInfo_throwsException() {
        // Arrange - create method without setting optimization info
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");

        // Act & Assert - should throw NullPointerException because no optimization info is set
        assertThrows(NullPointerException.class, () -> {
            ParameterUsageMarker.getUsedParameters(method);
        }, "Should throw NullPointerException when optimization info is not set");
    }

    /**
     * Tests getUsedParameters with marking parameters in reverse order.
     */
    @Test
    public void testGetUsedParameters_withReverseOrderMarking_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(IIIII)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameters in reverse order (5, 4, 3, 2, 1, 0)
        for (int i = 5; i >= 0; i--) {
            ParameterUsageMarker.markParameterUsed(method, i);
        }

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - all 6 bits should be set regardless of order (0b111111 = 63)
        assertEquals(63L, usedParameters, "All 6 parameters should be marked");
    }

    /**
     * Tests getUsedParameters with only odd indices marked.
     */
    @Test
    public void testGetUsedParameters_withOddIndices_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark only odd indices (1, 3, 5, 7, 9)
        for (int i = 1; i <= 9; i += 2) {
            ParameterUsageMarker.markParameterUsed(method, i);
        }

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 1, 3, 5, 7, 9 should be set
        long expected = (1L << 1) | (1L << 3) | (1L << 5) | (1L << 7) | (1L << 9);
        assertEquals(expected, usedParameters, "Odd indices should be marked");
        assertEquals(0b1010101010L, usedParameters, "Bitmask should have odd bits set");
    }

    /**
     * Tests getUsedParameters with only even indices marked.
     */
    @Test
    public void testGetUsedParameters_withEvenIndices_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark only even indices (0, 2, 4, 6, 8)
        for (int i = 0; i <= 8; i += 2) {
            ParameterUsageMarker.markParameterUsed(method, i);
        }

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - bits 0, 2, 4, 6, 8 should be set
        long expected = (1L << 0) | (1L << 2) | (1L << 4) | (1L << 6) | (1L << 8);
        assertEquals(expected, usedParameters, "Even indices should be marked");
        assertEquals(0b101010101L, usedParameters, "Bitmask should have even bits set");
    }

    /**
     * Tests getUsedParameters can represent power-of-2 patterns.
     */
    @Test
    public void testGetUsedParameters_withPowerOfTwoIndices_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Mark parameters at power-of-2 indices (1, 2, 4, 8, 16, 32)
        int[] powerOfTwoIndices = {1, 2, 4, 8, 16, 32};
        for (int index : powerOfTwoIndices) {
            ParameterUsageMarker.markParameterUsed(method, index);
        }

        // Act
        long usedParameters = ParameterUsageMarker.getUsedParameters(method);

        // Assert - calculate expected bitmask
        long expected = 0;
        for (int index : powerOfTwoIndices) {
            expected |= (1L << index);
        }
        assertEquals(expected, usedParameters, "Power-of-2 indices should be marked");
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

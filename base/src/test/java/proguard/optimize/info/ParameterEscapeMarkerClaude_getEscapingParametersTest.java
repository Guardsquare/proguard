package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParameterEscapeMarker#getEscapingParameters(Method)}.
 *
 * The getEscapingParameters static method returns a bitmask of escaping parameters.
 * It delegates to MethodOptimizationInfo.getMethodOptimizationInfo(method).getEscapingParameters().
 *
 * A parameter "escapes" when it becomes reachable on the heap after the method exits.
 * The return value is a long bitmask where each bit represents a parameter:
 * - Bit 0 corresponds to parameter 0
 * - Bit 1 corresponds to parameter 1
 * - etc.
 *
 * Default behavior (MethodOptimizationInfo): Returns -1L (all bits set) conservatively.
 * After setNoEscapingParameters: Returns 0L (no bits set).
 * With ProgramMethodOptimizationInfo: Returns actual bitmask of escaping parameters.
 */
public class ParameterEscapeMarkerClaude_getEscapingParametersTest {

    private ProgramClass testClass;

    @BeforeEach
    public void setUp() {
        testClass = createProgramClassWithConstantPool();
    }

    /**
     * Tests getEscapingParameters returns -1L by default with MethodOptimizationInfo.
     * -1L means all bits are set, indicating all parameters escape conservatively.
     */
    @Test
    public void testGetEscapingParameters_withDefaultMethodOptimizationInfo_returnsNegativeOne() {
        // Arrange - create method with default MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, method);

        // Act
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should return -1L (all bits set)
        assertEquals(-1L, escapingParameters,
                "Should return -1L by default (conservative: all parameters escape)");
    }

    /**
     * Tests getEscapingParameters returns 0L after setNoEscapingParameters.
     * 0L means no bits are set, indicating no parameters escape.
     */
    @Test
    public void testGetEscapingParameters_afterSetNoEscapingParameters_returnsZero() {
        // Arrange - create method with MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        info.setNoEscapingParameters();
        method.setProcessingInfo(info);

        // Act
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should return 0L (no parameters escape)
        assertEquals(0L, escapingParameters,
                "Should return 0L after setNoEscapingParameters");
    }

    /**
     * Tests getEscapingParameters with ProgramMethodOptimizationInfo and no parameters marked.
     * Should return 0L initially.
     */
    @Test
    public void testGetEscapingParameters_withProgramMethodOptimizationInfo_initiallyReturnsZero() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should return 0L initially (no parameters marked as escaping)
        assertEquals(0L, escapingParameters,
                "Should return 0L initially with ProgramMethodOptimizationInfo");
    }

    /**
     * Tests getEscapingParameters after marking a single parameter as escaping.
     * Should return bitmask with only that bit set.
     */
    @Test
    public void testGetEscapingParameters_withSingleParameterMarked_returnsCorrectBitmask() {
        // Arrange - create method and mark parameter 0
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterEscaping(0);

        // Act
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should have bit 0 set
        assertEquals(1L, escapingParameters,
                "Should return 1L (bit 0 set) when parameter 0 is escaping");
    }

    /**
     * Tests getEscapingParameters after marking parameter 1 as escaping.
     * Should return bitmask with bit 1 set.
     */
    @Test
    public void testGetEscapingParameters_withParameterOne_returnsCorrectBitmask() {
        // Arrange - create method and mark parameter 1
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterEscaping(1);

        // Act
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should have bit 1 set (value = 2)
        assertEquals(2L, escapingParameters,
                "Should return 2L (bit 1 set) when parameter 1 is escaping");
    }

    /**
     * Tests getEscapingParameters with multiple parameters marked.
     * Should return bitmask with multiple bits set.
     */
    @Test
    public void testGetEscapingParameters_withMultipleParameters_returnsCorrectBitmask() {
        // Arrange - create method and mark parameters 0 and 2
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterEscaping(0);
        info.setParameterEscaping(2);

        // Act
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should have bits 0 and 2 set (value = 5 = 0b101)
        assertEquals(5L, escapingParameters,
                "Should return 5L (bits 0 and 2 set) when parameters 0 and 2 are escaping");
    }

    /**
     * Tests getEscapingParameters with all parameters in a 3-parameter method marked.
     * Should return bitmask with all three bits set.
     */
    @Test
    public void testGetEscapingParameters_withAllParametersMarked_returnsCorrectBitmask() {
        // Arrange - create method and mark all 3 parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterEscaping(0);
        info.setParameterEscaping(1);
        info.setParameterEscaping(2);

        // Act
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should have bits 0, 1, and 2 set (value = 7 = 0b111)
        assertEquals(7L, escapingParameters,
                "Should return 7L (bits 0, 1, 2 set) when all 3 parameters are escaping");
    }

    /**
     * Tests getEscapingParameters using updateEscapingParameters with a bitmask.
     * Should return the same bitmask.
     */
    @Test
    public void testGetEscapingParameters_afterUpdateEscapingParameters_returnsCorrectBitmask() {
        // Arrange - create method and update with bitmask
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        long mask = (1L << 0) | (1L << 1);  // bits 0 and 1 set
        info.updateEscapingParameters(mask);

        // Act
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should return the same mask
        assertEquals(3L, escapingParameters,
                "Should return 3L (bits 0 and 1 set) after updateEscapingParameters");
    }

    /**
     * Tests getEscapingParameters with higher bit positions.
     * Tests that bitmask works correctly for larger parameter indices.
     */
    @Test
    public void testGetEscapingParameters_withHigherBits_returnsCorrectBitmask() {
        // Arrange - create method with many parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/Double;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 4
        info.setParameterEscaping(4);

        // Act
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should have bit 4 set (value = 16 = 2^4)
        assertEquals(16L, escapingParameters,
                "Should return 16L (bit 4 set) when parameter 4 is escaping");
    }

    /**
     * Tests getEscapingParameters with very high bit positions.
     * Tests bitmask works for parameters at index 10 and above.
     */
    @Test
    public void testGetEscapingParameters_withVeryHighBits_returnsCorrectBitmask() {
        // Arrange - create method (descriptor doesn't matter for this test)
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 10
        info.setParameterEscaping(10);

        // Act
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should have bit 10 set (value = 1024 = 2^10)
        assertEquals(1024L, escapingParameters,
                "Should return 1024L (bit 10 set) when parameter 10 is escaping");
    }

    /**
     * Tests getEscapingParameters with combined high and low bits.
     * Tests complex bitmask patterns.
     */
    @Test
    public void testGetEscapingParameters_withCombinedHighAndLowBits_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters 0, 3, and 8
        info.setParameterEscaping(0);
        info.setParameterEscaping(3);
        info.setParameterEscaping(8);

        // Act
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should have bits 0, 3, and 8 set (value = 1 + 8 + 256 = 265)
        long expected = (1L << 0) | (1L << 3) | (1L << 8);
        assertEquals(expected, escapingParameters,
                "Should return correct bitmask with bits 0, 3, and 8 set");
        assertEquals(265L, escapingParameters,
                "Should return 265L when parameters 0, 3, and 8 are escaping");
    }

    /**
     * Tests getEscapingParameters is consistent across multiple calls.
     * Should return the same value each time.
     */
    @Test
    public void testGetEscapingParameters_multipleCalls_returnsConsistentResults() {
        // Arrange - create method and mark parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterEscaping(1);

        // Act - call multiple times
        long firstCall = ParameterEscapeMarker.getEscapingParameters(method);
        long secondCall = ParameterEscapeMarker.getEscapingParameters(method);
        long thirdCall = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - all calls should return the same value
        assertEquals(2L, firstCall, "First call should return 2L");
        assertEquals(2L, secondCall, "Second call should return 2L");
        assertEquals(2L, thirdCall, "Third call should return 2L");
        assertEquals(firstCall, secondCall, "First and second calls should match");
        assertEquals(secondCall, thirdCall, "Second and third calls should match");
    }

    /**
     * Tests getEscapingParameters with different methods independently.
     * Each method should have its own independent bitmask.
     */
    @Test
    public void testGetEscapingParameters_withDifferentMethods_worksIndependently() {
        // Arrange - create two methods with different escaping parameters
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);
        ProgramMethodOptimizationInfo info1 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method1);
        info1.setParameterEscaping(0);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2", "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);
        ProgramMethodOptimizationInfo info2 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method2);
        info2.setParameterEscaping(1);

        // Act
        long method1Mask = ParameterEscapeMarker.getEscapingParameters(method1);
        long method2Mask = ParameterEscapeMarker.getEscapingParameters(method2);

        // Assert - methods should have different masks
        assertEquals(1L, method1Mask, "Method1 should return 1L (bit 0 set)");
        assertEquals(2L, method2Mask, "Method2 should return 2L (bit 1 set)");
        assertNotEquals(method1Mask, method2Mask, "Different methods should have different masks");
    }

    /**
     * Tests getEscapingParameters reflects incremental updates.
     * As parameters are marked, the bitmask should update accordingly.
     */
    @Test
    public void testGetEscapingParameters_withIncrementalUpdates_reflectsChanges() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act & Assert - check after each parameter is marked
        assertEquals(0L, ParameterEscapeMarker.getEscapingParameters(method),
                "Initially should return 0L");

        info.setParameterEscaping(0);
        assertEquals(1L, ParameterEscapeMarker.getEscapingParameters(method),
                "After marking parameter 0, should return 1L");

        info.setParameterEscaping(1);
        assertEquals(3L, ParameterEscapeMarker.getEscapingParameters(method),
                "After marking parameters 0 and 1, should return 3L");

        info.setParameterEscaping(2);
        assertEquals(7L, ParameterEscapeMarker.getEscapingParameters(method),
                "After marking all 3 parameters, should return 7L");
    }

    /**
     * Tests getEscapingParameters with static method.
     * Static methods don't have 'this', so parameter 0 is the first declared parameter.
     */
    @Test
    public void testGetEscapingParameters_withStaticMethod_worksCorrectly() {
        // Arrange - create static method
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "staticMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterEscaping(0);
        info.setParameterEscaping(1);

        // Act
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should have bits 0 and 1 set
        assertEquals(3L, escapingParameters,
                "Static method should return 3L with parameters 0 and 1 escaping");
    }

    /**
     * Tests getEscapingParameters can represent up to 64 parameters.
     * The long type has 64 bits, so it can track up to 64 parameters (indices 0-63).
     */
    @Test
    public void testGetEscapingParameters_withHighParameterIndex_handlesUpTo63() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter at index 63 (highest bit in long)
        info.setParameterEscaping(63);

        // Act
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should have bit 63 set
        long expected = 1L << 63;
        assertEquals(expected, escapingParameters,
                "Should correctly set bit 63 (highest bit in long)");
        assertTrue(escapingParameters < 0, "Bit 63 set means the long is negative");
    }

    /**
     * Tests getEscapingParameters with alternating bit pattern.
     * Tests complex bitmask scenarios.
     */
    @Test
    public void testGetEscapingParameters_withAlternatingPattern_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters 0, 2, 4, 6 (alternating pattern)
        info.setParameterEscaping(0);
        info.setParameterEscaping(2);
        info.setParameterEscaping(4);
        info.setParameterEscaping(6);

        // Act
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should have alternating bits set (0b01010101 = 85)
        long expected = (1L << 0) | (1L << 2) | (1L << 4) | (1L << 6);
        assertEquals(expected, escapingParameters,
                "Should return correct bitmask with alternating bits");
        assertEquals(85L, escapingParameters,
                "Should return 85L with parameters 0, 2, 4, 6 escaping");
    }

    /**
     * Tests getEscapingParameters delegates correctly to MethodOptimizationInfo.
     * Verifies the static method properly wraps the instance method.
     */
    @Test
    public void testGetEscapingParameters_delegatesToMethodOptimizationInfo() {
        // Arrange - create method with custom MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo customInfo = new MethodOptimizationInfo();
        method.setProcessingInfo(customInfo);

        // Act
        long result = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should delegate to info's getEscapingParameters
        // Default MethodOptimizationInfo returns -1L
        assertEquals(-1L, result, "Should delegate to MethodOptimizationInfo.getEscapingParameters");
    }

    /**
     * Tests getEscapingParameters with updateEscapingParameters multiple times.
     * Updates should be cumulative (OR operation).
     */
    @Test
    public void testGetEscapingParameters_withMultipleUpdates_accumulatesCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - update with different masks
        info.updateEscapingParameters(1L << 0);  // Set bit 0
        info.updateEscapingParameters(1L << 2);  // Set bit 2
        info.updateEscapingParameters(1L << 5);  // Set bit 5

        // Assert - all bits should be set (cumulative)
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);
        long expected = (1L << 0) | (1L << 2) | (1L << 5);
        assertEquals(expected, escapingParameters,
                "Multiple updates should accumulate (OR operation)");
        assertEquals(37L, escapingParameters, "Should return 37L (bits 0, 2, 5 set)");
    }

    /**
     * Tests getEscapingParameters after marking same parameter twice.
     * Marking the same parameter multiple times should be idempotent.
     */
    @Test
    public void testGetEscapingParameters_markingSameParameterTwice_isIdempotent() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark parameter 0 twice
        info.setParameterEscaping(0);
        long afterFirst = ParameterEscapeMarker.getEscapingParameters(method);

        info.setParameterEscaping(0);
        long afterSecond = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - both should return the same value
        assertEquals(1L, afterFirst, "After first mark should return 1L");
        assertEquals(1L, afterSecond, "After second mark should still return 1L");
        assertEquals(afterFirst, afterSecond, "Marking same parameter twice should be idempotent");
    }

    /**
     * Tests getEscapingParameters with method having no parameters.
     * Even with no parameters, the method should work correctly.
     */
    @Test
    public void testGetEscapingParameters_withNoParameters_returnsZero() {
        // Arrange - create method with no parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act
        long escapingParameters = ParameterEscapeMarker.getEscapingParameters(method);

        // Assert - should return 0L (no parameters to escape)
        assertEquals(0L, escapingParameters,
                "Method with no parameters should return 0L");
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

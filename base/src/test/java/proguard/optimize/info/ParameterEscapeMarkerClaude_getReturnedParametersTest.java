package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParameterEscapeMarker#getReturnedParameters(Method)}.
 *
 * The getReturnedParameters static method returns a bitmask of parameters that are returned
 * from a method. It delegates to MethodOptimizationInfo.getMethodOptimizationInfo(method).getReturnedParameters().
 *
 * A parameter is "returned" when the method returns that exact parameter reference.
 * The return value is a long bitmask where each bit represents a parameter:
 * - Bit 0 corresponds to parameter 0
 * - Bit 1 corresponds to parameter 1
 * - etc.
 *
 * Default behavior (MethodOptimizationInfo): Returns -1L (all bits set) conservatively.
 * With ProgramMethodOptimizationInfo: Returns actual bitmask of returned parameters.
 */
public class ParameterEscapeMarkerClaude_getReturnedParametersTest {

    private ProgramClass testClass;

    @BeforeEach
    public void setUp() {
        testClass = createProgramClassWithConstantPool();
    }

    /**
     * Tests getReturnedParameters returns -1L by default with MethodOptimizationInfo.
     * -1L means all bits are set, indicating all parameters might be returned conservatively.
     */
    @Test
    public void testGetReturnedParameters_withDefaultMethodOptimizationInfo_returnsNegativeOne() {
        // Arrange - create method with default MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, method);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should return -1L (all bits set)
        assertEquals(-1L, returnedParameters,
                "Should return -1L by default (conservative: all parameters might be returned)");
    }

    /**
     * Tests getReturnedParameters with ProgramMethodOptimizationInfo and no parameters marked.
     * Should return 0L initially.
     */
    @Test
    public void testGetReturnedParameters_withProgramMethodOptimizationInfo_initiallyReturnsZero() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should return 0L initially (no parameters marked as returned)
        assertEquals(0L, returnedParameters,
                "Should return 0L initially with ProgramMethodOptimizationInfo");
    }

    /**
     * Tests getReturnedParameters after marking a single parameter as returned.
     * Should return bitmask with only that bit set.
     */
    @Test
    public void testGetReturnedParameters_withSingleParameterMarked_returnsCorrectBitmask() {
        // Arrange - create method and mark parameter 0
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterReturned(0);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should have bit 0 set
        assertEquals(1L, returnedParameters,
                "Should return 1L (bit 0 set) when parameter 0 is returned");
    }

    /**
     * Tests getReturnedParameters after marking parameter 1 as returned.
     * Should return bitmask with bit 1 set.
     */
    @Test
    public void testGetReturnedParameters_withParameterOne_returnsCorrectBitmask() {
        // Arrange - create method and mark parameter 1
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterReturned(1);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should have bit 1 set (value = 2)
        assertEquals(2L, returnedParameters,
                "Should return 2L (bit 1 set) when parameter 1 is returned");
    }

    /**
     * Tests getReturnedParameters with multiple parameters marked.
     * Should return bitmask with multiple bits set.
     */
    @Test
    public void testGetReturnedParameters_withMultipleParameters_returnsCorrectBitmask() {
        // Arrange - create method and mark parameters 0 and 2
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterReturned(0);
        info.setParameterReturned(2);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should have bits 0 and 2 set (value = 5 = 0b101)
        assertEquals(5L, returnedParameters,
                "Should return 5L (bits 0 and 2 set) when parameters 0 and 2 are returned");
    }

    /**
     * Tests getReturnedParameters with all parameters in a 3-parameter method marked.
     * Should return bitmask with all three bits set.
     */
    @Test
    public void testGetReturnedParameters_withAllParametersMarked_returnsCorrectBitmask() {
        // Arrange - create method and mark all 3 parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterReturned(0);
        info.setParameterReturned(1);
        info.setParameterReturned(2);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should have bits 0, 1, and 2 set (value = 7 = 0b111)
        assertEquals(7L, returnedParameters,
                "Should return 7L (bits 0, 1, 2 set) when all 3 parameters are returned");
    }

    /**
     * Tests getReturnedParameters using updateReturnedParameters with a bitmask.
     * Should return the same bitmask.
     */
    @Test
    public void testGetReturnedParameters_afterUpdateReturnedParameters_returnsCorrectBitmask() {
        // Arrange - create method and update with bitmask
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        long mask = (1L << 0) | (1L << 1);  // bits 0 and 1 set
        info.updateReturnedParameters(mask);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should return the same mask
        assertEquals(3L, returnedParameters,
                "Should return 3L (bits 0 and 1 set) after updateReturnedParameters");
    }

    /**
     * Tests getReturnedParameters with higher bit positions.
     * Tests that bitmask works correctly for larger parameter indices.
     */
    @Test
    public void testGetReturnedParameters_withHigherBits_returnsCorrectBitmask() {
        // Arrange - create method with many parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/Double;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 4
        info.setParameterReturned(4);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should have bit 4 set (value = 16 = 2^4)
        assertEquals(16L, returnedParameters,
                "Should return 16L (bit 4 set) when parameter 4 is returned");
    }

    /**
     * Tests getReturnedParameters with very high bit positions.
     * Tests bitmask works for parameters at index 10 and above.
     */
    @Test
    public void testGetReturnedParameters_withVeryHighBits_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 10
        info.setParameterReturned(10);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should have bit 10 set (value = 1024 = 2^10)
        assertEquals(1024L, returnedParameters,
                "Should return 1024L (bit 10 set) when parameter 10 is returned");
    }

    /**
     * Tests getReturnedParameters with combined high and low bits.
     * Tests complex bitmask patterns.
     */
    @Test
    public void testGetReturnedParameters_withCombinedHighAndLowBits_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters 0, 3, and 8
        info.setParameterReturned(0);
        info.setParameterReturned(3);
        info.setParameterReturned(8);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should have bits 0, 3, and 8 set (value = 1 + 8 + 256 = 265)
        long expected = (1L << 0) | (1L << 3) | (1L << 8);
        assertEquals(expected, returnedParameters,
                "Should return correct bitmask with bits 0, 3, and 8 set");
        assertEquals(265L, returnedParameters,
                "Should return 265L when parameters 0, 3, and 8 are returned");
    }

    /**
     * Tests getReturnedParameters is consistent across multiple calls.
     * Should return the same value each time.
     */
    @Test
    public void testGetReturnedParameters_multipleCalls_returnsConsistentResults() {
        // Arrange - create method and mark parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterReturned(1);

        // Act - call multiple times
        long firstCall = ParameterEscapeMarker.getReturnedParameters(method);
        long secondCall = ParameterEscapeMarker.getReturnedParameters(method);
        long thirdCall = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - all calls should return the same value
        assertEquals(2L, firstCall, "First call should return 2L");
        assertEquals(2L, secondCall, "Second call should return 2L");
        assertEquals(2L, thirdCall, "Third call should return 2L");
        assertEquals(firstCall, secondCall, "First and second calls should match");
        assertEquals(secondCall, thirdCall, "Second and third calls should match");
    }

    /**
     * Tests getReturnedParameters with different methods independently.
     * Each method should have its own independent bitmask.
     */
    @Test
    public void testGetReturnedParameters_withDifferentMethods_worksIndependently() {
        // Arrange - create two methods with different returned parameters
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);
        ProgramMethodOptimizationInfo info1 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method1);
        info1.setParameterReturned(0);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2",
                "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);
        ProgramMethodOptimizationInfo info2 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method2);
        info2.setParameterReturned(1);

        // Act
        long method1Mask = ParameterEscapeMarker.getReturnedParameters(method1);
        long method2Mask = ParameterEscapeMarker.getReturnedParameters(method2);

        // Assert - methods should have different masks
        assertEquals(1L, method1Mask, "Method1 should return 1L (bit 0 set)");
        assertEquals(2L, method2Mask, "Method2 should return 2L (bit 1 set)");
        assertNotEquals(method1Mask, method2Mask, "Different methods should have different masks");
    }

    /**
     * Tests getReturnedParameters reflects incremental updates.
     * As parameters are marked, the bitmask should update accordingly.
     */
    @Test
    public void testGetReturnedParameters_withIncrementalUpdates_reflectsChanges() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act & Assert - check after each parameter is marked
        assertEquals(0L, ParameterEscapeMarker.getReturnedParameters(method),
                "Initially should return 0L");

        info.setParameterReturned(0);
        assertEquals(1L, ParameterEscapeMarker.getReturnedParameters(method),
                "After marking parameter 0, should return 1L");

        info.setParameterReturned(1);
        assertEquals(3L, ParameterEscapeMarker.getReturnedParameters(method),
                "After marking parameters 0 and 1, should return 3L");

        info.setParameterReturned(2);
        assertEquals(7L, ParameterEscapeMarker.getReturnedParameters(method),
                "After marking all 3 parameters, should return 7L");
    }

    /**
     * Tests getReturnedParameters with static method.
     * Static methods don't have 'this', so parameter 0 is the first declared parameter.
     */
    @Test
    public void testGetReturnedParameters_withStaticMethod_worksCorrectly() {
        // Arrange - create static method
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "staticMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterReturned(0);
        info.setParameterReturned(1);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should have bits 0 and 1 set
        assertEquals(3L, returnedParameters,
                "Static method should return 3L with parameters 0 and 1 returned");
    }

    /**
     * Tests getReturnedParameters can represent up to 64 parameters.
     * The long type has 64 bits, so it can track up to 64 parameters (indices 0-63).
     */
    @Test
    public void testGetReturnedParameters_withHighParameterIndex_handlesUpTo63() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter at index 63 (highest bit in long)
        info.setParameterReturned(63);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should have bit 63 set
        long expected = 1L << 63;
        assertEquals(expected, returnedParameters,
                "Should correctly set bit 63 (highest bit in long)");
        assertTrue(returnedParameters < 0, "Bit 63 set means the long is negative");
    }

    /**
     * Tests getReturnedParameters with alternating bit pattern.
     * Tests complex bitmask scenarios.
     */
    @Test
    public void testGetReturnedParameters_withAlternatingPattern_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters 0, 2, 4, 6 (alternating pattern)
        info.setParameterReturned(0);
        info.setParameterReturned(2);
        info.setParameterReturned(4);
        info.setParameterReturned(6);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should have alternating bits set (0b01010101 = 85)
        long expected = (1L << 0) | (1L << 2) | (1L << 4) | (1L << 6);
        assertEquals(expected, returnedParameters,
                "Should return correct bitmask with alternating bits");
        assertEquals(85L, returnedParameters,
                "Should return 85L with parameters 0, 2, 4, 6 returned");
    }

    /**
     * Tests getReturnedParameters delegates correctly to MethodOptimizationInfo.
     * Verifies the static method properly wraps the instance method.
     */
    @Test
    public void testGetReturnedParameters_delegatesToMethodOptimizationInfo() {
        // Arrange - create method with custom MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        MethodOptimizationInfo customInfo = new MethodOptimizationInfo();
        method.setProcessingInfo(customInfo);

        // Act
        long result = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should delegate to info's getReturnedParameters
        // Default MethodOptimizationInfo returns -1L
        assertEquals(-1L, result, "Should delegate to MethodOptimizationInfo.getReturnedParameters");
    }

    /**
     * Tests getReturnedParameters with updateReturnedParameters multiple times.
     * Updates should be cumulative (OR operation).
     */
    @Test
    public void testGetReturnedParameters_withMultipleUpdates_accumulatesCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - update with different masks
        info.updateReturnedParameters(1L << 0);  // Set bit 0
        info.updateReturnedParameters(1L << 2);  // Set bit 2
        info.updateReturnedParameters(1L << 5);  // Set bit 5

        // Assert - all bits should be set (cumulative)
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);
        long expected = (1L << 0) | (1L << 2) | (1L << 5);
        assertEquals(expected, returnedParameters,
                "Multiple updates should accumulate (OR operation)");
        assertEquals(37L, returnedParameters, "Should return 37L (bits 0, 2, 5 set)");
    }

    /**
     * Tests getReturnedParameters after marking same parameter twice.
     * Marking the same parameter multiple times should be idempotent.
     */
    @Test
    public void testGetReturnedParameters_markingSameParameterTwice_isIdempotent() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark parameter 0 twice
        info.setParameterReturned(0);
        long afterFirst = ParameterEscapeMarker.getReturnedParameters(method);

        info.setParameterReturned(0);
        long afterSecond = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - both should return the same value
        assertEquals(1L, afterFirst, "After first mark should return 1L");
        assertEquals(1L, afterSecond, "After second mark should still return 1L");
        assertEquals(afterFirst, afterSecond, "Marking same parameter twice should be idempotent");
    }

    /**
     * Tests getReturnedParameters with method having no parameters.
     * Even with no parameters, the method should work correctly.
     */
    @Test
    public void testGetReturnedParameters_withNoParameters_returnsZero() {
        // Arrange - create method with no parameters (returns void)
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should return 0L (no parameters to return)
        assertEquals(0L, returnedParameters,
                "Method with no parameters should return 0L");
    }

    /**
     * Tests getReturnedParameters with void return type method.
     * Even though method returns void, parameter tracking should still work.
     */
    @Test
    public void testGetReturnedParameters_withVoidReturnType_tracksCorrectly() {
        // Arrange - create method with void return type
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 0 (even though method returns void, tracking still works)
        info.setParameterReturned(0);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - tracking should work even with void return type
        assertEquals(1L, returnedParameters,
                "Should return 1L even with void return type");
    }

    /**
     * Tests getReturnedParameters with primitive return type method.
     * Parameter tracking should work regardless of return type.
     */
    @Test
    public void testGetReturnedParameters_withPrimitiveReturnType_tracksCorrectly() {
        // Arrange - create method with int return type
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)I");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 0
        info.setParameterReturned(0);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - tracking should work with primitive return type
        assertEquals(1L, returnedParameters,
                "Should return 1L with primitive return type");
    }

    /**
     * Tests getReturnedParameters with sparse parameter marking.
     * Tests non-contiguous bit patterns.
     */
    @Test
    public void testGetReturnedParameters_withSparseMarking_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark sparse parameters: 1, 5, 12
        info.setParameterReturned(1);
        info.setParameterReturned(5);
        info.setParameterReturned(12);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should have bits 1, 5, and 12 set
        long expected = (1L << 1) | (1L << 5) | (1L << 12);
        assertEquals(expected, returnedParameters,
                "Should return correct bitmask with sparse bits set");
        assertEquals(4130L, returnedParameters,
                "Should return 4130L with parameters 1, 5, and 12 returned");
    }

    /**
     * Tests getReturnedParameters after multiple cumulative updates.
     * Verifies OR operation accumulates all bits correctly.
     */
    @Test
    public void testGetReturnedParameters_withSequentialUpdates_accumulatesProperly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Long;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Sequential updates
        info.updateReturnedParameters(1L);      // Parameter 0
        assertEquals(1L, ParameterEscapeMarker.getReturnedParameters(method),
                "After first update should have bit 0");

        info.updateReturnedParameters(4L);      // Parameter 2
        assertEquals(5L, ParameterEscapeMarker.getReturnedParameters(method),
                "After second update should have bits 0 and 2");

        info.updateReturnedParameters(10L);     // Parameters 1 and 3
        assertEquals(15L, ParameterEscapeMarker.getReturnedParameters(method),
                "After third update should have bits 0, 1, 2, and 3");
    }

    /**
     * Tests getReturnedParameters with a large bitmask value.
     * Tests that large bitmask values are handled correctly.
     */
    @Test
    public void testGetReturnedParameters_withLargeBitmask_handlesCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Create large bitmask: parameters 20, 30, 40, 50
        long largeMask = (1L << 20) | (1L << 30) | (1L << 40) | (1L << 50);
        info.updateReturnedParameters(largeMask);

        // Act
        long returnedParameters = ParameterEscapeMarker.getReturnedParameters(method);

        // Assert - should return the large bitmask
        assertEquals(largeMask, returnedParameters,
                "Should correctly handle large bitmask values");
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

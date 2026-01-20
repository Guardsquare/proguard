package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParameterEscapeMarker#getModifiedParameters(Method)}.
 *
 * The getModifiedParameters static method returns a bitmask of parameters whose contents
 * are modified by the method. It delegates to
 * MethodOptimizationInfo.getMethodOptimizationInfo(method).getModifiedParameters().
 *
 * A parameter is "modified" when its internal state is changed (e.g., fields are written,
 * array elements are modified, or methods that change state are called on it).
 *
 * The return value is a long bitmask where each bit represents a parameter:
 * - Bit 0 corresponds to parameter 0
 * - Bit 1 corresponds to parameter 1
 * - etc.
 *
 * The logic involves side effect flags:
 * - hasNoSideEffects: if true, returns 0L (no parameters modified)
 * - hasNoExternalSideEffects: if true, returns bitmask & 1L (only parameter 0 can be modified)
 * - Otherwise: returns actual bitmask
 *
 * Default behavior (MethodOptimizationInfo):
 * - Returns 0L if hasNoSideEffects
 * - Returns 1L if hasNoExternalSideEffects
 * - Returns -1L otherwise (conservative: all parameters might be modified)
 *
 * With ProgramMethodOptimizationInfo:
 * - Returns 0L if hasNoSideEffects
 * - Returns modifiedParameters & 1L if hasNoExternalSideEffects
 * - Returns modifiedParameters otherwise (actual bitmask)
 */
public class ParameterEscapeMarkerClaude_getModifiedParametersTest {

    private ProgramClass testClass;

    @BeforeEach
    public void setUp() {
        testClass = createProgramClassWithConstantPool();
    }

    /**
     * Tests getModifiedParameters returns -1L by default with MethodOptimizationInfo.
     * -1L means all bits are set, indicating all parameters might be modified conservatively.
     */
    @Test
    public void testGetModifiedParameters_withDefaultMethodOptimizationInfo_returnsNegativeOne() {
        // Arrange - create method with default MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, method);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should return -1L (all bits set)
        assertEquals(-1L, modifiedParameters,
                "Should return -1L by default (conservative: all parameters might be modified)");
    }

    /**
     * Tests getModifiedParameters returns 0L after setNoSideEffects with MethodOptimizationInfo.
     * If no side effects, no parameters are modified.
     */
    @Test
    public void testGetModifiedParameters_withMethodOptimizationInfoNoSideEffects_returnsZero() {
        // Arrange - create method with MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        method.setProcessingInfo(info);
        info.setNoSideEffects();

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should return 0L (no parameters modified)
        assertEquals(0L, modifiedParameters,
                "Should return 0L after setNoSideEffects");
    }

    /**
     * Tests getModifiedParameters returns 1L after setNoExternalSideEffects with MethodOptimizationInfo.
     * Only parameter 0 ('this') can be modified if no external side effects.
     */
    @Test
    public void testGetModifiedParameters_withMethodOptimizationInfoNoExternalSideEffects_returnsOne() {
        // Arrange - create method with MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        method.setProcessingInfo(info);
        info.setNoExternalSideEffects();

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should return 1L (only bit 0 set)
        assertEquals(1L, modifiedParameters,
                "Should return 1L after setNoExternalSideEffects (only parameter 0)");
    }

    /**
     * Tests getModifiedParameters with ProgramMethodOptimizationInfo initially returns 0L.
     */
    @Test
    public void testGetModifiedParameters_withProgramMethodOptimizationInfo_initiallyReturnsZero() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should return 0L initially (no parameters marked as modified)
        assertEquals(0L, modifiedParameters,
                "Should return 0L initially with ProgramMethodOptimizationInfo");
    }

    /**
     * Tests getModifiedParameters after marking a single parameter as modified.
     */
    @Test
    public void testGetModifiedParameters_withSingleParameterMarked_returnsCorrectBitmask() {
        // Arrange - create method and mark parameter 0
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterModified(0);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should have bit 0 set
        assertEquals(1L, modifiedParameters,
                "Should return 1L (bit 0 set) when parameter 0 is modified");
    }

    /**
     * Tests getModifiedParameters after marking parameter 1 as modified.
     */
    @Test
    public void testGetModifiedParameters_withParameterOne_returnsCorrectBitmask() {
        // Arrange - create method and mark parameter 1
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterModified(1);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should have bit 1 set (value = 2)
        assertEquals(2L, modifiedParameters,
                "Should return 2L (bit 1 set) when parameter 1 is modified");
    }

    /**
     * Tests getModifiedParameters with multiple parameters marked.
     */
    @Test
    public void testGetModifiedParameters_withMultipleParameters_returnsCorrectBitmask() {
        // Arrange - create method and mark parameters 0 and 2
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterModified(0);
        info.setParameterModified(2);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should have bits 0 and 2 set (value = 5 = 0b101)
        assertEquals(5L, modifiedParameters,
                "Should return 5L (bits 0 and 2 set) when parameters 0 and 2 are modified");
    }

    /**
     * Tests getModifiedParameters with all parameters marked.
     */
    @Test
    public void testGetModifiedParameters_withAllParametersMarked_returnsCorrectBitmask() {
        // Arrange - create method and mark all 3 parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterModified(0);
        info.setParameterModified(1);
        info.setParameterModified(2);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should have bits 0, 1, and 2 set (value = 7 = 0b111)
        assertEquals(7L, modifiedParameters,
                "Should return 7L (bits 0, 1, 2 set) when all 3 parameters are modified");
    }

    /**
     * Tests getModifiedParameters returns 0L after setNoSideEffects with ProgramMethodOptimizationInfo.
     * hasNoSideEffects overrides the bitmask.
     */
    @Test
    public void testGetModifiedParameters_afterSetNoSideEffects_returnsZero() {
        // Arrange - create method and mark parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterModified(0);
        info.setParameterModified(1);

        // Verify parameters are marked
        assertEquals(3L, ParameterEscapeMarker.getModifiedParameters(method),
                "Should be 3L before setNoSideEffects");

        // Set no side effects
        info.setNoSideEffects();

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should return 0L (hasNoSideEffects overrides)
        assertEquals(0L, modifiedParameters,
                "Should return 0L after setNoSideEffects (overrides bitmask)");
    }

    /**
     * Tests getModifiedParameters masks to parameter 0 after setNoExternalSideEffects.
     * Only parameter 0 ('this') can be modified if no external side effects.
     */
    @Test
    public void testGetModifiedParameters_afterSetNoExternalSideEffects_masksToParameterZero() {
        // Arrange - create method and mark parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterModified(0);
        info.setParameterModified(1);

        // Verify both parameters are marked
        assertEquals(3L, ParameterEscapeMarker.getModifiedParameters(method),
                "Should be 3L before setNoExternalSideEffects");

        // Set no external side effects
        info.setNoExternalSideEffects();

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should return only bit 0 (masked to 1L)
        assertEquals(1L, modifiedParameters,
                "Should return 1L after setNoExternalSideEffects (masks to parameter 0)");
    }

    /**
     * Tests getModifiedParameters with setNoExternalSideEffects when only parameter 1 is marked.
     * Should return 0L since parameter 1 is masked out.
     */
    @Test
    public void testGetModifiedParameters_noExternalSideEffectsWithoutParameter0_returnsZero() {
        // Arrange - create method and mark only parameter 1
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterModified(1);

        // Set no external side effects
        info.setNoExternalSideEffects();

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should return 0L (parameter 1 is masked out)
        assertEquals(0L, modifiedParameters,
                "Should return 0L (parameter 1 masked out by noExternalSideEffects)");
    }

    /**
     * Tests getModifiedParameters using updateModifiedParameters.
     */
    @Test
    public void testGetModifiedParameters_afterUpdateModifiedParameters_returnsCorrectBitmask() {
        // Arrange - create method and update with bitmask
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        long mask = (1L << 0) | (1L << 1);  // bits 0 and 1 set
        info.updateModifiedParameters(mask);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should return the same mask
        assertEquals(3L, modifiedParameters,
                "Should return 3L (bits 0 and 1 set) after updateModifiedParameters");
    }

    /**
     * Tests getModifiedParameters with higher bit positions.
     */
    @Test
    public void testGetModifiedParameters_withHigherBits_returnsCorrectBitmask() {
        // Arrange - create method with many parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/Double;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 4
        info.setParameterModified(4);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should have bit 4 set (value = 16 = 2^4)
        assertEquals(16L, modifiedParameters,
                "Should return 16L (bit 4 set) when parameter 4 is modified");
    }

    /**
     * Tests getModifiedParameters with very high bit positions.
     */
    @Test
    public void testGetModifiedParameters_withVeryHighBits_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 10
        info.setParameterModified(10);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should have bit 10 set (value = 1024 = 2^10)
        assertEquals(1024L, modifiedParameters,
                "Should return 1024L (bit 10 set) when parameter 10 is modified");
    }

    /**
     * Tests getModifiedParameters with combined high and low bits.
     */
    @Test
    public void testGetModifiedParameters_withCombinedHighAndLowBits_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters 0, 3, and 8
        info.setParameterModified(0);
        info.setParameterModified(3);
        info.setParameterModified(8);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should have bits 0, 3, and 8 set (value = 1 + 8 + 256 = 265)
        long expected = (1L << 0) | (1L << 3) | (1L << 8);
        assertEquals(expected, modifiedParameters,
                "Should return correct bitmask with bits 0, 3, and 8 set");
        assertEquals(265L, modifiedParameters,
                "Should return 265L when parameters 0, 3, and 8 are modified");
    }

    /**
     * Tests getModifiedParameters is consistent across multiple calls.
     */
    @Test
    public void testGetModifiedParameters_multipleCalls_returnsConsistentResults() {
        // Arrange - create method and mark parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterModified(1);

        // Act - call multiple times
        long firstCall = ParameterEscapeMarker.getModifiedParameters(method);
        long secondCall = ParameterEscapeMarker.getModifiedParameters(method);
        long thirdCall = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - all calls should return the same value
        assertEquals(2L, firstCall, "First call should return 2L");
        assertEquals(2L, secondCall, "Second call should return 2L");
        assertEquals(2L, thirdCall, "Third call should return 2L");
        assertEquals(firstCall, secondCall, "First and second calls should match");
        assertEquals(secondCall, thirdCall, "Second and third calls should match");
    }

    /**
     * Tests getModifiedParameters with different methods independently.
     */
    @Test
    public void testGetModifiedParameters_withDifferentMethods_worksIndependently() {
        // Arrange - create two methods with different modified parameters
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);
        ProgramMethodOptimizationInfo info1 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method1);
        info1.setParameterModified(0);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);
        ProgramMethodOptimizationInfo info2 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method2);
        info2.setParameterModified(1);

        // Act
        long method1Mask = ParameterEscapeMarker.getModifiedParameters(method1);
        long method2Mask = ParameterEscapeMarker.getModifiedParameters(method2);

        // Assert - methods should have different masks
        assertEquals(1L, method1Mask, "Method1 should return 1L (bit 0 set)");
        assertEquals(2L, method2Mask, "Method2 should return 2L (bit 1 set)");
        assertNotEquals(method1Mask, method2Mask, "Different methods should have different masks");
    }

    /**
     * Tests getModifiedParameters reflects incremental updates.
     */
    @Test
    public void testGetModifiedParameters_withIncrementalUpdates_reflectsChanges() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act & Assert - check after each parameter is marked
        assertEquals(0L, ParameterEscapeMarker.getModifiedParameters(method),
                "Initially should return 0L");

        info.setParameterModified(0);
        assertEquals(1L, ParameterEscapeMarker.getModifiedParameters(method),
                "After marking parameter 0, should return 1L");

        info.setParameterModified(1);
        assertEquals(3L, ParameterEscapeMarker.getModifiedParameters(method),
                "After marking parameters 0 and 1, should return 3L");

        info.setParameterModified(2);
        assertEquals(7L, ParameterEscapeMarker.getModifiedParameters(method),
                "After marking all 3 parameters, should return 7L");
    }

    /**
     * Tests getModifiedParameters with static method.
     */
    @Test
    public void testGetModifiedParameters_withStaticMethod_worksCorrectly() {
        // Arrange - create static method
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "staticMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterModified(0);
        info.setParameterModified(1);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should have bits 0 and 1 set
        assertEquals(3L, modifiedParameters,
                "Static method should return 3L with parameters 0 and 1 modified");
    }

    /**
     * Tests getModifiedParameters can represent up to 64 parameters.
     */
    @Test
    public void testGetModifiedParameters_withHighParameterIndex_handlesUpTo63() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter at index 63 (highest bit in long)
        info.setParameterModified(63);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should have bit 63 set
        long expected = 1L << 63;
        assertEquals(expected, modifiedParameters,
                "Should correctly set bit 63 (highest bit in long)");
        assertTrue(modifiedParameters < 0, "Bit 63 set means the long is negative");
    }

    /**
     * Tests getModifiedParameters with alternating bit pattern.
     */
    @Test
    public void testGetModifiedParameters_withAlternatingPattern_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters 0, 2, 4, 6 (alternating pattern)
        info.setParameterModified(0);
        info.setParameterModified(2);
        info.setParameterModified(4);
        info.setParameterModified(6);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should have alternating bits set (0b01010101 = 85)
        long expected = (1L << 0) | (1L << 2) | (1L << 4) | (1L << 6);
        assertEquals(expected, modifiedParameters,
                "Should return correct bitmask with alternating bits");
        assertEquals(85L, modifiedParameters,
                "Should return 85L with parameters 0, 2, 4, 6 modified");
    }

    /**
     * Tests getModifiedParameters delegates correctly to MethodOptimizationInfo.
     */
    @Test
    public void testGetModifiedParameters_delegatesToMethodOptimizationInfo() {
        // Arrange - create method with custom MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo customInfo = new MethodOptimizationInfo();
        method.setProcessingInfo(customInfo);

        // Act
        long result = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should delegate to info's getModifiedParameters
        // Default MethodOptimizationInfo returns -1L
        assertEquals(-1L, result, "Should delegate to MethodOptimizationInfo.getModifiedParameters");
    }

    /**
     * Tests getModifiedParameters with multiple cumulative updates.
     */
    @Test
    public void testGetModifiedParameters_withMultipleUpdates_accumulatesCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - update with different masks
        info.updateModifiedParameters(1L << 0);  // Set bit 0
        info.updateModifiedParameters(1L << 2);  // Set bit 2
        info.updateModifiedParameters(1L << 5);  // Set bit 5

        // Assert - all bits should be set (cumulative)
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);
        long expected = (1L << 0) | (1L << 2) | (1L << 5);
        assertEquals(expected, modifiedParameters,
                "Multiple updates should accumulate (OR operation)");
        assertEquals(37L, modifiedParameters, "Should return 37L (bits 0, 2, 5 set)");
    }

    /**
     * Tests getModifiedParameters after marking same parameter twice (idempotent).
     */
    @Test
    public void testGetModifiedParameters_markingSameParameterTwice_isIdempotent() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Act - mark parameter 0 twice
        info.setParameterModified(0);
        long afterFirst = ParameterEscapeMarker.getModifiedParameters(method);

        info.setParameterModified(0);
        long afterSecond = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - both should return the same value
        assertEquals(1L, afterFirst, "After first mark should return 1L");
        assertEquals(1L, afterSecond, "After second mark should still return 1L");
        assertEquals(afterFirst, afterSecond, "Marking same parameter twice should be idempotent");
    }

    /**
     * Tests getModifiedParameters with method having no parameters.
     */
    @Test
    public void testGetModifiedParameters_withNoParameters_returnsZero() {
        // Arrange - create method with no parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should return 0L (no parameters to modify)
        assertEquals(0L, modifiedParameters,
                "Method with no parameters should return 0L");
    }

    /**
     * Tests getModifiedParameters precedence: hasNoSideEffects > hasNoExternalSideEffects > bitmask.
     */
    @Test
    public void testGetModifiedParameters_flagPrecedence_noSideEffectsWins() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters
        info.setParameterModified(0);
        info.setParameterModified(1);
        assertEquals(3L, ParameterEscapeMarker.getModifiedParameters(method),
                "Should be 3L initially");

        // Set hasNoExternalSideEffects (masks to parameter 0)
        info.setNoExternalSideEffects();
        assertEquals(1L, ParameterEscapeMarker.getModifiedParameters(method),
                "Should be 1L with noExternalSideEffects");

        // Set hasNoSideEffects (highest priority, returns 0)
        info.setNoSideEffects();
        assertEquals(0L, ParameterEscapeMarker.getModifiedParameters(method),
                "Should be 0L with noSideEffects (highest priority)");
    }

    /**
     * Tests getModifiedParameters with sparse parameter marking.
     */
    @Test
    public void testGetModifiedParameters_withSparseMarking_returnsCorrectBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark sparse parameters: 1, 5, 12
        info.setParameterModified(1);
        info.setParameterModified(5);
        info.setParameterModified(12);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should have bits 1, 5, and 12 set
        long expected = (1L << 1) | (1L << 5) | (1L << 12);
        assertEquals(expected, modifiedParameters,
                "Should return correct bitmask with sparse bits set");
        assertEquals(4130L, modifiedParameters,
                "Should return 4130L with parameters 1, 5, and 12 modified");
    }

    /**
     * Tests getModifiedParameters with sequential cumulative updates.
     */
    @Test
    public void testGetModifiedParameters_withSequentialUpdates_accumulatesProperly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Long;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Sequential updates
        info.updateModifiedParameters(1L);      // Parameter 0
        assertEquals(1L, ParameterEscapeMarker.getModifiedParameters(method),
                "After first update should have bit 0");

        info.updateModifiedParameters(4L);      // Parameter 2
        assertEquals(5L, ParameterEscapeMarker.getModifiedParameters(method),
                "After second update should have bits 0 and 2");

        info.updateModifiedParameters(10L);     // Parameters 1 and 3
        assertEquals(15L, ParameterEscapeMarker.getModifiedParameters(method),
                "After third update should have bits 0, 1, 2, and 3");
    }

    /**
     * Tests getModifiedParameters with large bitmask value.
     */
    @Test
    public void testGetModifiedParameters_withLargeBitmask_handlesCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Create large bitmask: parameters 20, 30, 40, 50
        long largeMask = (1L << 20) | (1L << 30) | (1L << 40) | (1L << 50);
        info.updateModifiedParameters(largeMask);

        // Act
        long modifiedParameters = ParameterEscapeMarker.getModifiedParameters(method);

        // Assert - should return the large bitmask
        assertEquals(largeMask, modifiedParameters,
                "Should correctly handle large bitmask values");
    }

    /**
     * Tests getModifiedParameters comparing MethodOptimizationInfo vs ProgramMethodOptimizationInfo.
     */
    @Test
    public void testGetModifiedParameters_comparesDefaultVsProgram() {
        // Arrange - create two methods with different info types
        ProgramMethod methodDefault = createMethodWithDescriptor(testClass, "methodDefault", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, methodDefault);

        ProgramMethod methodProgram = createMethodWithDescriptor(testClass, "methodProgram", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, methodProgram);

        // Act
        long defaultResult = ParameterEscapeMarker.getModifiedParameters(methodDefault);
        long programResult = ParameterEscapeMarker.getModifiedParameters(methodProgram);

        // Assert - default is conservative (-1L), program is precise (0L initially)
        assertEquals(-1L, defaultResult, "Default MethodOptimizationInfo should return -1L (conservative)");
        assertEquals(0L, programResult, "ProgramMethodOptimizationInfo should return 0L initially (precise)");
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

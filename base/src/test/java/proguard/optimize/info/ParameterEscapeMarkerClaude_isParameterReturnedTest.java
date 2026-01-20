package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParameterEscapeMarker#isParameterReturned(Method, int)}.
 *
 * The isParameterReturned static method checks whether a specific parameter is returned
 * from a method. It delegates to MethodOptimizationInfo.getMethodOptimizationInfo(method).returnsParameter(parameterIndex).
 *
 * A parameter is "returned" when the method returns that exact parameter reference.
 * For example, in a method like "Object identity(Object x) { return x; }", parameter 0 is returned.
 *
 * Default behavior (MethodOptimizationInfo): Returns true (conservative assumption).
 * With ProgramMethodOptimizationInfo: Returns whether the specific parameter bit is set.
 */
public class ParameterEscapeMarkerClaude_isParameterReturnedTest {

    private ProgramClass testClass;

    @BeforeEach
    public void setUp() {
        testClass = createProgramClassWithConstantPool();
    }

    /**
     * Tests isParameterReturned returns true by default with MethodOptimizationInfo.
     * Without detailed analysis, the method conservatively assumes parameters might be returned.
     */
    @Test
    public void testIsParameterReturned_withDefaultMethodOptimizationInfo_returnsTrue() {
        // Arrange - create method with default MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, method);

        // Act & Assert - should return true by default (conservative assumption)
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should be returned by default");
    }

    /**
     * Tests isParameterReturned with multiple parameters, all return true by default.
     */
    @Test
    public void testIsParameterReturned_withMultipleParametersDefault_allReturnTrue() {
        // Arrange - create method with multiple parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, method);

        // Act & Assert - all should return true by default
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should be returned by default");
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 1),
                "Parameter 1 should be returned by default");
    }

    /**
     * Tests isParameterReturned with ProgramMethodOptimizationInfo and no parameters marked.
     * Should return false initially (no parameters marked as returned).
     */
    @Test
    public void testIsParameterReturned_withProgramMethodOptimizationInfo_initiallyReturnsFalse() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act & Assert - should return false initially (no parameters marked as returned)
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should not be returned initially with ProgramMethodOptimizationInfo");
    }

    /**
     * Tests isParameterReturned after marking a parameter as returned.
     * Only the marked parameter should return true.
     */
    @Test
    public void testIsParameterReturned_withParameterMarked_returnsTrue() {
        // Arrange - create method and mark parameter 0 as returned
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterReturned(0);

        // Act & Assert - parameter 0 should now be returned
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should be returned after marking");
    }

    /**
     * Tests isParameterReturned with specific parameter marked among multiple.
     * Only the marked parameter should return true.
     */
    @Test
    public void testIsParameterReturned_withSpecificParameterMarked_returnsCorrectly() {
        // Arrange - create method with multiple parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark only parameter 1 as returned
        info.setParameterReturned(1);

        // Act & Assert
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should not be returned");
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 1),
                "Parameter 1 should be returned");
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 2),
                "Parameter 2 should not be returned");
    }

    /**
     * Tests isParameterReturned with multiple parameters marked.
     * All marked parameters should return true.
     */
    @Test
    public void testIsParameterReturned_withMultipleParametersMarked_returnsCorrectly() {
        // Arrange - create method with multiple parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters 0 and 2 as returned
        info.setParameterReturned(0);
        info.setParameterReturned(2);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should be returned");
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 1),
                "Parameter 1 should not be returned");
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 2),
                "Parameter 2 should be returned");
    }

    /**
     * Tests isParameterReturned with parameter index 0 (first parameter or 'this' for non-static).
     */
    @Test
    public void testIsParameterReturned_withParameterIndexZero_worksCorrectly() {
        // Arrange - create non-static method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially, parameter 0 should not be returned
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should not be returned initially");

        // Mark parameter 0 as returned
        info.setParameterReturned(0);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should be returned after marking");
    }

    /**
     * Tests isParameterReturned with large parameter indices.
     * Tests that the bit masking works correctly for high parameter indices.
     */
    @Test
    public void testIsParameterReturned_withLargeParameterIndex_worksCorrectly() {
        // Arrange - create method with many parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/Double;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter at higher index
        info.setParameterReturned(4);

        // Act & Assert
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should not be returned");
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 3),
                "Parameter 3 should not be returned");
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 4),
                "Parameter 4 should be returned");
    }

    /**
     * Tests isParameterReturned with a static method.
     * Static methods don't have 'this' parameter, so parameter 0 is the first declared parameter.
     */
    @Test
    public void testIsParameterReturned_withStaticMethod_worksCorrectly() {
        // Arrange - create static method
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "staticMethod",
                "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 0 as returned
        info.setParameterReturned(0);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 of static method should be returned");
    }

    /**
     * Tests isParameterReturned consistency across multiple calls.
     * The result should be consistent and not change between calls.
     */
    @Test
    public void testIsParameterReturned_multipleCalls_returnsConsistentResults() {
        // Arrange - create method and mark parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterReturned(0);

        // Act - call multiple times
        boolean firstCall = ParameterEscapeMarker.isParameterReturned(method, 0);
        boolean secondCall = ParameterEscapeMarker.isParameterReturned(method, 0);
        boolean thirdCall = ParameterEscapeMarker.isParameterReturned(method, 0);

        // Assert - all calls should return the same result
        assertTrue(firstCall, "First call should return true");
        assertTrue(secondCall, "Second call should return true");
        assertTrue(thirdCall, "Third call should return true");
        assertEquals(firstCall, secondCall, "First and second calls should match");
        assertEquals(secondCall, thirdCall, "Second and third calls should match");
    }

    /**
     * Tests isParameterReturned with different methods independently.
     * Each method should track its own returned parameters independently.
     */
    @Test
    public void testIsParameterReturned_withDifferentMethods_worksIndependently() {
        // Arrange - create two different methods
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);
        ProgramMethodOptimizationInfo info1 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method1);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);

        // Mark only method1's parameter as returned
        info1.setParameterReturned(0);

        // Act & Assert - method1's parameter should be returned, method2's should not
        assertTrue(ParameterEscapeMarker.isParameterReturned(method1, 0),
                "Method1's parameter 0 should be returned");
        assertFalse(ParameterEscapeMarker.isParameterReturned(method2, 0),
                "Method2's parameter 0 should not be returned");
    }

    /**
     * Tests isParameterReturned after updating returned parameters mask.
     * Using updateReturnedParameters should properly update the bits.
     */
    @Test
    public void testIsParameterReturned_afterUpdateReturnedParameters_returnsCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Update with bitmask marking parameters 0 and 1
        long mask = (1L << 0) | (1L << 1);
        info.updateReturnedParameters(mask);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should be returned after update");
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 1),
                "Parameter 1 should be returned after update");
    }

    /**
     * Tests isParameterReturned returns false for parameters not in the mask.
     */
    @Test
    public void testIsParameterReturned_withParametersNotInMask_returnsFalse() {
        // Arrange - create method with 5 parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/Double;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark only parameter 2
        info.setParameterReturned(2);

        // Act & Assert - unmarked parameters should return false
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should not be returned");
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 1),
                "Parameter 1 should not be returned");
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 2),
                "Parameter 2 should be returned");
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 3),
                "Parameter 3 should not be returned");
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 4),
                "Parameter 4 should not be returned");
    }

    /**
     * Tests isParameterReturned with method that has no parameters (aside from 'this').
     * Checking parameter 0 ('this') should work correctly.
     */
    @Test
    public void testIsParameterReturned_withNoExplicitParameters_worksForThis() {
        // Arrange - create method with no explicit parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 0 ('this' for non-static methods)
        info.setParameterReturned(0);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 ('this') should be returned");
    }

    /**
     * Tests isParameterReturned preserves state after marking.
     * Once set, the parameter should remain returned.
     */
    @Test
    public void testIsParameterReturned_preservesStateAfterMarking_remainsSet() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initial state
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should not be returned initially");

        // Mark as returned
        info.setParameterReturned(0);
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should be returned after marking");

        // Mark again (should remain set)
        info.setParameterReturned(0);
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should still be returned after marking again");
    }

    /**
     * Tests isParameterReturned correctly interprets bit positions.
     * Each parameter index corresponds to a bit in the bitmask.
     */
    @Test
    public void testIsParameterReturned_correctBitPositionInterpretation() {
        // Arrange - create method with parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters at specific bit positions
        info.setParameterReturned(0);  // bit 0
        info.setParameterReturned(2);  // bit 2

        // Act & Assert - verify correct bit interpretation
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 (bit 0) should be returned");
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 1),
                "Parameter 1 (bit 1) should not be returned");
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 2),
                "Parameter 2 (bit 2) should be returned");
    }

    /**
     * Tests isParameterReturned delegates correctly to MethodOptimizationInfo.
     * This verifies the static method properly wraps the instance method.
     */
    @Test
    public void testIsParameterReturned_delegatesToMethodOptimizationInfo() {
        // Arrange - create method with custom MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        MethodOptimizationInfo customInfo = new MethodOptimizationInfo();
        method.setProcessingInfo(customInfo);

        // Act - call static method
        boolean result = ParameterEscapeMarker.isParameterReturned(method, 0);

        // Assert - should delegate to info's returnsParameter
        // Default MethodOptimizationInfo returns true
        assertTrue(result, "Should delegate to MethodOptimizationInfo.returnsParameter");
    }

    /**
     * Tests isParameterReturned with void return type method.
     * Even though the method returns void, parameter tracking should still work.
     */
    @Test
    public void testIsParameterReturned_withVoidReturnType_tracksCorrectly() {
        // Arrange - create method with void return type
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially should be false
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should not be returned initially");

        // Mark parameter (even though method returns void, tracking still works)
        info.setParameterReturned(0);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 tracking should work even with void return type");
    }

    /**
     * Tests isParameterReturned with primitive return type method.
     * Parameter tracking should work regardless of return type.
     */
    @Test
    public void testIsParameterReturned_withPrimitiveReturnType_tracksCorrectly() {
        // Arrange - create method with int return type
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)I");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter
        info.setParameterReturned(0);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 tracking should work with primitive return type");
    }

    /**
     * Tests isParameterReturned with mixed parameter types in descriptor.
     * The method checks index only, not the actual type.
     */
    @Test
    public void testIsParameterReturned_withMixedParameterTypes_checksIndexOnly() {
        // Arrange - create method with mixed parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;ILjava/lang/String;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters regardless of type
        info.setParameterReturned(0);  // Object
        info.setParameterReturned(1);  // int (primitive)
        info.setParameterReturned(2);  // String

        // Act & Assert - method checks index only, not type
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 (Object) should be returned");
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 1),
                "Parameter 1 (int) should be returned");
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 2),
                "Parameter 2 (String) should be returned");
    }

    /**
     * Tests isParameterReturned with incremental marking.
     * Parameters marked incrementally should all be tracked.
     */
    @Test
    public void testIsParameterReturned_withIncrementalMarking_tracksAll() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark incrementally and check after each
        info.setParameterReturned(0);
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should be returned after marking");
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 1),
                "Parameter 1 should not be returned yet");

        info.setParameterReturned(2);
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should still be returned");
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 1),
                "Parameter 1 should still not be returned");
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 2),
                "Parameter 2 should be returned after marking");
    }

    /**
     * Tests isParameterReturned with higher parameter index values.
     * Tests parameters beyond typical small indices.
     */
    @Test
    public void testIsParameterReturned_withHighParameterIndices_worksCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters at higher indices
        info.setParameterReturned(10);
        info.setParameterReturned(15);

        // Act & Assert
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should not be returned");
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 10),
                "Parameter 10 should be returned");
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 15),
                "Parameter 15 should be returned");
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 20),
                "Parameter 20 should not be returned");
    }

    /**
     * Tests isParameterReturned with cumulative updates via updateReturnedParameters.
     * Multiple updates should accumulate (OR operation).
     */
    @Test
    public void testIsParameterReturned_withCumulativeUpdates_accumulatesCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Update with different masks
        info.updateReturnedParameters(1L << 0);  // Mark parameter 0
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should be returned after first update");

        info.updateReturnedParameters(1L << 2);  // Mark parameter 2
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 0),
                "Parameter 0 should still be returned");
        assertTrue(ParameterEscapeMarker.isParameterReturned(method, 2),
                "Parameter 2 should be returned after second update");
        assertFalse(ParameterEscapeMarker.isParameterReturned(method, 1),
                "Parameter 1 should not be returned");
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

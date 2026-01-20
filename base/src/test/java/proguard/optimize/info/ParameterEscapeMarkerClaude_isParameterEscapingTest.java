package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParameterEscapeMarker#isParameterEscaping(Method, int)}.
 *
 * The isParameterEscaping static method is a utility method that checks whether a specific
 * parameter escapes from a method. It delegates to MethodOptimizationInfo.getMethodOptimizationInfo(method).isParameterEscaping(parameterIndex).
 *
 * A parameter "escapes" when it becomes reachable on the heap after the method exits.
 * For example, parameters passed to System.setProperty or Set.add escape because they
 * become reachable through static fields or collections.
 *
 * This method is typically used after ParameterEscapeMarker has analyzed the method's code
 * to determine which parameters escape.
 */
public class ParameterEscapeMarkerClaude_isParameterEscapingTest {

    private ProgramClass testClass;

    @BeforeEach
    public void setUp() {
        testClass = createProgramClassWithConstantPool();
    }

    /**
     * Tests isParameterEscaping returns true by default when no optimization info is set.
     * Without optimization info attached, the method should return the conservative default (true).
     */
    @Test
    public void testIsParameterEscaping_withDefaultMethodOptimizationInfo_returnsTrue() {
        // Arrange - create method with default MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, method);

        // Act & Assert - should return true by default (conservative assumption)
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 should be escaping by default");
    }

    /**
     * Tests isParameterEscaping returns false after marking no escaping parameters.
     */
    @Test
    public void testIsParameterEscaping_afterSetNoEscapingParameters_returnsFalse() {
        // Arrange - create method with MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        info.setNoEscapingParameters();
        method.setProcessingInfo(info);

        // Act & Assert - should return false after setNoEscapingParameters
        assertFalse(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 should not be escaping after setNoEscapingParameters");
    }

    /**
     * Tests isParameterEscaping with ProgramMethodOptimizationInfo and specific parameter marked.
     * Only the marked parameter should escape.
     */
    @Test
    public void testIsParameterEscaping_withSpecificParameterMarked_returnsCorrectly() {
        // Arrange - create method with multiple parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;Ljava/lang/String;I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 1 as escaping
        info.setParameterEscaping(1);

        // Act & Assert
        assertFalse(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 should not be escaping");
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 1),
                "Parameter 1 should be escaping");
        assertFalse(ParameterEscapeMarker.isParameterEscaping(method, 2),
                "Parameter 2 should not be escaping");
    }

    /**
     * Tests isParameterEscaping with multiple parameters marked as escaping.
     */
    @Test
    public void testIsParameterEscaping_withMultipleParametersMarked_returnsCorrectly() {
        // Arrange - create method with multiple parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;Ljava/lang/String;Ljava/util/List;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters 0 and 2 as escaping
        info.setParameterEscaping(0);
        info.setParameterEscaping(2);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 should be escaping");
        assertFalse(ParameterEscapeMarker.isParameterEscaping(method, 1),
                "Parameter 1 should not be escaping");
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 2),
                "Parameter 2 should be escaping");
    }

    /**
     * Tests isParameterEscaping with parameter index 0 (first parameter or 'this' for non-static).
     */
    @Test
    public void testIsParameterEscaping_withParameterIndexZero_worksCorrectly() {
        // Arrange - create non-static method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially, parameter 0 should not be escaping
        assertFalse(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 should not be escaping initially");

        // Mark parameter 0 as escaping
        info.setParameterEscaping(0);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 should be escaping after marking");
    }

    /**
     * Tests isParameterEscaping with large parameter indices.
     * Tests that the bit masking works correctly for high parameter indices.
     */
    @Test
    public void testIsParameterEscaping_withLargeParameterIndex_worksCorrectly() {
        // Arrange - create method with many parameters (use descriptor with many parameters)
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
            "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/Double;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter at higher index
        info.setParameterEscaping(4);

        // Act & Assert
        assertFalse(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 should not be escaping");
        assertFalse(ParameterEscapeMarker.isParameterEscaping(method, 3),
                "Parameter 3 should not be escaping");
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 4),
                "Parameter 4 should be escaping");
    }

    /**
     * Tests isParameterEscaping with a static method.
     * Static methods don't have 'this' parameter, so parameter 0 is the first declared parameter.
     */
    @Test
    public void testIsParameterEscaping_withStaticMethod_worksCorrectly() {
        // Arrange - create static method
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "staticMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 0 as escaping
        info.setParameterEscaping(0);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 of static method should be escaping");
    }

    /**
     * Tests isParameterEscaping consistency across multiple calls.
     * The result should be consistent and not change between calls.
     */
    @Test
    public void testIsParameterEscaping_multipleCalls_returnsConsistentResults() {
        // Arrange - create method and mark parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterEscaping(0);

        // Act - call multiple times
        boolean firstCall = ParameterEscapeMarker.isParameterEscaping(method, 0);
        boolean secondCall = ParameterEscapeMarker.isParameterEscaping(method, 0);
        boolean thirdCall = ParameterEscapeMarker.isParameterEscaping(method, 0);

        // Assert - all calls should return the same result
        assertTrue(firstCall, "First call should return true");
        assertTrue(secondCall, "Second call should return true");
        assertTrue(thirdCall, "Third call should return true");
        assertEquals(firstCall, secondCall, "First and second calls should match");
        assertEquals(secondCall, thirdCall, "Second and third calls should match");
    }

    /**
     * Tests isParameterEscaping with different methods independently.
     * Each method should track its own escaping parameters independently.
     */
    @Test
    public void testIsParameterEscaping_withDifferentMethods_worksIndependently() {
        // Arrange - create two different methods
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);
        ProgramMethodOptimizationInfo info1 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method1);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);

        // Mark only method1's parameter as escaping
        info1.setParameterEscaping(0);

        // Act & Assert - method1's parameter should escape, method2's should not
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method1, 0),
                "Method1's parameter 0 should be escaping");
        assertFalse(ParameterEscapeMarker.isParameterEscaping(method2, 0),
                "Method2's parameter 0 should not be escaping");
    }

    /**
     * Tests isParameterEscaping after updating escaping parameters mask.
     * Using updateEscapingParameters should properly update the bits.
     */
    @Test
    public void testIsParameterEscaping_afterUpdateEscapingParameters_returnsCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Update with bitmask marking parameters 0 and 1
        long mask = (1L << 0) | (1L << 1);
        info.updateEscapingParameters(mask);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 should be escaping after update");
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 1),
                "Parameter 1 should be escaping after update");
    }

    /**
     * Tests isParameterEscaping returns false for parameters not in the mask.
     */
    @Test
    public void testIsParameterEscaping_withParametersNotInMask_returnsFalse() {
        // Arrange - create method with 5 parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
            "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/Double;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark only parameter 2
        info.setParameterEscaping(2);

        // Act & Assert - unmarked parameters should return false
        assertFalse(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 should not be escaping");
        assertFalse(ParameterEscapeMarker.isParameterEscaping(method, 1),
                "Parameter 1 should not be escaping");
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 2),
                "Parameter 2 should be escaping");
        assertFalse(ParameterEscapeMarker.isParameterEscaping(method, 3),
                "Parameter 3 should not be escaping");
        assertFalse(ParameterEscapeMarker.isParameterEscaping(method, 4),
                "Parameter 4 should not be escaping");
    }

    /**
     * Tests isParameterEscaping with method that has no parameters (aside from 'this').
     * Checking non-existent parameter indices should still work with the bitmask logic.
     */
    @Test
    public void testIsParameterEscaping_withNoParameters_worksCorrectly() {
        // Arrange - create method with no parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 0 (which is 'this' for non-static methods)
        info.setParameterEscaping(0);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 ('this') should be escaping");
    }

    /**
     * Tests isParameterEscaping preserves state after marking and unmarking.
     * Once set, the parameter should remain escaping (there's no unmark operation).
     */
    @Test
    public void testIsParameterEscaping_preservesStateAfterMarking_remainsSet() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initial state
        assertFalse(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 should not be escaping initially");

        // Mark as escaping
        info.setParameterEscaping(0);
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 should be escaping after marking");

        // Mark again (should remain set)
        info.setParameterEscaping(0);
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 should still be escaping after marking again");
    }

    /**
     * Tests isParameterEscaping correctly interprets bit positions.
     * Each parameter index corresponds to a bit in the bitmask.
     */
    @Test
    public void testIsParameterEscaping_correctBitPositionInterpretation() {
        // Arrange - create method with parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
            "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters at specific bit positions
        info.setParameterEscaping(0);  // bit 0
        info.setParameterEscaping(2);  // bit 2

        // Act & Assert - verify correct bit interpretation
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 (bit 0) should be escaping");
        assertFalse(ParameterEscapeMarker.isParameterEscaping(method, 1),
                "Parameter 1 (bit 1) should not be escaping");
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 2),
                "Parameter 2 (bit 2) should be escaping");
    }

    /**
     * Tests isParameterEscaping delegates correctly to MethodOptimizationInfo.
     * This verifies the static method properly wraps the instance method.
     */
    @Test
    public void testIsParameterEscaping_delegatesToMethodOptimizationInfo() {
        // Arrange - create method with custom MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo customInfo = new MethodOptimizationInfo();
        method.setProcessingInfo(customInfo);

        // Act - call static method
        boolean result = ParameterEscapeMarker.isParameterEscaping(method, 0);

        // Assert - should delegate to info's isParameterEscaping
        // Default MethodOptimizationInfo returns true when hasNoEscapingParameters is false
        assertTrue(result, "Should delegate to MethodOptimizationInfo.isParameterEscaping");
    }

    /**
     * Tests isParameterEscaping with mixed parameter types (objects and primitives in descriptor).
     * Note: The method itself only checks the index, not the actual type.
     */
    @Test
    public void testIsParameterEscaping_withMixedParameterTypes_checksIndexOnly() {
        // Arrange - create method with mixed parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
            "(Ljava/lang/Object;ILjava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters regardless of type
        info.setParameterEscaping(0);  // Object
        info.setParameterEscaping(1);  // int (primitive)
        info.setParameterEscaping(2);  // String

        // Act & Assert - method checks index only, not type
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 0),
                "Parameter 0 (Object) should be escaping");
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 1),
                "Parameter 1 (int) should be escaping");
        assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 2),
                "Parameter 2 (String) should be escaping");
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

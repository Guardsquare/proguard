package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParameterEscapeMarker#returnsExternalValues(Method)}.
 *
 * The returnsExternalValues static method checks whether a method returns external reference
 * values (values that originate from the heap, but are not parameters or new instances).
 * It delegates to MethodOptimizationInfo.getMethodOptimizationInfo(method).returnsExternalValues().
 *
 * External return values are values from the heap that are not:
 * - Parameters being returned (covered by isParameterReturned)
 * - New instances created in the method (covered by returnsNewInstances)
 *
 * Examples:
 * - Map.get(key) - returns external values (values from heap)
 * - StringBuilder.toString() - returns new instances (not external)
 * - Object identity(Object x) { return x; } - returns parameter (not external)
 *
 * Default behavior (MethodOptimizationInfo): Returns true (conservative assumption).
 * With ProgramMethodOptimizationInfo: Returns false initially, true after setReturnsExternalValues().
 * The setNoExternalReturnValues() method takes precedence and forces it to return false.
 */
public class ParameterEscapeMarkerClaude_returnsExternalValuesTest {

    private ProgramClass testClass;

    @BeforeEach
    public void setUp() {
        testClass = createProgramClassWithConstantPool();
    }

    /**
     * Tests returnsExternalValues returns true by default with MethodOptimizationInfo.
     * Without detailed analysis, the method conservatively assumes external values might be returned.
     */
    @Test
    public void testReturnsExternalValues_withDefaultMethodOptimizationInfo_returnsTrue() {
        // Arrange - create method with default MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, method);

        // Act
        boolean result = ParameterEscapeMarker.returnsExternalValues(method);

        // Assert - should return true by default (conservative assumption)
        assertTrue(result, "Should return true by default (conservative assumption)");
    }

    /**
     * Tests returnsExternalValues with ProgramMethodOptimizationInfo initially returns false.
     * Before analysis marks it, the flag should be false.
     */
    @Test
    public void testReturnsExternalValues_withProgramMethodOptimizationInfo_initiallyReturnsFalse() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act
        boolean result = ParameterEscapeMarker.returnsExternalValues(method);

        // Assert - should return false initially
        assertFalse(result, "Should return false initially with ProgramMethodOptimizationInfo");
    }

    /**
     * Tests returnsExternalValues returns true after calling setReturnsExternalValues.
     */
    @Test
    public void testReturnsExternalValues_afterSetReturnsExternalValues_returnsTrue() {
        // Arrange - create method and mark it as returning external values
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially false
        assertFalse(ParameterEscapeMarker.returnsExternalValues(method),
                "Should be false before marking");

        // Mark as returning external values
        info.setReturnsExternalValues();

        // Act
        boolean result = ParameterEscapeMarker.returnsExternalValues(method);

        // Assert - should now return true
        assertTrue(result, "Should return true after setReturnsExternalValues");
    }

    /**
     * Tests returnsExternalValues returns false after calling setNoExternalReturnValues.
     * This method takes precedence even if setReturnsExternalValues was called.
     */
    @Test
    public void testReturnsExternalValues_afterSetNoExternalReturnValues_returnsFalse() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as having no external return values
        info.setNoExternalReturnValues();

        // Act
        boolean result = ParameterEscapeMarker.returnsExternalValues(method);

        // Assert - should return false
        assertFalse(result, "Should return false after setNoExternalReturnValues");
    }

    /**
     * Tests that setNoExternalReturnValues takes precedence over setReturnsExternalValues.
     * Even if setReturnsExternalValues is called, setNoExternalReturnValues should force false.
     */
    @Test
    public void testReturnsExternalValues_setNoExternalReturnValuesTakesPrecedence() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // First mark as returning external values
        info.setReturnsExternalValues();
        assertTrue(ParameterEscapeMarker.returnsExternalValues(method),
                "Should be true after setReturnsExternalValues");

        // Then mark as having no external return values (takes precedence)
        info.setNoExternalReturnValues();

        // Act
        boolean result = ParameterEscapeMarker.returnsExternalValues(method);

        // Assert - should return false (setNoExternalReturnValues takes precedence)
        assertFalse(result, "Should return false (setNoExternalReturnValues takes precedence)");
    }

    /**
     * Tests that setNoExternalReturnValues takes precedence regardless of call order.
     * Even if called before setReturnsExternalValues, it should still take precedence.
     */
    @Test
    public void testReturnsExternalValues_setNoExternalReturnValuesPrecedenceRegardlessOfOrder() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // First mark as having no external return values
        info.setNoExternalReturnValues();

        // Then try to mark as returning external values (should be overridden)
        info.setReturnsExternalValues();

        // Act
        boolean result = ParameterEscapeMarker.returnsExternalValues(method);

        // Assert - should still return false (setNoExternalReturnValues takes precedence)
        assertFalse(result, "Should return false (setNoExternalReturnValues takes precedence regardless of order)");
    }

    /**
     * Tests returnsExternalValues is consistent across multiple calls.
     * The result should not change between calls.
     */
    @Test
    public void testReturnsExternalValues_multipleCalls_returnsConsistentResults() {
        // Arrange - create method and mark it
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setReturnsExternalValues();

        // Act - call multiple times
        boolean firstCall = ParameterEscapeMarker.returnsExternalValues(method);
        boolean secondCall = ParameterEscapeMarker.returnsExternalValues(method);
        boolean thirdCall = ParameterEscapeMarker.returnsExternalValues(method);

        // Assert - all calls should return the same result
        assertTrue(firstCall, "First call should return true");
        assertTrue(secondCall, "Second call should return true");
        assertTrue(thirdCall, "Third call should return true");
        assertEquals(firstCall, secondCall, "First and second calls should match");
        assertEquals(secondCall, thirdCall, "Second and third calls should match");
    }

    /**
     * Tests returnsExternalValues with different methods independently.
     * Each method should track its own flags independently.
     */
    @Test
    public void testReturnsExternalValues_withDifferentMethods_worksIndependently() {
        // Arrange - create three methods with different states
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);
        ProgramMethodOptimizationInfo info1 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method1);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);
        ProgramMethodOptimizationInfo info2 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method2);

        ProgramMethod method3 = createMethodWithDescriptor(testClass, "method3", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method3);
        ProgramMethodOptimizationInfo info3 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method3);

        // Mark method1 as returning external values
        info1.setReturnsExternalValues();

        // Mark method2 with no external return values
        info2.setNoExternalReturnValues();

        // Leave method3 unmarked

        // Act & Assert - methods should have independent flags
        assertTrue(ParameterEscapeMarker.returnsExternalValues(method1),
                "Method1 should return external values");
        assertFalse(ParameterEscapeMarker.returnsExternalValues(method2),
                "Method2 should not return external values");
        assertFalse(ParameterEscapeMarker.returnsExternalValues(method3),
                "Method3 should be false initially");
    }

    /**
     * Tests returnsExternalValues delegates correctly to MethodOptimizationInfo.
     * Verifies the static method properly wraps the instance method.
     */
    @Test
    public void testReturnsExternalValues_delegatesToMethodOptimizationInfo() {
        // Arrange - create method with custom MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        MethodOptimizationInfo customInfo = new MethodOptimizationInfo();
        method.setProcessingInfo(customInfo);

        // Act
        boolean result = ParameterEscapeMarker.returnsExternalValues(method);

        // Assert - should delegate to info's returnsExternalValues
        // Default MethodOptimizationInfo returns true (hasNoExternalReturnValues is false initially)
        assertTrue(result, "Should delegate to MethodOptimizationInfo.returnsExternalValues");
    }

    /**
     * Tests returnsExternalValues after setNoExternalReturnValues on MethodOptimizationInfo.
     */
    @Test
    public void testReturnsExternalValues_withMethodOptimizationInfoSetNoExternal_returnsFalse() {
        // Arrange - create method with MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        method.setProcessingInfo(info);

        // Mark as having no external return values
        info.setNoExternalReturnValues();

        // Act
        boolean result = ParameterEscapeMarker.returnsExternalValues(method);

        // Assert - should return false
        assertFalse(result, "Should return false after setNoExternalReturnValues");
    }

    /**
     * Tests returnsExternalValues with static method.
     * Static methods should work the same as instance methods.
     */
    @Test
    public void testReturnsExternalValues_withStaticMethod_worksCorrectly() {
        // Arrange - create static method
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "staticMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially false
        assertFalse(ParameterEscapeMarker.returnsExternalValues(method),
                "Static method should be false initially");

        // Mark as returning external values
        info.setReturnsExternalValues();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsExternalValues(method),
                "Static method should return true after marking");
    }

    /**
     * Tests returnsExternalValues with void return type method.
     * Even void methods can have the flag tracked (though semantically unusual).
     */
    @Test
    public void testReturnsExternalValues_withVoidReturnType_tracksCorrectly() {
        // Arrange - create method with void return type
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially false
        assertFalse(ParameterEscapeMarker.returnsExternalValues(method),
                "Should be false initially");

        // Mark as returning external values (tracking works even for void)
        info.setReturnsExternalValues();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsExternalValues(method),
                "Flag should be set even with void return type");
    }

    /**
     * Tests returnsExternalValues with primitive return type method.
     * Primitive return types cannot return object instances, but flag tracking still works.
     */
    @Test
    public void testReturnsExternalValues_withPrimitiveReturnType_tracksCorrectly() {
        // Arrange - create method with int return type
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()I");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as returning external values
        info.setReturnsExternalValues();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsExternalValues(method),
                "Flag should be set even with primitive return type");
    }

    /**
     * Tests returnsExternalValues with Object return type (typical use case).
     */
    @Test
    public void testReturnsExternalValues_withObjectReturnType_worksCorrectly() {
        // Arrange - create method returning Object
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as returning external values
        info.setReturnsExternalValues();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsExternalValues(method),
                "Should return true after marking");
    }

    /**
     * Tests returnsExternalValues with String return type (common getter scenario).
     */
    @Test
    public void testReturnsExternalValues_withStringReturnType_worksCorrectly() {
        // Arrange - create method returning String
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/String;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as returning external values
        info.setReturnsExternalValues();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsExternalValues(method),
                "Should return true for String return type");
    }

    /**
     * Tests returnsExternalValues with array return type.
     */
    @Test
    public void testReturnsExternalValues_withArrayReturnType_worksCorrectly() {
        // Arrange - create method returning array
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()[Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as returning external values
        info.setReturnsExternalValues();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsExternalValues(method),
                "Should return true for array return type");
    }

    /**
     * Tests returnsExternalValues with method that takes parameters.
     * The presence of parameters should not affect the flag.
     */
    @Test
    public void testReturnsExternalValues_withParameters_worksCorrectly() {
        // Arrange - create method with parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;I)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as returning external values
        info.setReturnsExternalValues();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsExternalValues(method),
                "Should return true regardless of parameters");
    }

    /**
     * Tests returnsExternalValues state transitions.
     * The flag should only go from false to true via setReturnsExternalValues.
     */
    @Test
    public void testReturnsExternalValues_stateTransitions_normalFlow() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // State 1: initially false
        assertFalse(ParameterEscapeMarker.returnsExternalValues(method),
                "State 1: Should be false initially");

        // State 2: set to true
        info.setReturnsExternalValues();
        assertTrue(ParameterEscapeMarker.returnsExternalValues(method),
                "State 2: Should be true after setReturnsExternalValues");

        // State 3: remains true (idempotent)
        info.setReturnsExternalValues();
        assertTrue(ParameterEscapeMarker.returnsExternalValues(method),
                "State 3: Should remain true");
    }

    /**
     * Tests returnsExternalValues complex state: both flags set, setNoExternalReturnValues wins.
     */
    @Test
    public void testReturnsExternalValues_bothFlagsSet_noExternalWins() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Set both flags
        info.setReturnsExternalValues();
        info.setNoExternalReturnValues();

        // Act
        boolean result = ParameterEscapeMarker.returnsExternalValues(method);

        // Assert - setNoExternalReturnValues takes precedence
        assertFalse(result, "Should return false when both flags set (setNoExternalReturnValues wins)");
    }

    /**
     * Tests returnsExternalValues with multiple method scenarios simultaneously.
     * Verifies that different methods maintain independent state.
     */
    @Test
    public void testReturnsExternalValues_multipleMethodsSimultaneously_maintainIndependentState() {
        // Arrange - create four methods with different states
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);
        ProgramMethodOptimizationInfo info2 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method2);
        info2.setReturnsExternalValues();

        ProgramMethod method3 = createMethodWithDescriptor(testClass, "method3", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method3);
        ProgramMethodOptimizationInfo info3 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method3);
        info3.setNoExternalReturnValues();

        ProgramMethod method4 = createMethodWithDescriptor(testClass, "method4", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method4);
        ProgramMethodOptimizationInfo info4 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method4);
        info4.setReturnsExternalValues();
        info4.setNoExternalReturnValues();

        // Act & Assert - each method should have independent state
        assertFalse(ParameterEscapeMarker.returnsExternalValues(method1),
                "Method1 should be false (unmarked)");
        assertTrue(ParameterEscapeMarker.returnsExternalValues(method2),
                "Method2 should be true (setReturnsExternalValues)");
        assertFalse(ParameterEscapeMarker.returnsExternalValues(method3),
                "Method3 should be false (setNoExternalReturnValues)");
        assertFalse(ParameterEscapeMarker.returnsExternalValues(method4),
                "Method4 should be false (both set, no-external wins)");
    }

    /**
     * Tests returnsExternalValues with complex descriptor.
     */
    @Test
    public void testReturnsExternalValues_withComplexDescriptor_worksCorrectly() {
        // Arrange - create method with complex descriptor
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;[ILjava/util/Map;)Ljava/util/List;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as returning external values
        info.setReturnsExternalValues();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsExternalValues(method),
                "Should work correctly with complex descriptor");
    }

    /**
     * Tests returnsExternalValues is a boolean flag controlled by two setters.
     * Shows the interaction between setReturnsExternalValues and setNoExternalReturnValues.
     */
    @Test
    public void testReturnsExternalValues_twoSeparateFlags_interactionPattern() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially both flags are false
        // returnsExternalValues() returns: !hasNoExternalReturnValues && returnsExternalValues
        // So: !false && false = false
        assertFalse(ParameterEscapeMarker.returnsExternalValues(method),
                "Initially: !false && false = false");

        // Set returnsExternalValues = true
        // returnsExternalValues() returns: !false && true = true
        info.setReturnsExternalValues();
        assertTrue(ParameterEscapeMarker.returnsExternalValues(method),
                "After setReturnsExternalValues: !false && true = true");

        // Set hasNoExternalReturnValues = true (via setNoExternalReturnValues)
        // returnsExternalValues() returns: !true && true = false
        info.setNoExternalReturnValues();
        assertFalse(ParameterEscapeMarker.returnsExternalValues(method),
                "After setNoExternalReturnValues: !true && true = false");
    }

    /**
     * Tests returnsExternalValues consistency with MethodOptimizationInfo vs ProgramMethodOptimizationInfo.
     * Shows the difference between conservative default and analyzed behavior.
     */
    @Test
    public void testReturnsExternalValues_comparesDefaultVsProgram() {
        // Arrange - create two methods with different info types
        ProgramMethod methodDefault = createMethodWithDescriptor(testClass, "methodDefault", "()Ljava/lang/Object;");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, methodDefault);

        ProgramMethod methodProgram = createMethodWithDescriptor(testClass, "methodProgram", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, methodProgram);

        // Act
        boolean defaultResult = ParameterEscapeMarker.returnsExternalValues(methodDefault);
        boolean programResult = ParameterEscapeMarker.returnsExternalValues(methodProgram);

        // Assert - default is conservative (true), program is precise (false initially)
        assertTrue(defaultResult, "Default MethodOptimizationInfo should return true (conservative)");
        assertFalse(programResult, "ProgramMethodOptimizationInfo should return false initially (precise)");
    }

    /**
     * Tests returnsExternalValues with newly created ProgramMethodOptimizationInfo instances.
     * All newly created instances should start with false.
     */
    @Test
    public void testReturnsExternalValues_newlyCreatedInfo_startsWithFalse() {
        // Test with multiple newly created methods
        for (int i = 0; i < 5; i++) {
            ProgramMethod method = createMethodWithDescriptor(testClass, "method" + i, "()Ljava/lang/Object;");
            ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

            // Each new method should start with false
            assertFalse(ParameterEscapeMarker.returnsExternalValues(method),
                    "Method" + i + " should start with false");
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

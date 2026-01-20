package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParameterEscapeMarker#returnsNewInstances(Method)}.
 *
 * The returnsNewInstances static method checks whether a method returns new instances
 * that are created inside the method. It delegates to
 * MethodOptimizationInfo.getMethodOptimizationInfo(method).returnsNewInstances().
 *
 * A method "returns new instances" when it creates objects via 'new' (or similar)
 * and returns them. For example:
 * - Object createObject() { return new Object(); } - returns new instances
 * - Object identity(Object x) { return x; } - does NOT return new instances (returns parameter)
 *
 * Default behavior (MethodOptimizationInfo): Returns true (conservative assumption).
 * With ProgramMethodOptimizationInfo: Returns false initially, true after setReturnsNewInstances().
 */
public class ParameterEscapeMarkerClaude_returnsNewInstancesTest {

    private ProgramClass testClass;

    @BeforeEach
    public void setUp() {
        testClass = createProgramClassWithConstantPool();
    }

    /**
     * Tests returnsNewInstances returns true by default with MethodOptimizationInfo.
     * Without detailed analysis, the method conservatively assumes new instances might be returned.
     */
    @Test
    public void testReturnsNewInstances_withDefaultMethodOptimizationInfo_returnsTrue() {
        // Arrange - create method with default MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, method);

        // Act
        boolean result = ParameterEscapeMarker.returnsNewInstances(method);

        // Assert - should return true by default (conservative assumption)
        assertTrue(result, "Should return true by default (conservative assumption)");
    }

    /**
     * Tests returnsNewInstances with ProgramMethodOptimizationInfo initially returns false.
     * Before analysis marks it, the flag should be false.
     */
    @Test
    public void testReturnsNewInstances_withProgramMethodOptimizationInfo_initiallyReturnsFalse() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act
        boolean result = ParameterEscapeMarker.returnsNewInstances(method);

        // Assert - should return false initially
        assertFalse(result, "Should return false initially with ProgramMethodOptimizationInfo");
    }

    /**
     * Tests returnsNewInstances returns true after calling setReturnsNewInstances.
     */
    @Test
    public void testReturnsNewInstances_afterSetReturnsNewInstances_returnsTrue() {
        // Arrange - create method and mark it as returning new instances
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially false
        assertFalse(ParameterEscapeMarker.returnsNewInstances(method),
                "Should be false before marking");

        // Mark as returning new instances
        info.setReturnsNewInstances();

        // Act
        boolean result = ParameterEscapeMarker.returnsNewInstances(method);

        // Assert - should now return true
        assertTrue(result, "Should return true after setReturnsNewInstances");
    }

    /**
     * Tests returnsNewInstances is consistent across multiple calls.
     * The result should not change between calls.
     */
    @Test
    public void testReturnsNewInstances_multipleCalls_returnsConsistentResults() {
        // Arrange - create method and mark it
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setReturnsNewInstances();

        // Act - call multiple times
        boolean firstCall = ParameterEscapeMarker.returnsNewInstances(method);
        boolean secondCall = ParameterEscapeMarker.returnsNewInstances(method);
        boolean thirdCall = ParameterEscapeMarker.returnsNewInstances(method);

        // Assert - all calls should return the same result
        assertTrue(firstCall, "First call should return true");
        assertTrue(secondCall, "Second call should return true");
        assertTrue(thirdCall, "Third call should return true");
        assertEquals(firstCall, secondCall, "First and second calls should match");
        assertEquals(secondCall, thirdCall, "Second and third calls should match");
    }

    /**
     * Tests returnsNewInstances with different methods independently.
     * Each method should track its own flag independently.
     */
    @Test
    public void testReturnsNewInstances_withDifferentMethods_worksIndependently() {
        // Arrange - create two methods
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);
        ProgramMethodOptimizationInfo info1 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method1);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);

        // Mark only method1 as returning new instances
        info1.setReturnsNewInstances();

        // Act & Assert - methods should have independent flags
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method1),
                "Method1 should return new instances");
        assertFalse(ParameterEscapeMarker.returnsNewInstances(method2),
                "Method2 should not return new instances");
    }

    /**
     * Tests returnsNewInstances preserves state after being set.
     * Once set to true, it should remain true.
     */
    @Test
    public void testReturnsNewInstances_preservesStateAfterSetting_remainsTrue() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initial state
        assertFalse(ParameterEscapeMarker.returnsNewInstances(method),
                "Should be false initially");

        // Set to true
        info.setReturnsNewInstances();
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method),
                "Should be true after setting");

        // Call set again (idempotent)
        info.setReturnsNewInstances();
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method),
                "Should remain true after setting again");
    }

    /**
     * Tests returnsNewInstances delegates correctly to MethodOptimizationInfo.
     * Verifies the static method properly wraps the instance method.
     */
    @Test
    public void testReturnsNewInstances_delegatesToMethodOptimizationInfo() {
        // Arrange - create method with custom MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        MethodOptimizationInfo customInfo = new MethodOptimizationInfo();
        method.setProcessingInfo(customInfo);

        // Act
        boolean result = ParameterEscapeMarker.returnsNewInstances(method);

        // Assert - should delegate to info's returnsNewInstances
        // Default MethodOptimizationInfo returns true
        assertTrue(result, "Should delegate to MethodOptimizationInfo.returnsNewInstances");
    }

    /**
     * Tests returnsNewInstances with static method.
     * Static methods should work the same as instance methods.
     */
    @Test
    public void testReturnsNewInstances_withStaticMethod_worksCorrectly() {
        // Arrange - create static method
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "staticMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially false
        assertFalse(ParameterEscapeMarker.returnsNewInstances(method),
                "Static method should be false initially");

        // Mark as returning new instances
        info.setReturnsNewInstances();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method),
                "Static method should return true after marking");
    }

    /**
     * Tests returnsNewInstances with void return type method.
     * Even void methods can have the flag set (though it may not make semantic sense).
     */
    @Test
    public void testReturnsNewInstances_withVoidReturnType_tracksCorrectly() {
        // Arrange - create method with void return type
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially false
        assertFalse(ParameterEscapeMarker.returnsNewInstances(method),
                "Should be false initially");

        // Mark as returning new instances (tracking works even for void)
        info.setReturnsNewInstances();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method),
                "Flag should be set even with void return type");
    }

    /**
     * Tests returnsNewInstances with primitive return type method.
     * Primitive return types cannot return object instances, but flag tracking still works.
     */
    @Test
    public void testReturnsNewInstances_withPrimitiveReturnType_tracksCorrectly() {
        // Arrange - create method with int return type
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()I");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially false
        assertFalse(ParameterEscapeMarker.returnsNewInstances(method),
                "Should be false initially");

        // Mark as returning new instances
        info.setReturnsNewInstances();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method),
                "Flag should be set even with primitive return type");
    }

    /**
     * Tests returnsNewInstances with method returning Object.
     * This is the typical use case for this flag.
     */
    @Test
    public void testReturnsNewInstances_withObjectReturnType_worksCorrectly() {
        // Arrange - create method returning Object
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially false
        assertFalse(ParameterEscapeMarker.returnsNewInstances(method),
                "Should be false initially");

        // Mark as returning new instances
        info.setReturnsNewInstances();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method),
                "Should return true after marking");
    }

    /**
     * Tests returnsNewInstances with method returning String.
     * String is a common return type for factory methods.
     */
    @Test
    public void testReturnsNewInstances_withStringReturnType_worksCorrectly() {
        // Arrange - create method returning String
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/String;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as returning new instances
        info.setReturnsNewInstances();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method),
                "Should return true for String return type");
    }

    /**
     * Tests returnsNewInstances with method returning array type.
     * Array creation should also be considered "new instances".
     */
    @Test
    public void testReturnsNewInstances_withArrayReturnType_worksCorrectly() {
        // Arrange - create method returning array
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()[Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as returning new instances
        info.setReturnsNewInstances();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method),
                "Should return true for array return type");
    }

    /**
     * Tests returnsNewInstances with method that takes parameters.
     * The presence of parameters should not affect the flag.
     */
    @Test
    public void testReturnsNewInstances_withParameters_worksCorrectly() {
        // Arrange - create method with parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;I)Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as returning new instances
        info.setReturnsNewInstances();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method),
                "Should return true regardless of parameters");
    }

    /**
     * Tests returnsNewInstances with constructor.
     * Constructors always create new instances, though they return void.
     */
    @Test
    public void testReturnsNewInstances_withConstructor_tracksCorrectly() {
        // Arrange - create constructor (special name <init>)
        ProgramMethod method = createMethodWithDescriptor(testClass, "<init>", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially false
        assertFalse(ParameterEscapeMarker.returnsNewInstances(method),
                "Constructor should be false initially");

        // Mark as returning new instances
        info.setReturnsNewInstances();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method),
                "Constructor can have flag set");
    }

    /**
     * Tests returnsNewInstances state transitions.
     * The flag should only go from false to true, never back to false.
     */
    @Test
    public void testReturnsNewInstances_stateTransitions_onlyGoesToTrue() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // State 1: initially false
        assertFalse(ParameterEscapeMarker.returnsNewInstances(method),
                "State 1: Should be false initially");

        // State 2: set to true
        info.setReturnsNewInstances();
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method),
                "State 2: Should be true after setting");

        // State 3: remains true (there's no unset method)
        // Calling set again should keep it true
        info.setReturnsNewInstances();
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method),
                "State 3: Should remain true");
    }

    /**
     * Tests returnsNewInstances with multiple method scenarios simultaneously.
     * Verifies that different methods maintain independent state.
     */
    @Test
    public void testReturnsNewInstances_multipleMethodsSimultaneously_maintainIndependentState() {
        // Arrange - create three methods
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);
        ProgramMethodOptimizationInfo info2 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method2);

        ProgramMethod method3 = createMethodWithDescriptor(testClass, "method3", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method3);

        // Mark only method2
        info2.setReturnsNewInstances();

        // Act & Assert - each method should have independent state
        assertFalse(ParameterEscapeMarker.returnsNewInstances(method1),
                "Method1 should be false");
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method2),
                "Method2 should be true");
        assertFalse(ParameterEscapeMarker.returnsNewInstances(method3),
                "Method3 should be false");
    }

    /**
     * Tests returnsNewInstances with method having complex descriptor.
     * Complex descriptors should not affect the flag behavior.
     */
    @Test
    public void testReturnsNewInstances_withComplexDescriptor_worksCorrectly() {
        // Arrange - create method with complex descriptor
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;[ILjava/util/Map;)Ljava/util/List;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as returning new instances
        info.setReturnsNewInstances();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method),
                "Should work correctly with complex descriptor");
    }

    /**
     * Tests returnsNewInstances behavior is boolean (not bitmask).
     * This is different from escaping/returned parameters which use bitmasks.
     */
    @Test
    public void testReturnsNewInstances_isBooleanFlag_notBitmask() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially false
        boolean initial = ParameterEscapeMarker.returnsNewInstances(method);
        assertFalse(initial, "Should be false initially");

        // Set to true
        info.setReturnsNewInstances();
        boolean afterSet = ParameterEscapeMarker.returnsNewInstances(method);
        assertTrue(afterSet, "Should be true after setting");

        // Verify it's a simple boolean (not numeric or bitmask)
        assertNotEquals(initial, afterSet, "Values should be different (boolean toggle)");
    }

    /**
     * Tests returnsNewInstances with ProgramMethodOptimizationInfo created at different times.
     * Newly created info should always start with false.
     */
    @Test
    public void testReturnsNewInstances_newlyCreatedInfo_startsWithFalse() {
        // Test with multiple newly created methods
        for (int i = 0; i < 5; i++) {
            ProgramMethod method = createMethodWithDescriptor(testClass, "method" + i, "()Ljava/lang/Object;");
            ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

            // Each new method should start with false
            assertFalse(ParameterEscapeMarker.returnsNewInstances(method),
                    "Method" + i + " should start with false");
        }
    }

    /**
     * Tests returnsNewInstances with method that has no code attribute.
     * The flag should still work even without actual method body.
     */
    @Test
    public void testReturnsNewInstances_withoutCodeAttribute_tracksCorrectly() {
        // Arrange - create abstract method (no code attribute)
        ProgramMethod method = createAbstractMethodWithDescriptor(testClass, "abstractMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially false
        assertFalse(ParameterEscapeMarker.returnsNewInstances(method),
                "Abstract method should be false initially");

        // Mark as returning new instances
        info.setReturnsNewInstances();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.returnsNewInstances(method),
                "Abstract method can have flag set");
    }

    /**
     * Tests returnsNewInstances consistency with MethodOptimizationInfo vs ProgramMethodOptimizationInfo.
     * Shows the difference between conservative default and analyzed behavior.
     */
    @Test
    public void testReturnsNewInstances_comparesDefaultVsProgram() {
        // Arrange - create two methods with different info types
        ProgramMethod methodDefault = createMethodWithDescriptor(testClass, "methodDefault", "()Ljava/lang/Object;");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, methodDefault);

        ProgramMethod methodProgram = createMethodWithDescriptor(testClass, "methodProgram", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, methodProgram);

        // Act
        boolean defaultResult = ParameterEscapeMarker.returnsNewInstances(methodDefault);
        boolean programResult = ParameterEscapeMarker.returnsNewInstances(methodProgram);

        // Assert - default is conservative (true), program is precise (false initially)
        assertTrue(defaultResult, "Default MethodOptimizationInfo should return true (conservative)");
        assertFalse(programResult, "ProgramMethodOptimizationInfo should return false initially (precise)");
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

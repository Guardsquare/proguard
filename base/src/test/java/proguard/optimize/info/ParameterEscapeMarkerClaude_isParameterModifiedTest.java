package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParameterEscapeMarker#isParameterModified(Method, int)}.
 *
 * The isParameterModified static method checks whether the contents of a specific reference
 * parameter are modified in the method. It delegates to
 * MethodOptimizationInfo.getMethodOptimizationInfo(method).isParameterModified(parameterIndex).
 *
 * A parameter is "modified" when its internal state is changed (e.g., fields are written,
 * array elements are modified, or methods that change state are called on it).
 *
 * For example:
 * - void append(StringBuilder sb) { sb.append("x"); } - modifies parameter 0
 * - Object identity(Object x) { return x; } - does NOT modify parameter
 *
 * The logic involves multiple flags:
 * - hasNoSideEffects: if true, no parameters are modified
 * - hasNoExternalSideEffects: if true, only parameter 0 ('this') can be modified
 * - modifiedParameters bitmask: specific parameters marked as modified
 * - modifiesAnything: if true, escaped parameters are also considered modified
 *
 * Default behavior (MethodOptimizationInfo): Conservative based on side effect flags.
 * With ProgramMethodOptimizationInfo: Checks bitmask plus side effect flags.
 */
public class ParameterEscapeMarkerClaude_isParameterModifiedTest {

    private ProgramClass testClass;

    @BeforeEach
    public void setUp() {
        testClass = createProgramClassWithConstantPool();
    }

    /**
     * Tests isParameterModified returns true by default with MethodOptimizationInfo.
     * Without side effect marking, the method conservatively assumes parameters might be modified.
     */
    @Test
    public void testIsParameterModified_withDefaultMethodOptimizationInfo_returnsTrue() {
        // Arrange - create method with default MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, method);

        // Act & Assert - should return true by default for parameter 0
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should be modified by default (conservative assumption)");
    }

    /**
     * Tests isParameterModified with ProgramMethodOptimizationInfo initially returns false.
     * Before marking, parameters should not be considered modified.
     */
    @Test
    public void testIsParameterModified_withProgramMethodOptimizationInfo_initiallyReturnsFalse() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act & Assert - should return false initially (no parameters marked as modified)
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should not be modified initially with ProgramMethodOptimizationInfo");
    }

    /**
     * Tests isParameterModified after marking a parameter as modified.
     */
    @Test
    public void testIsParameterModified_afterSetParameterModified_returnsTrue() {
        // Arrange - create method and mark parameter 0 as modified
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterModified(0);

        // Act
        boolean result = ParameterEscapeMarker.isParameterModified(method, 0);

        // Assert - should now return true
        assertTrue(result, "Parameter 0 should be modified after marking");
    }

    /**
     * Tests isParameterModified with specific parameter marked among multiple.
     */
    @Test
    public void testIsParameterModified_withSpecificParameterMarked_returnsCorrectly() {
        // Arrange - create method with multiple parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark only parameter 1 as modified
        info.setParameterModified(1);

        // Act & Assert
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should not be modified");
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 1),
                "Parameter 1 should be modified");
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 2),
                "Parameter 2 should not be modified");
    }

    /**
     * Tests isParameterModified with multiple parameters marked.
     */
    @Test
    public void testIsParameterModified_withMultipleParametersMarked_returnsCorrectly() {
        // Arrange - create method with multiple parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters 0 and 2 as modified
        info.setParameterModified(0);
        info.setParameterModified(2);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should be modified");
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 1),
                "Parameter 1 should not be modified");
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 2),
                "Parameter 2 should be modified");
    }

    /**
     * Tests isParameterModified returns false after setNoSideEffects.
     * If a method has no side effects, no parameters can be modified.
     */
    @Test
    public void testIsParameterModified_afterSetNoSideEffects_returnsFalse() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter as modified
        info.setParameterModified(0);
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Should be true before setNoSideEffects");

        // Mark as having no side effects
        info.setNoSideEffects();

        // Act & Assert - should now return false (no side effects overrides)
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 0),
                "Should return false after setNoSideEffects (overrides modified flag)");
    }

    /**
     * Tests isParameterModified with setNoExternalSideEffects.
     * Only parameter 0 ('this') can be modified if there are no external side effects.
     */
    @Test
    public void testIsParameterModified_afterSetNoExternalSideEffects_onlyParameter0() {
        // Arrange - create method with multiple parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark both parameters as modified
        info.setParameterModified(0);
        info.setParameterModified(1);

        // Verify both are modified initially
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0));
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 1));

        // Mark as having no external side effects
        info.setNoExternalSideEffects();

        // Act & Assert - only parameter 0 should be modified
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 ('this') should still be modified");
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 1),
                "Parameter 1 should not be modified (no external side effects)");
    }

    /**
     * Tests isParameterModified with modifiesAnything flag set.
     * When modifiesAnything is true, escaped parameters are also considered modified.
     */
    @Test
    public void testIsParameterModified_withModifiesAnything_includesEscapedParameters() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 0 as escaped (not modified)
        info.setParameterEscaped(0);

        // Initially not modified
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should not be modified initially");

        // Set modifiesAnything flag
        info.setModifiesAnything();

        // Act & Assert - escaped parameter should now be considered modified
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should be modified when modifiesAnything is set (includes escaped)");
    }

    /**
     * Tests isParameterModified with static method.
     */
    @Test
    public void testIsParameterModified_withStaticMethod_worksCorrectly() {
        // Arrange - create static method
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "staticMethod",
                "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 0 as modified
        info.setParameterModified(0);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 of static method should be modified");
    }

    /**
     * Tests isParameterModified consistency across multiple calls.
     */
    @Test
    public void testIsParameterModified_multipleCalls_returnsConsistentResults() {
        // Arrange - create method and mark parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterModified(0);

        // Act - call multiple times
        boolean firstCall = ParameterEscapeMarker.isParameterModified(method, 0);
        boolean secondCall = ParameterEscapeMarker.isParameterModified(method, 0);
        boolean thirdCall = ParameterEscapeMarker.isParameterModified(method, 0);

        // Assert - all calls should return the same result
        assertTrue(firstCall, "First call should return true");
        assertTrue(secondCall, "Second call should return true");
        assertTrue(thirdCall, "Third call should return true");
        assertEquals(firstCall, secondCall, "First and second calls should match");
        assertEquals(secondCall, thirdCall, "Second and third calls should match");
    }

    /**
     * Tests isParameterModified with different methods independently.
     */
    @Test
    public void testIsParameterModified_withDifferentMethods_worksIndependently() {
        // Arrange - create two methods
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);
        ProgramMethodOptimizationInfo info1 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method1);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);

        // Mark only method1's parameter as modified
        info1.setParameterModified(0);

        // Act & Assert - methods should have independent state
        assertTrue(ParameterEscapeMarker.isParameterModified(method1, 0),
                "Method1's parameter 0 should be modified");
        assertFalse(ParameterEscapeMarker.isParameterModified(method2, 0),
                "Method2's parameter 0 should not be modified");
    }

    /**
     * Tests isParameterModified after updateModifiedParameters.
     */
    @Test
    public void testIsParameterModified_afterUpdateModifiedParameters_returnsCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Update with bitmask marking parameters 0 and 1
        long mask = (1L << 0) | (1L << 1);
        info.updateModifiedParameters(mask);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should be modified after update");
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 1),
                "Parameter 1 should be modified after update");
    }

    /**
     * Tests isParameterModified with parameter index 0 ('this' for non-static methods).
     */
    @Test
    public void testIsParameterModified_withParameterIndexZero_worksCorrectly() {
        // Arrange - create non-static method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially not modified
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should not be modified initially");

        // Mark parameter 0 as modified
        info.setParameterModified(0);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should be modified after marking");
    }

    /**
     * Tests isParameterModified with higher parameter indices.
     */
    @Test
    public void testIsParameterModified_withHigherParameterIndices_worksCorrectly() {
        // Arrange - create method with many parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/Double;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter at higher index
        info.setParameterModified(4);

        // Act & Assert
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should not be modified");
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 3),
                "Parameter 3 should not be modified");
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 4),
                "Parameter 4 should be modified");
    }

    /**
     * Tests isParameterModified preserves state after marking.
     */
    @Test
    public void testIsParameterModified_preservesStateAfterMarking_remainsSet() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initial state
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should not be modified initially");

        // Mark as modified
        info.setParameterModified(0);
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should be modified after marking");

        // Mark again (idempotent)
        info.setParameterModified(0);
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should still be modified after marking again");
    }

    /**
     * Tests isParameterModified correct bit position interpretation.
     */
    @Test
    public void testIsParameterModified_correctBitPositionInterpretation() {
        // Arrange - create method with parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters at specific bit positions
        info.setParameterModified(0);  // bit 0
        info.setParameterModified(2);  // bit 2

        // Act & Assert - verify correct bit interpretation
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 (bit 0) should be modified");
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 1),
                "Parameter 1 (bit 1) should not be modified");
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 2),
                "Parameter 2 (bit 2) should be modified");
    }

    /**
     * Tests isParameterModified delegates correctly to MethodOptimizationInfo.
     */
    @Test
    public void testIsParameterModified_delegatesToMethodOptimizationInfo() {
        // Arrange - create method with custom MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo customInfo = new MethodOptimizationInfo();
        method.setProcessingInfo(customInfo);

        // Act
        boolean result = ParameterEscapeMarker.isParameterModified(method, 0);

        // Assert - should delegate to info's isParameterModified
        // Default MethodOptimizationInfo returns true for parameter 0 when no side effect flags are set
        assertTrue(result, "Should delegate to MethodOptimizationInfo.isParameterModified");
    }

    /**
     * Tests isParameterModified with MethodOptimizationInfo after setNoSideEffects.
     */
    @Test
    public void testIsParameterModified_methodOptimizationInfoWithNoSideEffects_returnsFalse() {
        // Arrange - create method with MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        method.setProcessingInfo(info);

        // Initially true (conservative)
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0));

        // Mark as having no side effects
        info.setNoSideEffects();

        // Act & Assert - should now return false
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 0),
                "Should return false after setNoSideEffects");
    }

    /**
     * Tests isParameterModified with MethodOptimizationInfo after setNoExternalSideEffects.
     */
    @Test
    public void testIsParameterModified_methodOptimizationInfoWithNoExternalSideEffects_onlyParameter0() {
        // Arrange - create method with MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        method.setProcessingInfo(info);

        // Initially true for both (conservative)
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0));
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 1));

        // Mark as having no external side effects
        info.setNoExternalSideEffects();

        // Act & Assert - only parameter 0 should be modified
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should still be modified");
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 1),
                "Parameter 1 should not be modified (no external side effects)");
    }

    /**
     * Tests isParameterModified with incremental marking.
     */
    @Test
    public void testIsParameterModified_withIncrementalMarking_tracksAll() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark incrementally and check after each
        info.setParameterModified(0);
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should be modified after marking");
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 1),
                "Parameter 1 should not be modified yet");

        info.setParameterModified(2);
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should still be modified");
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 1),
                "Parameter 1 should still not be modified");
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 2),
                "Parameter 2 should be modified after marking");
    }

    /**
     * Tests isParameterModified with cumulative updates via updateModifiedParameters.
     */
    @Test
    public void testIsParameterModified_withCumulativeUpdates_accumulatesCorrectly() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Integer;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Update with different masks
        info.updateModifiedParameters(1L << 0);  // Mark parameter 0
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should be modified after first update");

        info.updateModifiedParameters(1L << 2);  // Mark parameter 2
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should still be modified");
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 2),
                "Parameter 2 should be modified after second update");
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 1),
                "Parameter 1 should not be modified");
    }

    /**
     * Tests isParameterModified with mixed parameter types in descriptor.
     */
    @Test
    public void testIsParameterModified_withMixedParameterTypes_checksIndexOnly() {
        // Arrange - create method with mixed parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;ILjava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters regardless of type
        info.setParameterModified(0);  // Object
        info.setParameterModified(1);  // int (primitive)
        info.setParameterModified(2);  // String

        // Act & Assert - method checks index only, not type
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 (Object) should be modified");
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 1),
                "Parameter 1 (int) should be modified");
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 2),
                "Parameter 2 (String) should be modified");
    }

    /**
     * Tests isParameterModified interaction between modifiesAnything and escaped parameters.
     * Escaped parameters should only be considered modified when modifiesAnything is true.
     */
    @Test
    public void testIsParameterModified_escapedParametersOnlyWhenModifiesAnything() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 0 as escaped (not explicitly modified)
        info.setParameterEscaped(0);
        // Mark parameter 1 as explicitly modified
        info.setParameterModified(1);

        // Without modifiesAnything
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 0),
                "Escaped parameter 0 should not be modified without modifiesAnything");
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 1),
                "Explicitly modified parameter 1 should be modified");

        // Set modifiesAnything
        info.setModifiesAnything();

        // With modifiesAnything
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Escaped parameter 0 should be modified with modifiesAnything");
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 1),
                "Explicitly modified parameter 1 should still be modified");
    }

    /**
     * Tests isParameterModified priority of side effect flags.
     * hasNoSideEffects takes precedence over everything.
     */
    @Test
    public void testIsParameterModified_sideEffectFlagsPriority() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/Object;Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameters as modified
        info.setParameterModified(0);
        info.setParameterModified(1);

        // Verify both are modified
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0));
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 1));

        // Set hasNoSideEffects (highest priority)
        info.setNoSideEffects();

        // Both should now be false
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 should not be modified (hasNoSideEffects takes precedence)");
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 1),
                "Parameter 1 should not be modified (hasNoSideEffects takes precedence)");
    }

    /**
     * Tests isParameterModified with method having no parameters (aside from 'this').
     */
    @Test
    public void testIsParameterModified_withNoExplicitParameters_worksForThis() {
        // Arrange - create method with no explicit parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark parameter 0 ('this')
        info.setParameterModified(0);

        // Act & Assert
        assertTrue(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 ('this') should be modified");
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

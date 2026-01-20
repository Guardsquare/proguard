package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParameterEscapeMarker#modifiesAnything(Method)}.
 *
 * The modifiesAnything static method checks whether a method might modify objects
 * that it can reach through static fields or instance fields. It delegates to
 * MethodOptimizationInfo.getMethodOptimizationInfo(method).modifiesAnything().
 *
 * A method "modifies anything" when it can modify any objects on the heap that are
 * reachable through static or instance fields. This is a broader concept than just
 * modifying parameters - it means the method can have global side effects.
 *
 * Examples:
 * - System.setProperty() - modifies anything (writes to static fields)
 * - List.add() - modifies anything (modifies heap objects)
 * - StringBuilder.toString() - does NOT modify anything (creates new object but doesn't modify heap)
 * - Math.max() - does NOT modify anything (pure function)
 *
 * The logic involves the hasNoExternalSideEffects flag:
 * - If hasNoExternalSideEffects is true, modifiesAnything returns false
 * - Otherwise, for MethodOptimizationInfo: returns true (conservative)
 * - For ProgramMethodOptimizationInfo: returns the modifiesAnything flag value
 *
 * Default behavior (MethodOptimizationInfo): Returns true (conservative assumption).
 * With ProgramMethodOptimizationInfo: Returns false initially, true after setModifiesAnything().
 * The setNoExternalSideEffects() method takes precedence and forces it to return false.
 */
public class ParameterEscapeMarkerClaude_modifiesAnythingTest {

    private ProgramClass testClass;

    @BeforeEach
    public void setUp() {
        testClass = createProgramClassWithConstantPool();
    }

    /**
     * Tests modifiesAnything returns true by default with MethodOptimizationInfo.
     * Without detailed analysis, the method conservatively assumes anything might be modified.
     */
    @Test
    public void testModifiesAnything_withDefaultMethodOptimizationInfo_returnsTrue() {
        // Arrange - create method with default MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, method);

        // Act
        boolean result = ParameterEscapeMarker.modifiesAnything(method);

        // Assert - should return true by default (conservative assumption)
        assertTrue(result, "Should return true by default (conservative assumption)");
    }

    /**
     * Tests modifiesAnything with ProgramMethodOptimizationInfo initially returns false.
     * Before marking, the method should not be considered to modify anything.
     */
    @Test
    public void testModifiesAnything_withProgramMethodOptimizationInfo_initiallyReturnsFalse() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act
        boolean result = ParameterEscapeMarker.modifiesAnything(method);

        // Assert - should return false initially
        assertFalse(result, "Should return false initially with ProgramMethodOptimizationInfo");
    }

    /**
     * Tests modifiesAnything returns true after calling setModifiesAnything.
     */
    @Test
    public void testModifiesAnything_afterSetModifiesAnything_returnsTrue() {
        // Arrange - create method and mark it as modifying anything
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially false
        assertFalse(ParameterEscapeMarker.modifiesAnything(method),
                "Should be false before marking");

        // Mark as modifying anything
        info.setModifiesAnything();

        // Act
        boolean result = ParameterEscapeMarker.modifiesAnything(method);

        // Assert - should now return true
        assertTrue(result, "Should return true after setModifiesAnything");
    }

    /**
     * Tests modifiesAnything returns false after calling setNoExternalSideEffects.
     * setNoExternalSideEffects takes precedence over setModifiesAnything.
     */
    @Test
    public void testModifiesAnything_afterSetNoExternalSideEffects_returnsFalse() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as having no external side effects
        info.setNoExternalSideEffects();

        // Act
        boolean result = ParameterEscapeMarker.modifiesAnything(method);

        // Assert - should return false
        assertFalse(result, "Should return false after setNoExternalSideEffects");
    }

    /**
     * Tests that setNoExternalSideEffects takes precedence over setModifiesAnything.
     */
    @Test
    public void testModifiesAnything_setNoExternalSideEffectsTakesPrecedence() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // First mark as modifying anything
        info.setModifiesAnything();
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "Should be true after setModifiesAnything");

        // Then mark as having no external side effects (takes precedence)
        info.setNoExternalSideEffects();

        // Act
        boolean result = ParameterEscapeMarker.modifiesAnything(method);

        // Assert - should return false (setNoExternalSideEffects takes precedence)
        assertFalse(result, "Should return false (setNoExternalSideEffects takes precedence)");
    }

    /**
     * Tests that setNoExternalSideEffects takes precedence regardless of call order.
     */
    @Test
    public void testModifiesAnything_setNoExternalSideEffectsPrecedenceRegardlessOfOrder() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // First mark as having no external side effects
        info.setNoExternalSideEffects();

        // Then try to mark as modifying anything (should be overridden)
        info.setModifiesAnything();

        // Act
        boolean result = ParameterEscapeMarker.modifiesAnything(method);

        // Assert - should still return false (setNoExternalSideEffects takes precedence)
        assertFalse(result, "Should return false (setNoExternalSideEffects takes precedence regardless of order)");
    }

    /**
     * Tests modifiesAnything is consistent across multiple calls.
     */
    @Test
    public void testModifiesAnything_multipleCalls_returnsConsistentResults() {
        // Arrange - create method and mark it
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setModifiesAnything();

        // Act - call multiple times
        boolean firstCall = ParameterEscapeMarker.modifiesAnything(method);
        boolean secondCall = ParameterEscapeMarker.modifiesAnything(method);
        boolean thirdCall = ParameterEscapeMarker.modifiesAnything(method);

        // Assert - all calls should return the same result
        assertTrue(firstCall, "First call should return true");
        assertTrue(secondCall, "Second call should return true");
        assertTrue(thirdCall, "Third call should return true");
        assertEquals(firstCall, secondCall, "First and second calls should match");
        assertEquals(secondCall, thirdCall, "Second and third calls should match");
    }

    /**
     * Tests modifiesAnything with different methods independently.
     */
    @Test
    public void testModifiesAnything_withDifferentMethods_worksIndependently() {
        // Arrange - create three methods with different states
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);
        ProgramMethodOptimizationInfo info1 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method1);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);
        ProgramMethodOptimizationInfo info2 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method2);

        ProgramMethod method3 = createMethodWithDescriptor(testClass, "method3", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method3);
        ProgramMethodOptimizationInfo info3 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method3);

        // Mark method1 as modifying anything
        info1.setModifiesAnything();

        // Mark method2 with no external side effects
        info2.setNoExternalSideEffects();

        // Leave method3 unmarked

        // Act & Assert - methods should have independent flags
        assertTrue(ParameterEscapeMarker.modifiesAnything(method1),
                "Method1 should modify anything");
        assertFalse(ParameterEscapeMarker.modifiesAnything(method2),
                "Method2 should not modify anything");
        assertFalse(ParameterEscapeMarker.modifiesAnything(method3),
                "Method3 should be false initially");
    }

    /**
     * Tests modifiesAnything delegates correctly to MethodOptimizationInfo.
     */
    @Test
    public void testModifiesAnything_delegatesToMethodOptimizationInfo() {
        // Arrange - create method with custom MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo customInfo = new MethodOptimizationInfo();
        method.setProcessingInfo(customInfo);

        // Act
        boolean result = ParameterEscapeMarker.modifiesAnything(method);

        // Assert - should delegate to info's modifiesAnything
        // Default MethodOptimizationInfo returns true (hasNoExternalSideEffects is false initially)
        assertTrue(result, "Should delegate to MethodOptimizationInfo.modifiesAnything");
    }

    /**
     * Tests modifiesAnything after setNoExternalSideEffects on MethodOptimizationInfo.
     */
    @Test
    public void testModifiesAnything_withMethodOptimizationInfoSetNoExternal_returnsFalse() {
        // Arrange - create method with MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo info = new MethodOptimizationInfo();
        method.setProcessingInfo(info);

        // Mark as having no external side effects
        info.setNoExternalSideEffects();

        // Act
        boolean result = ParameterEscapeMarker.modifiesAnything(method);

        // Assert - should return false
        assertFalse(result, "Should return false after setNoExternalSideEffects");
    }

    /**
     * Tests modifiesAnything with static method.
     */
    @Test
    public void testModifiesAnything_withStaticMethod_worksCorrectly() {
        // Arrange - create static method
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "staticMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially false
        assertFalse(ParameterEscapeMarker.modifiesAnything(method),
                "Static method should be false initially");

        // Mark as modifying anything
        info.setModifiesAnything();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "Static method should return true after marking");
    }

    /**
     * Tests modifiesAnything with void return type method.
     */
    @Test
    public void testModifiesAnything_withVoidReturnType_tracksCorrectly() {
        // Arrange - create method with void return type
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as modifying anything
        info.setModifiesAnything();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "Flag should be set for void return type");
    }

    /**
     * Tests modifiesAnything with method returning value.
     */
    @Test
    public void testModifiesAnything_withReturnValue_tracksCorrectly() {
        // Arrange - create method returning Object
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()Ljava/lang/Object;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as modifying anything
        info.setModifiesAnything();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "Flag should be set regardless of return type");
    }

    /**
     * Tests modifiesAnything with method that takes parameters.
     */
    @Test
    public void testModifiesAnything_withParameters_worksCorrectly() {
        // Arrange - create method with parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as modifying anything
        info.setModifiesAnything();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "Flag should be set regardless of parameters");
    }

    /**
     * Tests modifiesAnything preserves state after being set.
     */
    @Test
    public void testModifiesAnything_preservesStateAfterSetting_remainsTrue() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initial state
        assertFalse(ParameterEscapeMarker.modifiesAnything(method),
                "Should be false initially");

        // Set to true
        info.setModifiesAnything();
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "Should be true after setting");

        // Call set again (idempotent)
        info.setModifiesAnything();
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "Should remain true after setting again");
    }

    /**
     * Tests modifiesAnything state transitions.
     */
    @Test
    public void testModifiesAnything_stateTransitions_normalFlow() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // State 1: initially false
        assertFalse(ParameterEscapeMarker.modifiesAnything(method),
                "State 1: Should be false initially");

        // State 2: set to true
        info.setModifiesAnything();
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "State 2: Should be true after setModifiesAnything");

        // State 3: remains true (idempotent)
        info.setModifiesAnything();
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "State 3: Should remain true");
    }

    /**
     * Tests modifiesAnything with both flags set - setNoExternalSideEffects wins.
     */
    @Test
    public void testModifiesAnything_bothFlagsSet_noExternalWins() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Set both flags
        info.setModifiesAnything();
        info.setNoExternalSideEffects();

        // Act
        boolean result = ParameterEscapeMarker.modifiesAnything(method);

        // Assert - setNoExternalSideEffects takes precedence
        assertFalse(result, "Should return false when both flags set (setNoExternalSideEffects wins)");
    }

    /**
     * Tests modifiesAnything with multiple method scenarios simultaneously.
     */
    @Test
    public void testModifiesAnything_multipleMethodsSimultaneously_maintainIndependentState() {
        // Arrange - create four methods with different states
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);
        ProgramMethodOptimizationInfo info2 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method2);
        info2.setModifiesAnything();

        ProgramMethod method3 = createMethodWithDescriptor(testClass, "method3", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method3);
        ProgramMethodOptimizationInfo info3 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method3);
        info3.setNoExternalSideEffects();

        ProgramMethod method4 = createMethodWithDescriptor(testClass, "method4", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method4);
        ProgramMethodOptimizationInfo info4 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method4);
        info4.setModifiesAnything();
        info4.setNoExternalSideEffects();

        // Act & Assert - each method should have independent state
        assertFalse(ParameterEscapeMarker.modifiesAnything(method1),
                "Method1 should be false (unmarked)");
        assertTrue(ParameterEscapeMarker.modifiesAnything(method2),
                "Method2 should be true (setModifiesAnything)");
        assertFalse(ParameterEscapeMarker.modifiesAnything(method3),
                "Method3 should be false (setNoExternalSideEffects)");
        assertFalse(ParameterEscapeMarker.modifiesAnything(method4),
                "Method4 should be false (both set, no-external wins)");
    }

    /**
     * Tests modifiesAnything with complex descriptor.
     */
    @Test
    public void testModifiesAnything_withComplexDescriptor_worksCorrectly() {
        // Arrange - create method with complex descriptor
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;[ILjava/util/Map;)Ljava/util/List;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as modifying anything
        info.setModifiesAnything();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "Should work correctly with complex descriptor");
    }

    /**
     * Tests modifiesAnything is a boolean flag controlled by two setters.
     */
    @Test
    public void testModifiesAnything_twoSeparateFlags_interactionPattern() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially both flags are false
        // modifiesAnything() returns: !hasNoExternalSideEffects && modifiesAnything
        // So: !false && false = false
        assertFalse(ParameterEscapeMarker.modifiesAnything(method),
                "Initially: !false && false = false");

        // Set modifiesAnything = true
        // modifiesAnything() returns: !false && true = true
        info.setModifiesAnything();
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "After setModifiesAnything: !false && true = true");

        // Set hasNoExternalSideEffects = true (via setNoExternalSideEffects)
        // modifiesAnything() returns: !true && true = false
        info.setNoExternalSideEffects();
        assertFalse(ParameterEscapeMarker.modifiesAnything(method),
                "After setNoExternalSideEffects: !true && true = false");
    }

    /**
     * Tests modifiesAnything consistency with MethodOptimizationInfo vs ProgramMethodOptimizationInfo.
     */
    @Test
    public void testModifiesAnything_comparesDefaultVsProgram() {
        // Arrange - create two methods with different info types
        ProgramMethod methodDefault = createMethodWithDescriptor(testClass, "methodDefault", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, methodDefault);

        ProgramMethod methodProgram = createMethodWithDescriptor(testClass, "methodProgram", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, methodProgram);

        // Act
        boolean defaultResult = ParameterEscapeMarker.modifiesAnything(methodDefault);
        boolean programResult = ParameterEscapeMarker.modifiesAnything(methodProgram);

        // Assert - default is conservative (true), program is precise (false initially)
        assertTrue(defaultResult, "Default MethodOptimizationInfo should return true (conservative)");
        assertFalse(programResult, "ProgramMethodOptimizationInfo should return false initially (precise)");
    }

    /**
     * Tests modifiesAnything with newly created ProgramMethodOptimizationInfo instances.
     */
    @Test
    public void testModifiesAnything_newlyCreatedInfo_startsWithFalse() {
        // Test with multiple newly created methods
        for (int i = 0; i < 5; i++) {
            ProgramMethod method = createMethodWithDescriptor(testClass, "method" + i, "(Ljava/lang/Object;)V");
            ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

            // Each new method should start with false
            assertFalse(ParameterEscapeMarker.modifiesAnything(method),
                    "Method" + i + " should start with false");
        }
    }

    /**
     * Tests modifiesAnything with constructor.
     */
    @Test
    public void testModifiesAnything_withConstructor_tracksCorrectly() {
        // Arrange - create constructor (special name <init>)
        ProgramMethod method = createMethodWithDescriptor(testClass, "<init>", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially false
        assertFalse(ParameterEscapeMarker.modifiesAnything(method),
                "Constructor should be false initially");

        // Mark as modifying anything
        info.setModifiesAnything();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "Constructor can have flag set");
    }

    /**
     * Tests modifiesAnything with abstract method.
     */
    @Test
    public void testModifiesAnything_withAbstractMethod_tracksCorrectly() {
        // Arrange - create abstract method
        ProgramMethod method = createAbstractMethodWithDescriptor(testClass, "abstractMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Initially false
        assertFalse(ParameterEscapeMarker.modifiesAnything(method),
                "Abstract method should be false initially");

        // Mark as modifying anything
        info.setModifiesAnything();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "Abstract method can have flag set");
    }

    /**
     * Tests modifiesAnything relationship with setNoSideEffects.
     * While setNoSideEffects implies setNoExternalSideEffects, we test the direct relationship.
     */
    @Test
    public void testModifiesAnything_afterSetNoSideEffects_returnsFalse() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as modifying anything
        info.setModifiesAnything();
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "Should be true after setModifiesAnything");

        // Set no side effects (implies no external side effects)
        info.setNoSideEffects();

        // Act & Assert
        assertFalse(ParameterEscapeMarker.modifiesAnything(method),
                "Should return false after setNoSideEffects (implies no external side effects)");
    }

    /**
     * Tests modifiesAnything with method having no parameters.
     */
    @Test
    public void testModifiesAnything_withNoParameters_worksCorrectly() {
        // Arrange - create method with no parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as modifying anything
        info.setModifiesAnything();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "Method with no parameters can still modify anything (e.g., static fields)");
    }

    /**
     * Tests modifiesAnything reflects the broader scope than parameter modification.
     * A method can modify anything even without modifying parameters.
     */
    @Test
    public void testModifiesAnything_broaderThanParameterModification() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

        // Mark as modifying anything (but don't mark any parameters as modified)
        info.setModifiesAnything();

        // Act & Assert
        assertTrue(ParameterEscapeMarker.modifiesAnything(method),
                "Can modify anything (e.g., static/global state) without modifying parameters");
        assertFalse(ParameterEscapeMarker.isParameterModified(method, 0),
                "Parameter 0 is not marked as modified");
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

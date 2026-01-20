package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.SwitchInstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SimpleEnumUseChecker#visitAnySwitchInstruction(Clazz, Method, CodeAttribute, int, SwitchInstruction)}.
 *
 * The visitAnySwitchInstruction method is an empty implementation (no-op) that serves as a default
 * handler in the InstructionVisitor pattern for switch instructions (tableswitch, lookupswitch) that
 * don't require specialized processing in this checker.
 */
public class SimpleEnumUseCheckerClaude_visitAnySwitchInstructionTest {

    private SimpleEnumUseChecker checker;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;
    private SwitchInstruction switchInstruction;

    @BeforeEach
    public void setUp() {
        checker = new SimpleEnumUseChecker();
        clazz = mock(ProgramClass.class);
        method = mock(Method.class);
        codeAttribute = mock(CodeAttribute.class);
        switchInstruction = mock(SwitchInstruction.class);
    }

    /**
     * Tests that visitAnySwitchInstruction can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnySwitchInstruction_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction));
    }

    /**
     * Tests that visitAnySwitchInstruction can be called with null clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnySwitchInstruction_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnySwitchInstruction(null, method, codeAttribute, 0, switchInstruction));
    }

    /**
     * Tests that visitAnySwitchInstruction can be called with null method parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnySwitchInstruction_withNullMethod_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnySwitchInstruction(clazz, null, codeAttribute, 0, switchInstruction));
    }

    /**
     * Tests that visitAnySwitchInstruction can be called with null codeAttribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnySwitchInstruction_withNullCodeAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnySwitchInstruction(clazz, method, null, 0, switchInstruction));
    }

    /**
     * Tests that visitAnySwitchInstruction can be called with null switchInstruction parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnySwitchInstruction_withNullSwitchInstruction_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, null));
    }

    /**
     * Tests that visitAnySwitchInstruction can be called with all null parameters.
     * The method should handle all nulls gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnySwitchInstruction_withAllNullParameters_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitAnySwitchInstruction(null, null, null, 0, null));
    }

    /**
     * Tests that visitAnySwitchInstruction can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnySwitchInstruction_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
        });
    }

    /**
     * Tests that visitAnySwitchInstruction doesn't interact with any of the parameters.
     * Since it's a no-op method, it should not call any methods on the parameters.
     */
    @Test
    public void testVisitAnySwitchInstruction_doesNotInteractWithParameters() {
        // Act
        checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);

        // Assert - verify no interactions occurred with any of the mocks
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        verifyNoInteractions(switchInstruction);
    }

    /**
     * Tests that visitAnySwitchInstruction works with different offset values.
     * Verifies the method works regardless of the offset parameter.
     */
    @Test
    public void testVisitAnySwitchInstruction_withDifferentOffsets_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 10, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 100, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, -1, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, Integer.MAX_VALUE, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, Integer.MIN_VALUE, switchInstruction);
        });
    }

    /**
     * Tests that visitAnySwitchInstruction can be called rapidly in succession.
     * Verifies consistent behavior under stress.
     */
    @Test
    public void testVisitAnySwitchInstruction_rapidSequentialCalls_doesNotThrowException() {
        // Act & Assert - all calls should succeed without exceptions
        for (int i = 0; i < 1000; i++) {
            int offset = i;
            assertDoesNotThrow(() -> checker.visitAnySwitchInstruction(clazz, method, codeAttribute, offset, switchInstruction),
                    "Call " + i + " should not throw exception");
        }
    }

    /**
     * Tests that multiple SimpleEnumUseChecker instances can all call visitAnySwitchInstruction
     * on the same parameters without interference.
     */
    @Test
    public void testVisitAnySwitchInstruction_multipleCheckersOneParameter_allWorkCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker3 = new SimpleEnumUseChecker();

        // Act & Assert
        assertDoesNotThrow(() -> {
            checker1.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
            checker2.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
            checker3.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
        });

        // Verify no interactions from any of the calls
        verifyNoInteractions(clazz, method, codeAttribute, switchInstruction);
    }

    /**
     * Tests that visitAnySwitchInstruction is truly a no-op by verifying no exceptions
     * even with parameters that would normally cause issues if accessed.
     */
    @Test
    public void testVisitAnySwitchInstruction_isNoop_noExceptionsWithAnyInput() {
        // Act & Assert - should not throw even with unusual combinations
        assertDoesNotThrow(() -> {
            checker.visitAnySwitchInstruction(null, null, null, 0, null);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, null, null, -1, null);
            checker.visitAnySwitchInstruction(null, method, codeAttribute, 100, switchInstruction);
        });
    }

    /**
     * Tests that visitAnySwitchInstruction can be used as part of the InstructionVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitAnySwitchInstruction_usedAsInstructionVisitor_worksCorrectly() {
        // Arrange
        proguard.classfile.instruction.visitor.InstructionVisitor visitor = checker;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction));
        verifyNoInteractions(clazz, method, codeAttribute, switchInstruction);
    }

    /**
     * Tests that visitAnySwitchInstruction maintains thread-safe behavior as a no-op.
     * Verifies the method can be called rapidly without issues.
     */
    @Test
    public void testVisitAnySwitchInstruction_rapidCalls_doesNotThrowException() {
        // Act & Assert - rapid calls should all succeed
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                checker.visitAnySwitchInstruction(clazz, method, codeAttribute, i, switchInstruction);
            }
        });
    }

    /**
     * Tests that visitAnySwitchInstruction has no effect on the SimpleEnumUseChecker's internal state.
     * Verifies that subsequent calls to visitAnySwitchInstruction behave identically.
     */
    @Test
    public void testVisitAnySwitchInstruction_repeatedCalls_behaviorRemainsConsistent() {
        // Act & Assert - multiple calls should have identical behavior
        assertDoesNotThrow(() -> checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction));
        assertDoesNotThrow(() -> checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction));
        assertDoesNotThrow(() -> checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction));

        // Verify no interactions still occur after multiple calls
        verifyNoInteractions(clazz, method, codeAttribute, switchInstruction);
    }

    /**
     * Tests that visitAnySwitchInstruction can be interleaved with null and non-null parameters.
     * Verifies that the no-op method doesn't interfere with normal operation.
     */
    @Test
    public void testVisitAnySwitchInstruction_interleavedCalls_doesNotThrowException() {
        // Act & Assert - interleaved calls should all succeed
        assertDoesNotThrow(() -> {
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
            checker.visitAnySwitchInstruction(null, null, null, 0, null);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 10, switchInstruction);
            checker.visitAnySwitchInstruction(null, method, null, 20, switchInstruction);
        });
    }

    /**
     * Tests that visitAnySwitchInstruction works correctly when called with the same parameters repeatedly.
     * Verifies stable behavior with parameter reuse.
     */
    @Test
    public void testVisitAnySwitchInstruction_sameParametersRepeatedCalls_consistentBehavior() {
        // Act & Assert - multiple calls with same parameters should all succeed
        for (int i = 0; i < 50; i++) {
            assertDoesNotThrow(() -> checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 42, switchInstruction),
                    "Repeated call " + i + " with same parameters should not throw");
        }

        // Verify still no interactions after many calls
        verifyNoInteractions(clazz, method, codeAttribute, switchInstruction);
    }

    /**
     * Tests that visitAnySwitchInstruction preserves the checker's ability to work with other visitor methods.
     * Verifies that calling visitAnySwitchInstruction doesn't affect the checker's state for other operations.
     */
    @Test
    public void testVisitAnySwitchInstruction_doesNotAffectOtherVisitorMethods() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - call visitAnySwitchInstruction multiple times
        checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
        checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 1, switchInstruction);

        // Assert - other visitor methods should still work
        assertDoesNotThrow(() -> checker.visitAnyClass(programClass),
                "Other visitor methods should still work after visitAnySwitchInstruction");
        assertDoesNotThrow(() -> checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 2, switchInstruction),
                "visitAnySwitchInstruction should still work after multiple calls");
    }

    /**
     * Tests that visitAnySwitchInstruction works correctly in a sequence of different visitor method calls.
     * Verifies the no-op doesn't affect other visitor patterns.
     */
    @Test
    public void testVisitAnySwitchInstruction_inVisitorSequence_doesNotThrowException() {
        // Arrange
        ProgramClass realClass = new ProgramClass();

        // Act & Assert - should work in a sequence of visitor calls
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(realClass);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
            checker.visitAnyClass(realClass);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 10, switchInstruction);
        });
    }

    /**
     * Tests that visitAnySwitchInstruction maintains expected behavior across varied call patterns.
     * Verifies consistency regardless of call order or parameter variation.
     */
    @Test
    public void testVisitAnySwitchInstruction_variedCallPatterns_allWorkCorrectly() {
        // Arrange
        SwitchInstruction switch1 = mock(SwitchInstruction.class);
        SwitchInstruction switch2 = mock(SwitchInstruction.class);
        Method method2 = mock(Method.class);

        // Act & Assert - various call patterns should all work
        assertDoesNotThrow(() -> {
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 10, switch1);
            checker.visitAnySwitchInstruction(clazz, method2, codeAttribute, 20, switch2);
            checker.visitAnySwitchInstruction(null, null, null, 30, null);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 40, switchInstruction);
        });
    }

    /**
     * Tests that visitAnySwitchInstruction doesn't retain references to any parameters.
     * Verifies no memory leaks or reference retention.
     */
    @Test
    public void testVisitAnySwitchInstruction_doesNotRetainReferences() {
        // Arrange
        SwitchInstruction tempSwitch = mock(SwitchInstruction.class);

        // Act
        checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, tempSwitch);

        // Assert - no interactions means no references should be retained
        verifyNoInteractions(tempSwitch);
    }

    /**
     * Tests that visitAnySwitchInstruction doesn't call any methods on any parameter when given mocks.
     * Confirms complete isolation and no-op behavior.
     */
    @Test
    public void testVisitAnySwitchInstruction_withMocks_noMethodsCalled() {
        // Arrange - create strict mocks
        Clazz mockClazz = mock(Clazz.class, withSettings().strictness(org.mockito.quality.Strictness.STRICT_STUBS));
        Method mockMethod = mock(Method.class, withSettings().strictness(org.mockito.quality.Strictness.STRICT_STUBS));
        CodeAttribute mockCode = mock(CodeAttribute.class, withSettings().strictness(org.mockito.quality.Strictness.STRICT_STUBS));
        SwitchInstruction mockSwitch = mock(SwitchInstruction.class, withSettings().strictness(org.mockito.quality.Strictness.STRICT_STUBS));

        // Act
        checker.visitAnySwitchInstruction(mockClazz, mockMethod, mockCode, 0, mockSwitch);

        // Assert - verify absolutely no interactions
        verifyNoInteractions(mockClazz, mockMethod, mockCode, mockSwitch);
    }

    /**
     * Tests that the checker created with a custom PartialEvaluator also has a no-op visitAnySwitchInstruction.
     * Verifies consistency across different constructor configurations.
     */
    @Test
    public void testVisitAnySwitchInstruction_withCustomPartialEvaluator_doesNotThrowException() {
        // Arrange
        proguard.evaluation.PartialEvaluator evaluator =
            proguard.evaluation.PartialEvaluator.Builder.create().build();
        SimpleEnumUseChecker customChecker = new SimpleEnumUseChecker(evaluator);

        // Act & Assert
        assertDoesNotThrow(() -> {
            customChecker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
            customChecker.visitAnySwitchInstruction(null, null, null, 0, null);
        });

        verifyNoInteractions(clazz, method, codeAttribute, switchInstruction);
    }

    /**
     * Tests that visitAnySwitchInstruction can be called an extreme number of times without issues.
     * Verifies the no-op implementation doesn't accumulate any state or resources.
     */
    @Test
    public void testVisitAnySwitchInstruction_extremeNumberOfCalls_doesNotThrowException() {
        // Act & Assert - should handle many calls without issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10000; i++) {
                checker.visitAnySwitchInstruction(clazz, method, codeAttribute, i, switchInstruction);
            }
        });

        // After many calls, still no interactions
        verifyNoInteractions(clazz, method, codeAttribute, switchInstruction);
    }

    /**
     * Tests that visitAnySwitchInstruction through InstructionVisitor interface works with casting.
     * Verifies proper interface implementation.
     */
    @Test
    public void testVisitAnySwitchInstruction_throughCastedInterface_doesNotThrowException() {
        // Arrange
        proguard.classfile.instruction.visitor.InstructionVisitor visitor =
            (proguard.classfile.instruction.visitor.InstructionVisitor) checker;

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
            visitor.visitAnySwitchInstruction(null, null, null, 0, null);
        });
    }

    /**
     * Tests that visitAnySwitchInstruction works with different combinations of null and non-null parameters.
     * Verifies robust handling of various parameter states.
     */
    @Test
    public void testVisitAnySwitchInstruction_mixedNullAndNonNull_doesNotThrowException() {
        // Act & Assert - should handle all combinations gracefully
        assertDoesNotThrow(() -> {
            checker.visitAnySwitchInstruction(clazz, null, null, 0, null);
            checker.visitAnySwitchInstruction(null, method, null, 10, null);
            checker.visitAnySwitchInstruction(null, null, codeAttribute, 20, null);
            checker.visitAnySwitchInstruction(null, null, null, 30, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, method, null, 40, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, null, codeAttribute, 50, switchInstruction);
            checker.visitAnySwitchInstruction(null, method, codeAttribute, 60, switchInstruction);
        });
    }

    /**
     * Tests that multiple checker instances can call visitAnySwitchInstruction independently
     * without interfering with each other.
     */
    @Test
    public void testVisitAnySwitchInstruction_multipleInstancesIndependent_worksCorrectly() {
        // Arrange
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        SwitchInstruction switch1 = mock(SwitchInstruction.class);
        SwitchInstruction switch2 = mock(SwitchInstruction.class);

        // Act
        checker1.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switch1);
        checker2.visitAnySwitchInstruction(clazz, method, codeAttribute, 10, switch2);

        // Assert - verify each checker called with respective switch instructions
        verifyNoInteractions(switch1, switch2);
    }

    /**
     * Tests that visitAnySwitchInstruction maintains the visitor pattern contract.
     * The method should always complete successfully regardless of parameters.
     */
    @Test
    public void testVisitAnySwitchInstruction_visitorPatternContract_alwaysSucceeds() {
        // Arrange
        Object[][] parameterVariations = {
            {clazz, method, codeAttribute, 0, switchInstruction},
            {null, null, null, 0, null},
            {clazz, null, null, 10, null},
            {null, method, codeAttribute, 20, switchInstruction},
            {clazz, method, null, 30, switchInstruction},
        };

        // Act - call with different parameter combinations
        for (Object[] params : parameterVariations) {
            assertDoesNotThrow(() -> checker.visitAnySwitchInstruction(
                (Clazz) params[0],
                (Method) params[1],
                (CodeAttribute) params[2],
                (int) params[3],
                (SwitchInstruction) params[4]
            ));
        }
    }

    /**
     * Tests that visitAnySwitchInstruction behavior remains consistent after being called
     * with null parameters.
     */
    @Test
    public void testVisitAnySwitchInstruction_afterNullParameters_behaviorRemainsConsistent() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            checker.visitAnySwitchInstruction(null, null, null, 0, null);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
            checker.visitAnySwitchInstruction(null, null, null, 0, null);
        });

        // Verify no interactions occurred
        verifyNoInteractions(clazz, method, codeAttribute, switchInstruction);
    }

    /**
     * Tests that visitAnySwitchInstruction can be called as part of a complex visitor workflow.
     */
    @Test
    public void testVisitAnySwitchInstruction_inComplexWorkflow_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        SwitchInstruction switch1 = mock(SwitchInstruction.class);
        SwitchInstruction switch2 = mock(SwitchInstruction.class);

        // Act & Assert - simulate a complex visitor workflow
        assertDoesNotThrow(() -> {
            checker.visitAnyClass(programClass);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switch1);
            checker.visitAnyClass(programClass);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 10, switch2);
            checker.visitAnyClass(programClass);
        });

        // Verify no interactions with switch instructions
        verifyNoInteractions(switch1, switch2);
    }

    /**
     * Tests that visitAnySwitchInstruction handles boundary values for offset parameter correctly.
     * Verifies the method works with extreme offset values.
     */
    @Test
    public void testVisitAnySwitchInstruction_withBoundaryOffsetValues_doesNotThrowException() {
        // Act & Assert - should handle boundary values
        assertDoesNotThrow(() -> {
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 1, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, -1, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, Integer.MAX_VALUE, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, Integer.MIN_VALUE, switchInstruction);
        });

        verifyNoInteractions(clazz, method, codeAttribute, switchInstruction);
    }

    /**
     * Tests that visitAnySwitchInstruction works with different switch instruction instances.
     * Verifies the method can handle multiple different switch instructions.
     */
    @Test
    public void testVisitAnySwitchInstruction_withDifferentSwitchInstructions_worksCorrectly() {
        // Arrange
        SwitchInstruction switch1 = mock(SwitchInstruction.class);
        SwitchInstruction switch2 = mock(SwitchInstruction.class);
        SwitchInstruction switch3 = mock(SwitchInstruction.class);

        // Act
        checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 0, switch1);
        checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 10, switch2);
        checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 20, switch3);

        // Assert - verify no interactions with any switch instruction
        verifyNoInteractions(switch1, switch2, switch3);
    }

    /**
     * Tests that visitAnySwitchInstruction is a proper no-op that doesn't access any properties.
     * Even uninitialized or null parameters should work fine.
     */
    @Test
    public void testVisitAnySwitchInstruction_asProperNoop_worksWithAnyParameters() {
        // Act & Assert - should work as a proper no-op
        assertDoesNotThrow(() -> {
            checker.visitAnySwitchInstruction(null, null, null, 0, null);
            checker.visitAnySwitchInstruction(clazz, method, codeAttribute, 100, switchInstruction);
            checker.visitAnySwitchInstruction(clazz, null, codeAttribute, -50, null);
        });
    }
}

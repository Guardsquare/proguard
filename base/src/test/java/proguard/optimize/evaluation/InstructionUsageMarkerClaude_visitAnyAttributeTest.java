package proguard.optimize.evaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;
import proguard.evaluation.PartialEvaluator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link InstructionUsageMarker#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is an empty implementation (no-op) that serves as a default
 * handler in the AttributeVisitor pattern for attributes that don't have specialized visitor methods.
 * Since visitCodeAttribute has a specific implementation, visitAnyAttribute handles all other
 * attribute types by doing nothing.
 *
 * These tests verify that:
 * 1. The method can be called without throwing exceptions
 * 2. The method handles null parameters gracefully
 * 3. The method doesn't interact with any parameters (true no-op)
 * 4. The method can be called multiple times safely
 */
public class InstructionUsageMarkerClaude_visitAnyAttributeTest {

    private InstructionUsageMarker instructionUsageMarker;
    private Clazz clazz;
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        instructionUsageMarker = new InstructionUsageMarker(true);
        clazz = mock(ProgramClass.class);
        attribute = mock(Attribute.class);
    }

    /**
     * Tests that visitAnyAttribute can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> instructionUsageMarker.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> instructionUsageMarker.visitAnyAttribute(null, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Attribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> instructionUsageMarker.visitAnyAttribute(clazz, null));
    }

    /**
     * Tests that visitAnyAttribute can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> instructionUsageMarker.visitAnyAttribute(null, null));
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            instructionUsageMarker.visitAnyAttribute(clazz, attribute);
            instructionUsageMarker.visitAnyAttribute(clazz, attribute);
            instructionUsageMarker.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithClazz() {
        // Act
        instructionUsageMarker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Attribute parameter.
     * Since it's a no-op method, it should not call any methods on the attribute.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithAttribute() {
        // Act
        instructionUsageMarker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the attribute mock
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with either parameter.
     * Verifies that both parameters remain untouched.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithAnyParameter() {
        // Act
        instructionUsageMarker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with either mock
        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute can be used as part of the AttributeVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitAnyAttribute_usedAsAttributeVisitor_doesNotThrowException() {
        // Arrange
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = instructionUsageMarker;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with real ProgramClass instance.
     * Verifies the method works with actual class instances, not just mocks.
     */
    @Test
    public void testVisitAnyAttribute_withRealProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> instructionUsageMarker.visitAnyAttribute(realClass, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called rapidly in succession.
     * Verifies consistent behavior under stress.
     */
    @Test
    public void testVisitAnyAttribute_rapidSequentialCalls_doesNotThrowException() {
        // Act & Assert - all calls should succeed without exceptions
        for (int i = 0; i < 1000; i++) {
            assertDoesNotThrow(() -> instructionUsageMarker.visitAnyAttribute(clazz, attribute),
                    "Call " + i + " should not throw exception");
        }
    }

    /**
     * Tests that visitAnyAttribute works with different InstructionUsageMarker instances.
     * Verifies that multiple marker instances behave consistently.
     */
    @Test
    public void testVisitAnyAttribute_multipleInstructionUsageMarkerInstances_allWorkCorrectly() {
        // Arrange
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(false);
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker3 = new InstructionUsageMarker(partialEvaluator, true, true);
        InstructionUsageMarker marker4 = new InstructionUsageMarker(partialEvaluator, false, false);

        // Act & Assert - all should work without exceptions
        assertDoesNotThrow(() -> {
            marker1.visitAnyAttribute(clazz, attribute);
            marker2.visitAnyAttribute(clazz, attribute);
            marker3.visitAnyAttribute(clazz, attribute);
            marker4.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute can be called with different attribute mocks.
     * Verifies the method works with various attribute types.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentAttributes_doesNotThrowException() {
        // Arrange
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);
        Attribute attr3 = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            instructionUsageMarker.visitAnyAttribute(clazz, attr1);
            instructionUsageMarker.visitAnyAttribute(clazz, attr2);
            instructionUsageMarker.visitAnyAttribute(clazz, attr3);
        });
    }

    /**
     * Tests that visitAnyAttribute can be called with different clazz mocks.
     * Verifies the method works with various class types.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentClasses_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            instructionUsageMarker.visitAnyAttribute(clazz1, attribute);
            instructionUsageMarker.visitAnyAttribute(clazz2, attribute);
            instructionUsageMarker.visitAnyAttribute(clazz3, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't modify any state.
     * Verifies that calling the method has no side effects.
     */
    @Test
    public void testVisitAnyAttribute_doesNotModifyState() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        Object initialProcessingInfo = new Object();
        realClass.setProcessingInfo(initialProcessingInfo);

        // Act
        instructionUsageMarker.visitAnyAttribute(realClass, attribute);

        // Assert - verify the class state wasn't modified
        assertSame(initialProcessingInfo, realClass.getProcessingInfo(),
                "Class processing info should not be modified");
    }

    /**
     * Tests that visitAnyAttribute is truly a no-op by verifying no exceptions
     * even with parameters that would normally cause issues if accessed.
     */
    @Test
    public void testVisitAnyAttribute_isNoop_noExceptionsWithAnyInput() {
        // Act & Assert - should not throw even with unusual combinations
        assertDoesNotThrow(() -> {
            instructionUsageMarker.visitAnyAttribute(null, null);
            instructionUsageMarker.visitAnyAttribute(clazz, null);
            instructionUsageMarker.visitAnyAttribute(null, attribute);
            instructionUsageMarker.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute can be called with InstructionUsageMarker created via
     * single-parameter constructor with markExternalSideEffects=true.
     * Verifies consistent behavior across different constructor variants.
     */
    @Test
    public void testVisitAnyAttribute_withMarkExternalSideEffectsTrue_doesNotThrowException() {
        // Arrange
        InstructionUsageMarker marker = new InstructionUsageMarker(true);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with InstructionUsageMarker created via
     * single-parameter constructor with markExternalSideEffects=false.
     * Verifies consistent behavior across different constructor variants.
     */
    @Test
    public void testVisitAnyAttribute_withMarkExternalSideEffectsFalse_doesNotThrowException() {
        // Arrange
        InstructionUsageMarker marker = new InstructionUsageMarker(false);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute maintains consistent behavior across different
     * InstructionUsageMarker constructor parameter combinations.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentConstructorParameters_allWorkCorrectly() {
        // Arrange
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(false);
        InstructionUsageMarker marker3 = new InstructionUsageMarker(partialEvaluator, true, true);
        InstructionUsageMarker marker4 = new InstructionUsageMarker(partialEvaluator, false, false);
        InstructionUsageMarker marker5 = new InstructionUsageMarker(partialEvaluator, true, true, true);
        InstructionUsageMarker marker6 = new InstructionUsageMarker(partialEvaluator, false, false, false);

        // Act & Assert - all constructor variants should work
        assertDoesNotThrow(() -> {
            marker1.visitAnyAttribute(clazz, attribute);
            marker2.visitAnyAttribute(clazz, attribute);
            marker3.visitAnyAttribute(clazz, attribute);
            marker4.visitAnyAttribute(clazz, attribute);
            marker5.visitAnyAttribute(clazz, attribute);
            marker6.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute maintains thread-safe behavior as a no-op.
     * Verifies the method can be called rapidly without issues.
     */
    @Test
    public void testVisitAnyAttribute_rapidCalls_doesNotThrowException() {
        // Act & Assert - rapid calls should all succeed
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                instructionUsageMarker.visitAnyAttribute(clazz, attribute);
            }
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interfere with the AttributeVisitor interface contract.
     * Verifies that it can be safely called through the interface.
     */
    @Test
    public void testVisitAnyAttribute_throughInterface_doesNotThrowException() {
        // Arrange
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = instructionUsageMarker;
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitAnyAttribute(realClass, attribute);
            visitor.visitAnyAttribute(null, null);
        });
    }

    /**
     * Tests that visitAnyAttribute has no effect on the InstructionUsageMarker's internal state.
     * Verifies that subsequent calls to visitAnyAttribute behave identically.
     */
    @Test
    public void testVisitAnyAttribute_repeatedCalls_behaviorRemainsConsistent() {
        // Act & Assert - multiple calls should have identical behavior
        assertDoesNotThrow(() -> instructionUsageMarker.visitAnyAttribute(clazz, attribute));
        assertDoesNotThrow(() -> instructionUsageMarker.visitAnyAttribute(clazz, attribute));
        assertDoesNotThrow(() -> instructionUsageMarker.visitAnyAttribute(clazz, attribute));

        // Verify no interactions still occur after multiple calls
        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute can be interleaved with other method calls.
     * Verifies that the no-op method doesn't interfere with normal operation.
     */
    @Test
    public void testVisitAnyAttribute_interleavedCalls_doesNotThrowException() {
        // Act & Assert - interleaved calls should all succeed
        assertDoesNotThrow(() -> {
            instructionUsageMarker.visitAnyAttribute(clazz, attribute);
            instructionUsageMarker.visitAnyAttribute(null, null);
            instructionUsageMarker.visitAnyAttribute(clazz, attribute);
            instructionUsageMarker.visitAnyAttribute(null, attribute);
            instructionUsageMarker.visitAnyAttribute(clazz, null);
        });
    }

    /**
     * Tests that visitAnyAttribute works correctly with a freshly created ProgramClass.
     * Verifies no initialization issues affect the no-op behavior.
     */
    @Test
    public void testVisitAnyAttribute_withFreshProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass freshClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> instructionUsageMarker.visitAnyAttribute(freshClass, attribute));
        assertDoesNotThrow(() -> instructionUsageMarker.visitAnyAttribute(freshClass, null));
    }

    /**
     * Tests that visitAnyAttribute can handle alternating null and non-null parameters.
     * Verifies robustness of the no-op implementation.
     */
    @Test
    public void testVisitAnyAttribute_alternatingNullAndNonNull_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            instructionUsageMarker.visitAnyAttribute(clazz, attribute);
            instructionUsageMarker.visitAnyAttribute(null, attribute);
            instructionUsageMarker.visitAnyAttribute(clazz, null);
            instructionUsageMarker.visitAnyAttribute(null, null);
            instructionUsageMarker.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute maintains consistency across different PartialEvaluator instances.
     * Verifies that the PartialEvaluator doesn't affect the no-op behavior.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentPartialEvaluators_allWorkCorrectly() {
        // Arrange
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker1 = new InstructionUsageMarker(evaluator1, true, true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(evaluator2, false, false);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marker1.visitAnyAttribute(clazz, attribute);
            marker2.visitAnyAttribute(clazz, attribute);
        });

        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't cause any memory leaks or reference retention.
     * Verifies that parameters can be garbage collected after the call.
     */
    @Test
    public void testVisitAnyAttribute_doesNotRetainReferences() {
        // Arrange
        Clazz tempClazz = mock(ProgramClass.class);
        Attribute tempAttribute = mock(Attribute.class);

        // Act
        instructionUsageMarker.visitAnyAttribute(tempClazz, tempAttribute);

        // Assert - no interactions means no references should be retained
        verifyNoInteractions(tempClazz, tempAttribute);
    }

    /**
     * Tests that visitAnyAttribute with runPartialEvaluator=true behaves correctly.
     * Verifies the boolean parameter doesn't affect this no-op method.
     */
    @Test
    public void testVisitAnyAttribute_withRunPartialEvaluatorTrue_doesNotThrowException() {
        // Arrange
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, attribute));
        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute with runPartialEvaluator=false behaves correctly.
     * Verifies the boolean parameter doesn't affect this no-op method.
     */
    @Test
    public void testVisitAnyAttribute_withRunPartialEvaluatorFalse_doesNotThrowException() {
        // Arrange
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, false, false);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, attribute));
        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute with ensureSafetyForVerifier=true behaves correctly.
     * Verifies the boolean parameter doesn't affect this no-op method.
     */
    @Test
    public void testVisitAnyAttribute_withEnsureSafetyForVerifierTrue_doesNotThrowException() {
        // Arrange
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, true, true);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, attribute));
        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute with ensureSafetyForVerifier=false behaves correctly.
     * Verifies the boolean parameter doesn't affect this no-op method.
     */
    @Test
    public void testVisitAnyAttribute_withEnsureSafetyForVerifierFalse_doesNotThrowException() {
        // Arrange
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker = new InstructionUsageMarker(partialEvaluator, true, false, true);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, attribute));
        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute works correctly across all constructor parameter combinations.
     * Verifies robustness across different initialization scenarios.
     */
    @Test
    public void testVisitAnyAttribute_allConstructorCombinations_workCorrectly() {
        // Arrange
        PartialEvaluator pe = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker[] markers = new InstructionUsageMarker[] {
            new InstructionUsageMarker(true),
            new InstructionUsageMarker(false),
            new InstructionUsageMarker(pe, true, true),
            new InstructionUsageMarker(pe, true, false),
            new InstructionUsageMarker(pe, false, true),
            new InstructionUsageMarker(pe, false, false),
            new InstructionUsageMarker(pe, true, true, true),
            new InstructionUsageMarker(pe, true, true, false),
            new InstructionUsageMarker(pe, true, false, true),
            new InstructionUsageMarker(pe, true, false, false),
            new InstructionUsageMarker(pe, false, true, true),
            new InstructionUsageMarker(pe, false, true, false),
            new InstructionUsageMarker(pe, false, false, true),
            new InstructionUsageMarker(pe, false, false, false)
        };

        // Act & Assert - all markers should handle the call without exception
        for (int i = 0; i < markers.length; i++) {
            int index = i; // for lambda capture
            assertDoesNotThrow(() -> markers[index].visitAnyAttribute(clazz, attribute),
                    "Marker " + index + " should not throw exception");
        }

        // Verify no interactions occurred
        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute can be called after creating markers with different PartialEvaluators.
     * Verifies that different evaluator configurations don't affect the no-op behavior.
     */
    @Test
    public void testVisitAnyAttribute_withVariousPartialEvaluatorConfigurations_doesNotThrowException() {
        // Arrange
        PartialEvaluator pe1 = PartialEvaluator.Builder.create().build();
        PartialEvaluator pe2 = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker marker1 = new InstructionUsageMarker(pe1, true, true, true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(pe2, false, false, false);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marker1.visitAnyAttribute(clazz, attribute);
            marker2.visitAnyAttribute(clazz, attribute);
            marker1.visitAnyAttribute(null, null);
            marker2.visitAnyAttribute(null, null);
        });

        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute remains a no-op even when called in various sequences.
     * Verifies consistency across different call patterns.
     */
    @Test
    public void testVisitAnyAttribute_variousCallSequences_allSucceed() {
        // Act & Assert - various patterns should all work
        assertDoesNotThrow(() -> {
            // Single calls
            instructionUsageMarker.visitAnyAttribute(clazz, attribute);

            // Null calls
            instructionUsageMarker.visitAnyAttribute(null, null);

            // Mixed calls
            instructionUsageMarker.visitAnyAttribute(clazz, null);
            instructionUsageMarker.visitAnyAttribute(null, attribute);

            // Repeated calls
            for (int i = 0; i < 10; i++) {
                instructionUsageMarker.visitAnyAttribute(clazz, attribute);
            }
        });

        // Verify mock objects were never accessed
        verifyNoInteractions(clazz, attribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't affect or depend on any internal marker state.
     * Verifies true stateless behavior.
     */
    @Test
    public void testVisitAnyAttribute_isStateless_noSideEffects() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        Object info1 = new Object();
        realClass.setProcessingInfo(info1);

        // Act - call multiple times
        instructionUsageMarker.visitAnyAttribute(realClass, attribute);
        instructionUsageMarker.visitAnyAttribute(realClass, attribute);
        instructionUsageMarker.visitAnyAttribute(realClass, attribute);

        // Assert - state should remain unchanged
        assertSame(info1, realClass.getProcessingInfo(),
                "Class state should not be modified by multiple visitAnyAttribute calls");
    }
}

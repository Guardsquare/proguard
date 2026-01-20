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
 * Test class for {@link EvaluationShrinker#visitAnyAttribute(Clazz, Attribute)}.
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
public class EvaluationShrinkerClaude_visitAnyAttributeTest {

    private EvaluationShrinker evaluationShrinker;
    private Clazz clazz;
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        evaluationShrinker = new EvaluationShrinker(partialEvaluator, true, false, null, null);
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
        assertDoesNotThrow(() -> evaluationShrinker.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> evaluationShrinker.visitAnyAttribute(null, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Attribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> evaluationShrinker.visitAnyAttribute(clazz, null));
    }

    /**
     * Tests that visitAnyAttribute can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> evaluationShrinker.visitAnyAttribute(null, null));
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            evaluationShrinker.visitAnyAttribute(clazz, attribute);
            evaluationShrinker.visitAnyAttribute(clazz, attribute);
            evaluationShrinker.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithClazz() {
        // Act
        evaluationShrinker.visitAnyAttribute(clazz, attribute);

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
        evaluationShrinker.visitAnyAttribute(clazz, attribute);

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
        evaluationShrinker.visitAnyAttribute(clazz, attribute);

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
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = evaluationShrinker;

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
        assertDoesNotThrow(() -> evaluationShrinker.visitAnyAttribute(realClass, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called rapidly in succession.
     * Verifies consistent behavior under stress.
     */
    @Test
    public void testVisitAnyAttribute_rapidSequentialCalls_doesNotThrowException() {
        // Act & Assert - all calls should succeed without exceptions
        for (int i = 0; i < 1000; i++) {
            assertDoesNotThrow(() -> evaluationShrinker.visitAnyAttribute(clazz, attribute),
                    "Call " + i + " should not throw exception");
        }
    }

    /**
     * Tests that visitAnyAttribute works with different EvaluationShrinker instances.
     * Verifies that multiple shrinker instances behave consistently.
     */
    @Test
    public void testVisitAnyAttribute_multipleEvaluationShrinkerInstances_allWorkCorrectly() {
        // Arrange
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        EvaluationShrinker shrinker1 = new EvaluationShrinker(partialEvaluator, true, false, null, null);
        EvaluationShrinker shrinker2 = new EvaluationShrinker(partialEvaluator, false, true, null, null);
        EvaluationShrinker shrinker3 = new EvaluationShrinker(partialEvaluator, true, true, null, null);

        // Act & Assert - all should work without exceptions
        assertDoesNotThrow(() -> {
            shrinker1.visitAnyAttribute(clazz, attribute);
            shrinker2.visitAnyAttribute(clazz, attribute);
            shrinker3.visitAnyAttribute(clazz, attribute);
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
            evaluationShrinker.visitAnyAttribute(clazz, attr1);
            evaluationShrinker.visitAnyAttribute(clazz, attr2);
            evaluationShrinker.visitAnyAttribute(clazz, attr3);
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
            evaluationShrinker.visitAnyAttribute(clazz1, attribute);
            evaluationShrinker.visitAnyAttribute(clazz2, attribute);
            evaluationShrinker.visitAnyAttribute(clazz3, attribute);
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
        evaluationShrinker.visitAnyAttribute(realClass, attribute);

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
            evaluationShrinker.visitAnyAttribute(null, null);
            evaluationShrinker.visitAnyAttribute(clazz, null);
            evaluationShrinker.visitAnyAttribute(null, attribute);
            evaluationShrinker.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute can be called with EvaluationShrinker created via
     * InstructionUsageMarker constructor.
     * Verifies consistent behavior across different constructor variants.
     */
    @Test
    public void testVisitAnyAttribute_withInstructionUsageMarkerConstructor_doesNotThrowException() {
        // Arrange
        InstructionUsageMarker usageMarker = new InstructionUsageMarker(true);
        EvaluationShrinker shrinker = new EvaluationShrinker(usageMarker, true, null, null);

        // Act & Assert
        assertDoesNotThrow(() -> shrinker.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute maintains consistent behavior across different
     * EvaluationShrinker constructor parameter combinations.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentConstructorParameters_allWorkCorrectly() {
        // Arrange
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        EvaluationShrinker shrinker1 = new EvaluationShrinker(partialEvaluator, true, false, null, null);
        EvaluationShrinker shrinker2 = new EvaluationShrinker(partialEvaluator, false, true, null, null);
        EvaluationShrinker shrinker3 = new EvaluationShrinker(partialEvaluator, true, true, null, null);
        EvaluationShrinker shrinker4 = new EvaluationShrinker(partialEvaluator, false, false, null, null);

        // Act & Assert - all constructor variants should work
        assertDoesNotThrow(() -> {
            shrinker1.visitAnyAttribute(clazz, attribute);
            shrinker2.visitAnyAttribute(clazz, attribute);
            shrinker3.visitAnyAttribute(clazz, attribute);
            shrinker4.visitAnyAttribute(clazz, attribute);
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
                evaluationShrinker.visitAnyAttribute(clazz, attribute);
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
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = evaluationShrinker;
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            visitor.visitAnyAttribute(realClass, attribute);
            visitor.visitAnyAttribute(null, null);
        });
    }

    /**
     * Tests that visitAnyAttribute has no effect on the EvaluationShrinker's internal state.
     * Verifies that subsequent calls to visitAnyAttribute behave identically.
     */
    @Test
    public void testVisitAnyAttribute_repeatedCalls_behaviorRemainsConsistent() {
        // Act & Assert - multiple calls should have identical behavior
        assertDoesNotThrow(() -> evaluationShrinker.visitAnyAttribute(clazz, attribute));
        assertDoesNotThrow(() -> evaluationShrinker.visitAnyAttribute(clazz, attribute));
        assertDoesNotThrow(() -> evaluationShrinker.visitAnyAttribute(clazz, attribute));

        // Verify no interactions still occur after multiple calls
        verifyNoInteractions(clazz, attribute);
    }
}

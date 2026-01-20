package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.evaluation.PartialEvaluator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link EvaluationSimplifier} constructor.
 * Tests the EvaluationSimplifier(PartialEvaluator, InstructionVisitor, boolean) constructor.
 */
public class EvaluationSimplifierClaude_constructorTest {

    /**
     * Tests the constructor with all parameters provided (non-null visitor).
     * Verifies that the EvaluationSimplifier instance can be instantiated with valid parameters.
     */
    @Test
    public void testConstructorWithAllParameters() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionVisitor extraVisitor = new TestInstructionVisitor();

        // Act - Create EvaluationSimplifier with all parameters
        EvaluationSimplifier simplifier = new EvaluationSimplifier(
            partialEvaluator,
            extraVisitor,
            true
        );

        // Assert - Verify the EvaluationSimplifier instance was created successfully
        assertNotNull(simplifier, "EvaluationSimplifier should be instantiated successfully");
    }

    /**
     * Tests the constructor with null InstructionVisitor.
     * Verifies that the constructor accepts null for the optional visitor parameter.
     */
    @Test
    public void testConstructorWithNullVisitor() {
        // Arrange - Create partial evaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create EvaluationSimplifier with null visitor
        EvaluationSimplifier simplifier = new EvaluationSimplifier(
            partialEvaluator,
            null,
            true
        );

        // Assert - Verify the EvaluationSimplifier instance was created successfully
        assertNotNull(simplifier, "EvaluationSimplifier should accept null visitor");
    }

    /**
     * Tests the constructor with predictNullPointerExceptions set to true.
     * Verifies that the constructor works with NPE prediction enabled.
     */
    @Test
    public void testConstructorWithPredictNullPointerExceptionsTrue() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create EvaluationSimplifier with predictNullPointerExceptions=true
        EvaluationSimplifier simplifier = new EvaluationSimplifier(
            partialEvaluator,
            null,
            true
        );

        // Assert - Verify the EvaluationSimplifier instance was created successfully
        assertNotNull(simplifier, "EvaluationSimplifier should be created with predictNullPointerExceptions=true");
    }

    /**
     * Tests the constructor with predictNullPointerExceptions set to false.
     * Verifies that the constructor works with NPE prediction disabled.
     */
    @Test
    public void testConstructorWithPredictNullPointerExceptionsFalse() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create EvaluationSimplifier with predictNullPointerExceptions=false
        EvaluationSimplifier simplifier = new EvaluationSimplifier(
            partialEvaluator,
            null,
            false
        );

        // Assert - Verify the EvaluationSimplifier instance was created successfully
        assertNotNull(simplifier, "EvaluationSimplifier should be created with predictNullPointerExceptions=false");
    }

    /**
     * Tests the constructor with different boolean values.
     * Verifies that both boolean parameter values work correctly.
     */
    @Test
    public void testConstructorWithVariousBooleanValues() {
        // Arrange - Create partial evaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act & Assert - Test both boolean values
        EvaluationSimplifier simplifier1 = new EvaluationSimplifier(partialEvaluator, null, true);
        assertNotNull(simplifier1, "EvaluationSimplifier should work with true");

        EvaluationSimplifier simplifier2 = new EvaluationSimplifier(partialEvaluator, null, false);
        assertNotNull(simplifier2, "EvaluationSimplifier should work with false");
    }

    /**
     * Tests the constructor with a non-null extraInstructionVisitor.
     * Verifies that the constructor works when the extra visitor is specified.
     */
    @Test
    public void testConstructorWithExtraInstructionVisitor() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionVisitor extraVisitor = new TestInstructionVisitor();

        // Act - Create EvaluationSimplifier with extra visitor
        EvaluationSimplifier simplifier = new EvaluationSimplifier(
            partialEvaluator,
            extraVisitor,
            false
        );

        // Assert - Verify the EvaluationSimplifier instance was created successfully
        assertNotNull(simplifier, "EvaluationSimplifier should work with extra visitor");
    }

    /**
     * Tests that multiple EvaluationSimplifier instances can be created independently.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testMultipleEvaluationSimplifierInstances() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionVisitor visitor1 = new TestInstructionVisitor();
        InstructionVisitor visitor2 = new TestInstructionVisitor();

        // Act - Create two EvaluationSimplifier instances
        EvaluationSimplifier simplifier1 = new EvaluationSimplifier(partialEvaluator, visitor1, true);
        EvaluationSimplifier simplifier2 = new EvaluationSimplifier(partialEvaluator, visitor2, false);

        // Assert - Verify both instances were created and are different
        assertNotNull(simplifier1, "First EvaluationSimplifier should be created");
        assertNotNull(simplifier2, "Second EvaluationSimplifier should be created");
        assertNotSame(simplifier1, simplifier2, "EvaluationSimplifier instances should be different objects");
    }

    /**
     * Tests the constructor with the same visitor for multiple instances.
     * Verifies that the same visitor can be used for multiple EvaluationSimplifier instances.
     */
    @Test
    public void testConstructorWithSharedVisitor() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Create EvaluationSimplifier instances with same visitor
        EvaluationSimplifier simplifier1 = new EvaluationSimplifier(
            partialEvaluator,
            visitor,
            true
        );
        EvaluationSimplifier simplifier2 = new EvaluationSimplifier(
            partialEvaluator,
            visitor,
            false
        );

        // Assert - Verify the EvaluationSimplifier instances were created successfully
        assertNotNull(simplifier1, "First EvaluationSimplifier should be created with shared visitor");
        assertNotNull(simplifier2, "Second EvaluationSimplifier should be created with shared visitor");
        assertNotSame(simplifier1, simplifier2, "Instances should be different objects");
    }

    /**
     * Tests the constructor with different visitor implementations.
     * Verifies that different InstructionVisitor implementations work correctly.
     */
    @Test
    public void testConstructorWithDifferentVisitorImplementations() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionVisitor visitor1 = new TestInstructionVisitor();
        InstructionVisitor visitor2 = new TrackingInstructionVisitor();

        // Act - Create EvaluationSimplifier with different visitor types
        EvaluationSimplifier simplifier1 = new EvaluationSimplifier(
            partialEvaluator,
            visitor1,
            true
        );
        EvaluationSimplifier simplifier2 = new EvaluationSimplifier(
            partialEvaluator,
            visitor2,
            false
        );

        // Assert - Verify the EvaluationSimplifier instances were created successfully
        assertNotNull(simplifier1, "EvaluationSimplifier should work with first visitor implementation");
        assertNotNull(simplifier2, "EvaluationSimplifier should work with second visitor implementation");
    }

    /**
     * Tests that the created EvaluationSimplifier is a valid AttributeVisitor.
     * Verifies that EvaluationSimplifier implements the AttributeVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidAttributeVisitor() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create EvaluationSimplifier
        EvaluationSimplifier simplifier = new EvaluationSimplifier(
            partialEvaluator,
            null,
            true
        );

        // Assert - Verify it implements AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, simplifier,
                "EvaluationSimplifier should implement AttributeVisitor");
    }

    /**
     * Tests that the created EvaluationSimplifier is a valid InstructionVisitor.
     * Verifies that EvaluationSimplifier implements the InstructionVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidInstructionVisitor() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create EvaluationSimplifier
        EvaluationSimplifier simplifier = new EvaluationSimplifier(
            partialEvaluator,
            null,
            false
        );

        // Assert - Verify it implements InstructionVisitor
        assertInstanceOf(InstructionVisitor.class, simplifier,
                "EvaluationSimplifier should implement InstructionVisitor");
    }

    /**
     * Tests the constructor with different PartialEvaluator configurations.
     * Verifies that the constructor works with various PartialEvaluator instances.
     */
    @Test
    public void testConstructorWithDifferentPartialEvaluatorConfigurations() {
        // Arrange & Act - Create EvaluationSimplifiers with different PartialEvaluator configurations
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();

        EvaluationSimplifier simplifier1 = new EvaluationSimplifier(evaluator1, null, true);
        EvaluationSimplifier simplifier2 = new EvaluationSimplifier(evaluator2, null, false);

        // Assert - Verify both instances were created successfully
        assertNotNull(simplifier1, "EvaluationSimplifier should work with first PartialEvaluator");
        assertNotNull(simplifier2, "EvaluationSimplifier should work with second PartialEvaluator");
    }

    /**
     * Tests the constructor with the same PartialEvaluator for multiple instances.
     * Verifies that multiple EvaluationSimplifier instances can share the same PartialEvaluator.
     */
    @Test
    public void testConstructorWithSharedPartialEvaluator() {
        // Arrange - Create a single PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create multiple EvaluationSimplifier instances with the same evaluator
        EvaluationSimplifier simplifier1 = new EvaluationSimplifier(partialEvaluator, null, true);
        EvaluationSimplifier simplifier2 = new EvaluationSimplifier(partialEvaluator, null, false);

        // Assert - Verify both instances were created successfully
        assertNotNull(simplifier1, "First EvaluationSimplifier should be created");
        assertNotNull(simplifier2, "Second EvaluationSimplifier should be created");
        assertNotSame(simplifier1, simplifier2, "Different EvaluationSimplifier instances should be different objects");
    }

    /**
     * Tests the constructor with all combinations of boolean and visitor parameters.
     * Verifies that all parameter combinations work correctly.
     */
    @Test
    public void testConstructorWithAllParameterCombinations() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Test all 4 combinations
        EvaluationSimplifier s1 = new EvaluationSimplifier(partialEvaluator, visitor, true);
        assertNotNull(s1, "EvaluationSimplifier should work with (visitor, true)");

        EvaluationSimplifier s2 = new EvaluationSimplifier(partialEvaluator, visitor, false);
        assertNotNull(s2, "EvaluationSimplifier should work with (visitor, false)");

        EvaluationSimplifier s3 = new EvaluationSimplifier(partialEvaluator, null, true);
        assertNotNull(s3, "EvaluationSimplifier should work with (null, true)");

        EvaluationSimplifier s4 = new EvaluationSimplifier(partialEvaluator, null, false);
        assertNotNull(s4, "EvaluationSimplifier should work with (null, false)");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     * Verifies the constructor's basic robustness.
     */
    @Test
    public void testConstructorDoesNotThrowException() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act & Assert - Verify no exceptions are thrown
        assertDoesNotThrow(() -> new EvaluationSimplifier(partialEvaluator, visitor, true),
                "Constructor should not throw exception with (visitor, true)");
        assertDoesNotThrow(() -> new EvaluationSimplifier(partialEvaluator, visitor, false),
                "Constructor should not throw exception with (visitor, false)");
        assertDoesNotThrow(() -> new EvaluationSimplifier(partialEvaluator, null, true),
                "Constructor should not throw exception with (null, true)");
        assertDoesNotThrow(() -> new EvaluationSimplifier(partialEvaluator, null, false),
                "Constructor should not throw exception with (null, false)");
    }

    /**
     * Tests creating multiple instances in succession with alternating parameters.
     * Verifies that the constructor can handle repeated instantiation with different parameters.
     */
    @Test
    public void testMultipleInstancesInSuccession() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Create multiple instances with alternating parameters
        EvaluationSimplifier s1 = new EvaluationSimplifier(partialEvaluator, visitor, true);
        EvaluationSimplifier s2 = new EvaluationSimplifier(partialEvaluator, null, false);
        EvaluationSimplifier s3 = new EvaluationSimplifier(partialEvaluator, visitor, true);
        EvaluationSimplifier s4 = new EvaluationSimplifier(partialEvaluator, null, false);

        // Assert - Verify all instances are unique
        assertNotNull(s1, "First instance should be created");
        assertNotNull(s2, "Second instance should be created");
        assertNotNull(s3, "Third instance should be created");
        assertNotNull(s4, "Fourth instance should be created");
        assertNotSame(s1, s3, "Instances with same parameters should still be different objects");
        assertNotSame(s2, s4, "Instances with same parameters should still be different objects");
    }

    /**
     * Tests that the constructor creates instances implementing both interfaces.
     * Verifies that EvaluationSimplifier properly implements both AttributeVisitor and InstructionVisitor.
     */
    @Test
    public void testConstructorCreatesDualInterfaceImplementation() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create EvaluationSimplifier with predictNullPointerExceptions=true
        EvaluationSimplifier simplifier = new EvaluationSimplifier(
            partialEvaluator,
            null,
            true
        );

        // Assert - Verify it implements both interfaces
        assertInstanceOf(AttributeVisitor.class, simplifier,
                "EvaluationSimplifier should implement AttributeVisitor");
        assertInstanceOf(InstructionVisitor.class, simplifier,
                "EvaluationSimplifier should implement InstructionVisitor");
    }

    /**
     * Tests that instances created with different parameters implement the same interfaces.
     * Verifies consistency across different parameter values.
     */
    @Test
    public void testInterfaceImplementationConsistency() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Create instances with different parameters
        EvaluationSimplifier s1 = new EvaluationSimplifier(partialEvaluator, visitor, true);
        EvaluationSimplifier s2 = new EvaluationSimplifier(partialEvaluator, null, false);

        // Assert - Verify both implement the same interfaces
        assertInstanceOf(AttributeVisitor.class, s1);
        assertInstanceOf(AttributeVisitor.class, s2);
        assertInstanceOf(InstructionVisitor.class, s1);
        assertInstanceOf(InstructionVisitor.class, s2);
    }

    /**
     * Tests the constructor with predictNullPointerExceptions=true produces a unique instance.
     * Verifies instance creation with NPE prediction enabled.
     */
    @Test
    public void testConstructorWithTrueProducesUniqueInstance() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create two instances with true
        EvaluationSimplifier simplifier1 = new EvaluationSimplifier(partialEvaluator, null, true);
        EvaluationSimplifier simplifier2 = new EvaluationSimplifier(partialEvaluator, null, true);

        // Assert - Verify they are separate instances
        assertNotSame(simplifier1, simplifier2,
                "Two instances created with same parameters should be different objects");
    }

    /**
     * Tests the constructor with predictNullPointerExceptions=false produces a unique instance.
     * Verifies instance creation with NPE prediction disabled.
     */
    @Test
    public void testConstructorWithFalseProducesUniqueInstance() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create two instances with false
        EvaluationSimplifier simplifier1 = new EvaluationSimplifier(partialEvaluator, null, false);
        EvaluationSimplifier simplifier2 = new EvaluationSimplifier(partialEvaluator, null, false);

        // Assert - Verify they are separate instances
        assertNotSame(simplifier1, simplifier2,
                "Two instances created with same parameters should be different objects");
    }

    /**
     * Tests the constructor with a tracking visitor to verify visitor parameter is accepted.
     * Verifies that the visitor parameter is properly handled by the constructor.
     */
    @Test
    public void testConstructorWithTrackingVisitor() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        TrackingInstructionVisitor visitor = new TrackingInstructionVisitor();

        // Act - Create EvaluationSimplifier with tracking visitor
        EvaluationSimplifier simplifier = new EvaluationSimplifier(
            partialEvaluator,
            visitor,
            true
        );

        // Assert - Verify the EvaluationSimplifier instance was created successfully
        assertNotNull(simplifier, "EvaluationSimplifier should work with tracking visitor");
        assertFalse(visitor.instructionVisited, "Visitor should not be invoked during construction");
    }

    /**
     * Simple test InstructionVisitor implementation for testing purposes.
     */
    private static class TestInstructionVisitor implements InstructionVisitor {
        @Override
        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute,
                                       int offset, Instruction instruction) {
            // No-op for testing
        }
    }

    /**
     * InstructionVisitor implementation that tracks whether it was called.
     */
    private static class TrackingInstructionVisitor implements InstructionVisitor {
        boolean instructionVisited = false;

        @Override
        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute,
                                       int offset, Instruction instruction) {
            instructionVisited = true;
        }
    }
}

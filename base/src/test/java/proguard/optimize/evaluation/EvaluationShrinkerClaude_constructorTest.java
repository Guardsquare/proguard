package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.evaluation.PartialEvaluator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link EvaluationShrinker} constructor.
 * Tests both the EvaluationShrinker(PartialEvaluator, boolean, boolean, InstructionVisitor, InstructionVisitor) constructor
 * and the EvaluationShrinker(InstructionUsageMarker, boolean, InstructionVisitor, InstructionVisitor) constructor.
 */
public class EvaluationShrinkerClaude_constructorTest {

    /**
     * Tests the constructor with all parameters provided (non-null visitors).
     * Verifies that the EvaluationShrinker instance can be instantiated with valid parameters.
     */
    @Test
    public void testConstructorWithAllParameters() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionVisitor deletedVisitor = new TestInstructionVisitor();
        InstructionVisitor addedVisitor = new TestInstructionVisitor();

        // Act - Create EvaluationShrinker with all parameters
        EvaluationShrinker shrinker = new EvaluationShrinker(
            partialEvaluator,
            true,
            false,
            deletedVisitor,
            addedVisitor
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should be instantiated successfully");
    }

    /**
     * Tests the constructor with null InstructionVisitors.
     * Verifies that the constructor accepts null for the optional visitor parameters.
     */
    @Test
    public void testConstructorWithNullVisitors() {
        // Arrange - Create partial evaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create EvaluationShrinker with null visitors
        EvaluationShrinker shrinker = new EvaluationShrinker(
            partialEvaluator,
            true,
            false,
            null,
            null
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should accept null visitors");
    }

    /**
     * Tests the constructor with runPartialEvaluator set to true.
     * Verifies that the constructor works with runPartialEvaluator flag enabled.
     */
    @Test
    public void testConstructorWithRunPartialEvaluatorTrue() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create EvaluationShrinker with runPartialEvaluator=true
        EvaluationShrinker shrinker = new EvaluationShrinker(
            partialEvaluator,
            true,
            false,
            null,
            null
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should be created with runPartialEvaluator=true");
    }

    /**
     * Tests the constructor with runPartialEvaluator set to false.
     * Verifies that the constructor works with runPartialEvaluator flag disabled.
     */
    @Test
    public void testConstructorWithRunPartialEvaluatorFalse() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create EvaluationShrinker with runPartialEvaluator=false
        EvaluationShrinker shrinker = new EvaluationShrinker(
            partialEvaluator,
            false,
            false,
            null,
            null
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should be created with runPartialEvaluator=false");
    }

    /**
     * Tests the constructor with optimizeConservatively set to true.
     * Verifies that the constructor works with conservative optimization enabled.
     */
    @Test
    public void testConstructorWithOptimizeConservativelyTrue() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create EvaluationShrinker with optimizeConservatively=true
        EvaluationShrinker shrinker = new EvaluationShrinker(
            partialEvaluator,
            true,
            true,
            null,
            null
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should be created with optimizeConservatively=true");
    }

    /**
     * Tests the constructor with optimizeConservatively set to false.
     * Verifies that the constructor works with conservative optimization disabled.
     */
    @Test
    public void testConstructorWithOptimizeConservativelyFalse() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create EvaluationShrinker with optimizeConservatively=false
        EvaluationShrinker shrinker = new EvaluationShrinker(
            partialEvaluator,
            true,
            false,
            null,
            null
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should be created with optimizeConservatively=false");
    }

    /**
     * Tests the constructor with different boolean combinations.
     * Verifies that all combinations of boolean parameters work correctly.
     */
    @Test
    public void testConstructorWithVariousBooleanCombinations() {
        // Arrange - Create partial evaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act & Assert - Test all 4 combinations of boolean flags
        EvaluationShrinker shrinker1 = new EvaluationShrinker(partialEvaluator, true, true, null, null);
        assertNotNull(shrinker1, "EvaluationShrinker should work with (true, true)");

        EvaluationShrinker shrinker2 = new EvaluationShrinker(partialEvaluator, true, false, null, null);
        assertNotNull(shrinker2, "EvaluationShrinker should work with (true, false)");

        EvaluationShrinker shrinker3 = new EvaluationShrinker(partialEvaluator, false, true, null, null);
        assertNotNull(shrinker3, "EvaluationShrinker should work with (false, true)");

        EvaluationShrinker shrinker4 = new EvaluationShrinker(partialEvaluator, false, false, null, null);
        assertNotNull(shrinker4, "EvaluationShrinker should work with (false, false)");
    }

    /**
     * Tests the constructor with only extraDeletedInstructionVisitor provided.
     * Verifies that the constructor works when only the deleted visitor is specified.
     */
    @Test
    public void testConstructorWithOnlyDeletedVisitor() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionVisitor deletedVisitor = new TestInstructionVisitor();

        // Act - Create EvaluationShrinker with only deleted visitor
        EvaluationShrinker shrinker = new EvaluationShrinker(
            partialEvaluator,
            true,
            false,
            deletedVisitor,
            null
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should work with only deleted visitor");
    }

    /**
     * Tests the constructor with only extraAddedInstructionVisitor provided.
     * Verifies that the constructor works when only the added visitor is specified.
     */
    @Test
    public void testConstructorWithOnlyAddedVisitor() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionVisitor addedVisitor = new TestInstructionVisitor();

        // Act - Create EvaluationShrinker with only added visitor
        EvaluationShrinker shrinker = new EvaluationShrinker(
            partialEvaluator,
            true,
            false,
            null,
            addedVisitor
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should work with only added visitor");
    }

    /**
     * Tests that multiple EvaluationShrinker instances can be created independently.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testMultipleEvaluationShrinkerInstances() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionVisitor visitor1 = new TestInstructionVisitor();
        InstructionVisitor visitor2 = new TestInstructionVisitor();

        // Act - Create two EvaluationShrinker instances
        EvaluationShrinker shrinker1 = new EvaluationShrinker(partialEvaluator, true, false, visitor1, visitor1);
        EvaluationShrinker shrinker2 = new EvaluationShrinker(partialEvaluator, false, true, visitor2, visitor2);

        // Assert - Verify both instances were created and are different
        assertNotNull(shrinker1, "First EvaluationShrinker should be created");
        assertNotNull(shrinker2, "Second EvaluationShrinker should be created");
        assertNotSame(shrinker1, shrinker2, "EvaluationShrinker instances should be different objects");
    }

    /**
     * Tests the constructor with the same visitor for both parameters.
     * Verifies that the same visitor can be used for both deleted and added instructions.
     */
    @Test
    public void testConstructorWithSameVisitorForBothParameters() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Create EvaluationShrinker with same visitor for both parameters
        EvaluationShrinker shrinker = new EvaluationShrinker(
            partialEvaluator,
            true,
            false,
            visitor,
            visitor
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should accept the same visitor for both parameters");
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

        // Act - Create EvaluationShrinker with different visitor types
        EvaluationShrinker shrinker = new EvaluationShrinker(
            partialEvaluator,
            true,
            false,
            visitor1,
            visitor2
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should work with different visitor implementations");
    }

    /**
     * Tests that the created EvaluationShrinker is a valid AttributeVisitor.
     * Verifies that EvaluationShrinker implements the AttributeVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidAttributeVisitor() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create EvaluationShrinker
        EvaluationShrinker shrinker = new EvaluationShrinker(
            partialEvaluator,
            true,
            false,
            null,
            null
        );

        // Assert - Verify it implements AttributeVisitor
        assertInstanceOf(proguard.classfile.attribute.visitor.AttributeVisitor.class, shrinker,
                "EvaluationShrinker should implement AttributeVisitor");
    }

    /**
     * Tests that the created EvaluationShrinker is a valid ExceptionInfoVisitor.
     * Verifies that EvaluationShrinker implements the ExceptionInfoVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidExceptionInfoVisitor() {
        // Arrange - Create dependencies
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create EvaluationShrinker
        EvaluationShrinker shrinker = new EvaluationShrinker(
            partialEvaluator,
            true,
            false,
            null,
            null
        );

        // Assert - Verify it implements ExceptionInfoVisitor
        assertInstanceOf(proguard.classfile.attribute.visitor.ExceptionInfoVisitor.class, shrinker,
                "EvaluationShrinker should implement ExceptionInfoVisitor");
    }

    /**
     * Tests the constructor with different PartialEvaluator configurations.
     * Verifies that the constructor works with various PartialEvaluator instances.
     */
    @Test
    public void testConstructorWithDifferentPartialEvaluatorConfigurations() {
        // Arrange & Act - Create EvaluationShrinkers with different PartialEvaluator configurations
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();

        EvaluationShrinker shrinker1 = new EvaluationShrinker(evaluator1, true, false, null, null);
        EvaluationShrinker shrinker2 = new EvaluationShrinker(evaluator2, true, false, null, null);

        // Assert - Verify both instances were created successfully
        assertNotNull(shrinker1, "EvaluationShrinker should work with first PartialEvaluator");
        assertNotNull(shrinker2, "EvaluationShrinker should work with second PartialEvaluator");
    }

    /**
     * Tests the constructor with the same PartialEvaluator for multiple instances.
     * Verifies that multiple EvaluationShrinker instances can share the same PartialEvaluator.
     */
    @Test
    public void testConstructorWithSharedPartialEvaluator() {
        // Arrange - Create a single PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create multiple EvaluationShrinker instances with the same evaluator
        EvaluationShrinker shrinker1 = new EvaluationShrinker(partialEvaluator, true, false, null, null);
        EvaluationShrinker shrinker2 = new EvaluationShrinker(partialEvaluator, false, true, null, null);

        // Assert - Verify both instances were created successfully
        assertNotNull(shrinker1, "First EvaluationShrinker should be created");
        assertNotNull(shrinker2, "Second EvaluationShrinker should be created");
        assertNotSame(shrinker1, shrinker2, "Different EvaluationShrinker instances should be different objects");
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

    // ========== Tests for InstructionUsageMarker constructor ==========

    /**
     * Tests the InstructionUsageMarker constructor with all parameters.
     * Verifies that the EvaluationShrinker can be instantiated with an InstructionUsageMarker.
     */
    @Test
    public void testInstructionUsageMarkerConstructorWithAllParameters() {
        // Arrange - Create dependencies
        InstructionUsageMarker usageMarker = new InstructionUsageMarker(true);
        InstructionVisitor deletedVisitor = new TestInstructionVisitor();
        InstructionVisitor addedVisitor = new TestInstructionVisitor();

        // Act - Create EvaluationShrinker with InstructionUsageMarker
        EvaluationShrinker shrinker = new EvaluationShrinker(
            usageMarker,
            true,
            deletedVisitor,
            addedVisitor
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should be instantiated with InstructionUsageMarker");
    }

    /**
     * Tests the InstructionUsageMarker constructor with null visitors.
     * Verifies that the constructor accepts null for the optional visitor parameters.
     */
    @Test
    public void testInstructionUsageMarkerConstructorWithNullVisitors() {
        // Arrange - Create usage marker
        InstructionUsageMarker usageMarker = new InstructionUsageMarker(false);

        // Act - Create EvaluationShrinker with null visitors
        EvaluationShrinker shrinker = new EvaluationShrinker(
            usageMarker,
            true,
            null,
            null
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should accept null visitors with InstructionUsageMarker");
    }

    /**
     * Tests the InstructionUsageMarker constructor with runInstructionUsageMarker set to true.
     * Verifies that the constructor works with the run flag enabled.
     */
    @Test
    public void testInstructionUsageMarkerConstructorWithRunFlagTrue() {
        // Arrange - Create usage marker
        InstructionUsageMarker usageMarker = new InstructionUsageMarker(true);

        // Act - Create EvaluationShrinker with runInstructionUsageMarker=true
        EvaluationShrinker shrinker = new EvaluationShrinker(
            usageMarker,
            true,
            null,
            null
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should be created with runInstructionUsageMarker=true");
    }

    /**
     * Tests the InstructionUsageMarker constructor with runInstructionUsageMarker set to false.
     * Verifies that the constructor works with the run flag disabled.
     */
    @Test
    public void testInstructionUsageMarkerConstructorWithRunFlagFalse() {
        // Arrange - Create usage marker
        InstructionUsageMarker usageMarker = new InstructionUsageMarker(false);

        // Act - Create EvaluationShrinker with runInstructionUsageMarker=false
        EvaluationShrinker shrinker = new EvaluationShrinker(
            usageMarker,
            false,
            null,
            null
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should be created with runInstructionUsageMarker=false");
    }

    /**
     * Tests the InstructionUsageMarker constructor with only extraDeletedInstructionVisitor.
     * Verifies that the constructor works when only the deleted visitor is specified.
     */
    @Test
    public void testInstructionUsageMarkerConstructorWithOnlyDeletedVisitor() {
        // Arrange - Create dependencies
        InstructionUsageMarker usageMarker = new InstructionUsageMarker(true);
        InstructionVisitor deletedVisitor = new TestInstructionVisitor();

        // Act - Create EvaluationShrinker with only deleted visitor
        EvaluationShrinker shrinker = new EvaluationShrinker(
            usageMarker,
            true,
            deletedVisitor,
            null
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should work with only deleted visitor");
    }

    /**
     * Tests the InstructionUsageMarker constructor with only extraAddedInstructionVisitor.
     * Verifies that the constructor works when only the added visitor is specified.
     */
    @Test
    public void testInstructionUsageMarkerConstructorWithOnlyAddedVisitor() {
        // Arrange - Create dependencies
        InstructionUsageMarker usageMarker = new InstructionUsageMarker(false);
        InstructionVisitor addedVisitor = new TestInstructionVisitor();

        // Act - Create EvaluationShrinker with only added visitor
        EvaluationShrinker shrinker = new EvaluationShrinker(
            usageMarker,
            true,
            null,
            addedVisitor
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should work with only added visitor");
    }

    /**
     * Tests the InstructionUsageMarker constructor with the same visitor for both parameters.
     * Verifies that the same visitor can be used for both deleted and added instructions.
     */
    @Test
    public void testInstructionUsageMarkerConstructorWithSameVisitor() {
        // Arrange - Create dependencies
        InstructionUsageMarker usageMarker = new InstructionUsageMarker(true);
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Create EvaluationShrinker with same visitor for both parameters
        EvaluationShrinker shrinker = new EvaluationShrinker(
            usageMarker,
            true,
            visitor,
            visitor
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should accept the same visitor for both parameters");
    }

    /**
     * Tests the InstructionUsageMarker constructor with different visitor implementations.
     * Verifies that different InstructionVisitor implementations work correctly.
     */
    @Test
    public void testInstructionUsageMarkerConstructorWithDifferentVisitors() {
        // Arrange - Create dependencies
        InstructionUsageMarker usageMarker = new InstructionUsageMarker(false);
        InstructionVisitor visitor1 = new TestInstructionVisitor();
        InstructionVisitor visitor2 = new TrackingInstructionVisitor();

        // Act - Create EvaluationShrinker with different visitor types
        EvaluationShrinker shrinker = new EvaluationShrinker(
            usageMarker,
            true,
            visitor1,
            visitor2
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should work with different visitor implementations");
    }

    /**
     * Tests multiple EvaluationShrinker instances with InstructionUsageMarker.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testInstructionUsageMarkerConstructorMultipleInstances() {
        // Arrange - Create dependencies
        InstructionUsageMarker usageMarker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker usageMarker2 = new InstructionUsageMarker(false);

        // Act - Create two EvaluationShrinker instances
        EvaluationShrinker shrinker1 = new EvaluationShrinker(usageMarker1, true, null, null);
        EvaluationShrinker shrinker2 = new EvaluationShrinker(usageMarker2, false, null, null);

        // Assert - Verify both instances were created and are different
        assertNotNull(shrinker1, "First EvaluationShrinker should be created");
        assertNotNull(shrinker2, "Second EvaluationShrinker should be created");
        assertNotSame(shrinker1, shrinker2, "EvaluationShrinker instances should be different objects");
    }

    /**
     * Tests the InstructionUsageMarker constructor with shared usage marker.
     * Verifies that multiple EvaluationShrinker instances can share the same InstructionUsageMarker.
     */
    @Test
    public void testInstructionUsageMarkerConstructorWithSharedMarker() {
        // Arrange - Create a single InstructionUsageMarker
        InstructionUsageMarker usageMarker = new InstructionUsageMarker(true);

        // Act - Create multiple EvaluationShrinker instances with the same marker
        EvaluationShrinker shrinker1 = new EvaluationShrinker(usageMarker, true, null, null);
        EvaluationShrinker shrinker2 = new EvaluationShrinker(usageMarker, false, null, null);

        // Assert - Verify both instances were created successfully
        assertNotNull(shrinker1, "First EvaluationShrinker should be created");
        assertNotNull(shrinker2, "Second EvaluationShrinker should be created");
        assertNotSame(shrinker1, shrinker2, "Different EvaluationShrinker instances should be different objects");
    }

    /**
     * Tests the InstructionUsageMarker constructor with PartialEvaluator-based marker.
     * Verifies that InstructionUsageMarker created with PartialEvaluator works correctly.
     */
    @Test
    public void testInstructionUsageMarkerConstructorWithPartialEvaluatorMarker() {
        // Arrange - Create InstructionUsageMarker with PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();
        InstructionUsageMarker usageMarker = new InstructionUsageMarker(partialEvaluator, true, false);

        // Act - Create EvaluationShrinker
        EvaluationShrinker shrinker = new EvaluationShrinker(
            usageMarker,
            true,
            null,
            null
        );

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should work with PartialEvaluator-based marker");
    }

    /**
     * Tests that EvaluationShrinker created with InstructionUsageMarker implements AttributeVisitor.
     * Verifies interface implementation.
     */
    @Test
    public void testInstructionUsageMarkerConstructorCreatesValidAttributeVisitor() {
        // Arrange - Create usage marker
        InstructionUsageMarker usageMarker = new InstructionUsageMarker(true);

        // Act - Create EvaluationShrinker
        EvaluationShrinker shrinker = new EvaluationShrinker(usageMarker, true, null, null);

        // Assert - Verify it implements AttributeVisitor
        assertInstanceOf(proguard.classfile.attribute.visitor.AttributeVisitor.class, shrinker,
                "EvaluationShrinker should implement AttributeVisitor");
    }

    /**
     * Tests that EvaluationShrinker created with InstructionUsageMarker implements ExceptionInfoVisitor.
     * Verifies interface implementation.
     */
    @Test
    public void testInstructionUsageMarkerConstructorCreatesValidExceptionInfoVisitor() {
        // Arrange - Create usage marker
        InstructionUsageMarker usageMarker = new InstructionUsageMarker(false);

        // Act - Create EvaluationShrinker
        EvaluationShrinker shrinker = new EvaluationShrinker(usageMarker, false, null, null);

        // Assert - Verify it implements ExceptionInfoVisitor
        assertInstanceOf(proguard.classfile.attribute.visitor.ExceptionInfoVisitor.class, shrinker,
                "EvaluationShrinker should implement ExceptionInfoVisitor");
    }

    /**
     * Tests the InstructionUsageMarker constructor with markExternalSideEffects true.
     * Verifies that InstructionUsageMarker with external side effects marking works.
     */
    @Test
    public void testInstructionUsageMarkerConstructorWithExternalSideEffectsTrue() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=true
        InstructionUsageMarker usageMarker = new InstructionUsageMarker(true);

        // Act - Create EvaluationShrinker
        EvaluationShrinker shrinker = new EvaluationShrinker(usageMarker, true, null, null);

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should work with markExternalSideEffects=true");
    }

    /**
     * Tests the InstructionUsageMarker constructor with markExternalSideEffects false.
     * Verifies that InstructionUsageMarker without external side effects marking works.
     */
    @Test
    public void testInstructionUsageMarkerConstructorWithExternalSideEffectsFalse() {
        // Arrange - Create InstructionUsageMarker with markExternalSideEffects=false
        InstructionUsageMarker usageMarker = new InstructionUsageMarker(false);

        // Act - Create EvaluationShrinker
        EvaluationShrinker shrinker = new EvaluationShrinker(usageMarker, false, null, null);

        // Assert - Verify the EvaluationShrinker instance was created successfully
        assertNotNull(shrinker, "EvaluationShrinker should work with markExternalSideEffects=false");
    }

    /**
     * Tests the InstructionUsageMarker constructor with various run flag combinations.
     * Verifies that both boolean values work correctly.
     */
    @Test
    public void testInstructionUsageMarkerConstructorWithVariousRunFlags() {
        // Arrange & Act - Test both combinations
        InstructionUsageMarker marker1 = new InstructionUsageMarker(true);
        InstructionUsageMarker marker2 = new InstructionUsageMarker(false);

        EvaluationShrinker shrinker1 = new EvaluationShrinker(marker1, true, null, null);
        EvaluationShrinker shrinker2 = new EvaluationShrinker(marker2, false, null, null);

        // Assert - Verify all combinations work
        assertNotNull(shrinker1, "EvaluationShrinker should work with (true, true)");
        assertNotNull(shrinker2, "EvaluationShrinker should work with (false, false)");
    }
}

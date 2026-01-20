package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link TailRecursionSimplifier} constructors.
 * Tests both TailRecursionSimplifier() and TailRecursionSimplifier(InstructionVisitor) constructors.
 */
public class TailRecursionSimplifierClaude_constructorTest {

    /**
     * Tests the no-arg constructor TailRecursionSimplifier().
     * Verifies that the TailRecursionSimplifier instance can be instantiated without parameters.
     */
    @Test
    public void testNoArgConstructor() {
        // Act - Create TailRecursionSimplifier with no-arg constructor
        TailRecursionSimplifier simplifier = new TailRecursionSimplifier();

        // Assert - Verify the TailRecursionSimplifier instance was created successfully
        assertNotNull(simplifier, "TailRecursionSimplifier should be instantiated successfully");
    }

    /**
     * Tests the constructor TailRecursionSimplifier(InstructionVisitor) with a valid InstructionVisitor.
     * Verifies that the TailRecursionSimplifier instance can be instantiated with a proper visitor.
     */
    @Test
    public void testConstructorWithValidInstructionVisitor() {
        // Arrange - Create a valid InstructionVisitor
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Create TailRecursionSimplifier with the visitor
        TailRecursionSimplifier simplifier = new TailRecursionSimplifier(visitor);

        // Assert - Verify the TailRecursionSimplifier instance was created successfully
        assertNotNull(simplifier, "TailRecursionSimplifier should be instantiated successfully");
    }

    /**
     * Tests the constructor TailRecursionSimplifier(InstructionVisitor) with a null InstructionVisitor.
     * Verifies that the TailRecursionSimplifier constructor accepts null visitor.
     */
    @Test
    public void testConstructorWithNullInstructionVisitor() {
        // Act - Create TailRecursionSimplifier with null visitor
        TailRecursionSimplifier simplifier = new TailRecursionSimplifier(null);

        // Assert - Verify the TailRecursionSimplifier instance was created
        assertNotNull(simplifier, "TailRecursionSimplifier should be instantiated even with null visitor");
    }

    /**
     * Tests that multiple TailRecursionSimplifier instances can be created independently.
     * Verifies that multiple TailRecursionSimplifier instances can be created with different visitors.
     */
    @Test
    public void testMultipleTailRecursionSimplifierInstances() {
        // Arrange - Create two different InstructionVisitors
        InstructionVisitor visitor1 = new TestInstructionVisitor();
        InstructionVisitor visitor2 = new TestInstructionVisitor();

        // Act - Create two TailRecursionSimplifier instances
        TailRecursionSimplifier simplifier1 = new TailRecursionSimplifier(visitor1);
        TailRecursionSimplifier simplifier2 = new TailRecursionSimplifier(visitor2);

        // Assert - Verify both TailRecursionSimplifier instances were created successfully
        assertNotNull(simplifier1, "First TailRecursionSimplifier instance should be created");
        assertNotNull(simplifier2, "Second TailRecursionSimplifier instance should be created");
        assertNotSame(simplifier1, simplifier2, "TailRecursionSimplifier instances should be different objects");
    }

    /**
     * Tests that the same visitor can be used to create multiple TailRecursionSimplifier instances.
     * Verifies that multiple TailRecursionSimplifier instances can share the same visitor.
     */
    @Test
    public void testMultipleTailRecursionSimplifierInstancesWithSameVisitor() {
        // Arrange - Create a single InstructionVisitor
        InstructionVisitor visitor = new TestInstructionVisitor();

        // Act - Create multiple TailRecursionSimplifier instances with the same visitor
        TailRecursionSimplifier simplifier1 = new TailRecursionSimplifier(visitor);
        TailRecursionSimplifier simplifier2 = new TailRecursionSimplifier(visitor);

        // Assert - Verify both TailRecursionSimplifier instances were created successfully
        assertNotNull(simplifier1, "First TailRecursionSimplifier instance should be created");
        assertNotNull(simplifier2, "Second TailRecursionSimplifier instance should be created");
        assertNotSame(simplifier1, simplifier2, "TailRecursionSimplifier instances should be different objects");
    }

    /**
     * Tests that the no-arg constructor creates a TailRecursionSimplifier equivalent to passing null.
     * Verifies that TailRecursionSimplifier() is equivalent to TailRecursionSimplifier(null).
     */
    @Test
    public void testNoArgConstructorEquivalentToNullParameter() {
        // Act - Create TailRecursionSimplifier with both constructors
        TailRecursionSimplifier simplifier1 = new TailRecursionSimplifier();
        TailRecursionSimplifier simplifier2 = new TailRecursionSimplifier(null);

        // Assert - Verify both TailRecursionSimplifier instances were created successfully
        assertNotNull(simplifier1, "No-arg TailRecursionSimplifier should be created");
        assertNotNull(simplifier2, "Null-arg TailRecursionSimplifier should be created");
        assertNotSame(simplifier1, simplifier2, "Different TailRecursionSimplifier instances should be different objects");
    }

    /**
     * Tests that the constructor accepts different InstructionVisitor implementations.
     * Verifies that TailRecursionSimplifier works with various InstructionVisitor implementations.
     */
    @Test
    public void testConstructorWithDifferentInstructionVisitorImplementations() {
        // Arrange & Act - Create TailRecursionSimplifier with different visitor types
        TailRecursionSimplifier simplifier1 = new TailRecursionSimplifier(new TestInstructionVisitor());
        TailRecursionSimplifier simplifier2 = new TailRecursionSimplifier(new TrackingInstructionVisitor());
        TailRecursionSimplifier simplifier3 = new TailRecursionSimplifier(new AnotherTestInstructionVisitor());

        // Assert - Verify all TailRecursionSimplifier instances were created successfully
        assertNotNull(simplifier1, "TailRecursionSimplifier should work with TestInstructionVisitor");
        assertNotNull(simplifier2, "TailRecursionSimplifier should work with TrackingInstructionVisitor");
        assertNotNull(simplifier3, "TailRecursionSimplifier should work with AnotherTestInstructionVisitor");
    }

    /**
     * Tests that multiple no-arg constructor calls create independent instances.
     * Verifies that each call to the no-arg constructor creates a new object.
     */
    @Test
    public void testMultipleNoArgConstructorCalls() {
        // Act - Create multiple TailRecursionSimplifier instances with no-arg constructor
        TailRecursionSimplifier simplifier1 = new TailRecursionSimplifier();
        TailRecursionSimplifier simplifier2 = new TailRecursionSimplifier();
        TailRecursionSimplifier simplifier3 = new TailRecursionSimplifier();

        // Assert - Verify all instances are distinct
        assertNotNull(simplifier1, "First TailRecursionSimplifier should be created");
        assertNotNull(simplifier2, "Second TailRecursionSimplifier should be created");
        assertNotNull(simplifier3, "Third TailRecursionSimplifier should be created");
        assertNotSame(simplifier1, simplifier2, "First and second instances should be different");
        assertNotSame(simplifier2, simplifier3, "Second and third instances should be different");
        assertNotSame(simplifier1, simplifier3, "First and third instances should be different");
    }

    /**
     * Tests that the created TailRecursionSimplifier is a valid AttributeVisitor.
     * Verifies that TailRecursionSimplifier can be used as an AttributeVisitor.
     */
    @Test
    public void testConstructorCreatesValidAttributeVisitor() {
        // Act - Create TailRecursionSimplifier with both constructors
        TailRecursionSimplifier simplifier1 = new TailRecursionSimplifier();
        TailRecursionSimplifier simplifier2 = new TailRecursionSimplifier(new TestInstructionVisitor());

        // Assert - Verify they can be used as AttributeVisitors
        assertInstanceOf(proguard.classfile.attribute.visitor.AttributeVisitor.class, simplifier1,
                "TailRecursionSimplifier should implement AttributeVisitor");
        assertInstanceOf(proguard.classfile.attribute.visitor.AttributeVisitor.class, simplifier2,
                "TailRecursionSimplifier with visitor should implement AttributeVisitor");
    }

    /**
     * Tests that the created TailRecursionSimplifier is a valid InstructionVisitor.
     * Verifies that TailRecursionSimplifier can be used as an InstructionVisitor.
     */
    @Test
    public void testConstructorCreatesValidInstructionVisitor() {
        // Act - Create TailRecursionSimplifier with both constructors
        TailRecursionSimplifier simplifier1 = new TailRecursionSimplifier();
        TailRecursionSimplifier simplifier2 = new TailRecursionSimplifier(new TestInstructionVisitor());

        // Assert - Verify they can be used as InstructionVisitors
        assertInstanceOf(InstructionVisitor.class, simplifier1,
                "TailRecursionSimplifier should implement InstructionVisitor");
        assertInstanceOf(InstructionVisitor.class, simplifier2,
                "TailRecursionSimplifier with visitor should implement InstructionVisitor");
    }

    /**
     * Tests that the created TailRecursionSimplifier is a valid ConstantVisitor.
     * Verifies that TailRecursionSimplifier can be used as a ConstantVisitor.
     */
    @Test
    public void testConstructorCreatesValidConstantVisitor() {
        // Act - Create TailRecursionSimplifier with both constructors
        TailRecursionSimplifier simplifier1 = new TailRecursionSimplifier();
        TailRecursionSimplifier simplifier2 = new TailRecursionSimplifier(new TestInstructionVisitor());

        // Assert - Verify they can be used as ConstantVisitors
        assertInstanceOf(proguard.classfile.constant.visitor.ConstantVisitor.class, simplifier1,
                "TailRecursionSimplifier should implement ConstantVisitor");
        assertInstanceOf(proguard.classfile.constant.visitor.ConstantVisitor.class, simplifier2,
                "TailRecursionSimplifier with visitor should implement ConstantVisitor");
    }

    /**
     * Tests that the created TailRecursionSimplifier is a valid ExceptionInfoVisitor.
     * Verifies that TailRecursionSimplifier can be used as an ExceptionInfoVisitor.
     */
    @Test
    public void testConstructorCreatesValidExceptionInfoVisitor() {
        // Act - Create TailRecursionSimplifier with both constructors
        TailRecursionSimplifier simplifier1 = new TailRecursionSimplifier();
        TailRecursionSimplifier simplifier2 = new TailRecursionSimplifier(new TestInstructionVisitor());

        // Assert - Verify they can be used as ExceptionInfoVisitors
        assertInstanceOf(proguard.classfile.attribute.visitor.ExceptionInfoVisitor.class, simplifier1,
                "TailRecursionSimplifier should implement ExceptionInfoVisitor");
        assertInstanceOf(proguard.classfile.attribute.visitor.ExceptionInfoVisitor.class, simplifier2,
                "TailRecursionSimplifier with visitor should implement ExceptionInfoVisitor");
    }

    /**
     * Simple test InstructionVisitor implementation for testing purposes.
     */
    private static class TestInstructionVisitor implements InstructionVisitor {
        @Override
        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {
            // No-op for testing
        }
    }

    /**
     * InstructionVisitor implementation that tracks whether it was called.
     */
    private static class TrackingInstructionVisitor implements InstructionVisitor {
        boolean instructionVisited = false;

        @Override
        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {
            instructionVisited = true;
        }
    }

    /**
     * Another test InstructionVisitor implementation for testing purposes.
     */
    private static class AnotherTestInstructionVisitor implements InstructionVisitor {
        @Override
        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {
            // No-op for testing
        }
    }
}

package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link WrapperClassUseSimplifier} constructors with signatures:
 * - ()V
 * - (Lproguard/classfile/instruction/visitor/InstructionVisitor;)V
 *
 * These constructors create a new WrapperClassUseSimplifier that simplifies the use of retargeted
 * wrapper classes in code attributes. The no-arg constructor delegates to the single-parameter
 * constructor with null for the extraInstructionVisitor parameter.
 */
public class WrapperClassUseSimplifierClaude_constructorTest {

    // ========================================
    // Basic Constructor Tests
    // ========================================

    /**
     * Tests the no-arg constructor.
     * Verifies that the simplifier can be instantiated without parameters.
     */
    @Test
    public void testNoArgConstructor() {
        // Arrange & Act
        WrapperClassUseSimplifier simplifier = new WrapperClassUseSimplifier();

        // Assert
        assertNotNull(simplifier, "WrapperClassUseSimplifier should be created successfully");
    }

    /**
     * Tests that the no-arg constructor creates an instance of AttributeVisitor.
     * Verifies that WrapperClassUseSimplifier implements the AttributeVisitor interface.
     */
    @Test
    public void testNoArgConstructorCreatesInstanceOfAttributeVisitor() {
        // Arrange & Act
        WrapperClassUseSimplifier simplifier = new WrapperClassUseSimplifier();

        // Assert
        assertInstanceOf(AttributeVisitor.class, simplifier,
            "WrapperClassUseSimplifier should implement AttributeVisitor interface");
    }

    /**
     * Tests that the created instance can be used as an AttributeVisitor.
     * Verifies that the simplifier is in a valid state for use.
     */
    @Test
    public void testNoArgConstructorCreatesUsableAttributeVisitor() {
        // Arrange & Act
        WrapperClassUseSimplifier simplifier = new WrapperClassUseSimplifier();

        // Assert
        assertNotNull(simplifier, "WrapperClassUseSimplifier should be created");
        AttributeVisitor visitor = simplifier;
        assertNotNull(visitor, "WrapperClassUseSimplifier should be usable as AttributeVisitor");
    }

    // ========================================
    // Multiple Instance Tests
    // ========================================

    /**
     * Tests creating multiple simplifier instances.
     * Verifies that multiple instances can be created independently.
     */
    @Test
    public void testMultipleNoArgConstructorInstances() {
        // Arrange & Act
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier();
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier();

        // Assert
        assertNotNull(simplifier1, "First simplifier should be created");
        assertNotNull(simplifier2, "Second simplifier should be created");
        assertNotSame(simplifier1, simplifier2, "Simplifier instances should be different");
    }

    /**
     * Tests creating a sequence of simplifiers.
     * Verifies that multiple simplifiers can be created sequentially without issues.
     */
    @Test
    public void testSequentialSimplifierCreation() {
        // Act & Assert - create multiple simplifiers sequentially
        for (int i = 0; i < 10; i++) {
            WrapperClassUseSimplifier simplifier = new WrapperClassUseSimplifier();
            assertNotNull(simplifier, "Simplifier " + i + " should be created");
        }
    }

    // ========================================
    // Efficiency and Performance Tests
    // ========================================

    /**
     * Tests that the no-arg constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testNoArgConstructorIsEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act
        WrapperClassUseSimplifier simplifier = new WrapperClassUseSimplifier();

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(simplifier, "WrapperClassUseSimplifier should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests that the no-arg constructor properly initializes the instance for use.
     * Verifies that the created instance is in a valid state.
     */
    @Test
    public void testNoArgConstructorInitializesInstanceProperly() {
        // Arrange & Act
        WrapperClassUseSimplifier simplifier = new WrapperClassUseSimplifier();

        // Assert
        assertNotNull(simplifier, "WrapperClassUseSimplifier should be created");
        // The simplifier should be usable as an AttributeVisitor
        AttributeVisitor visitor = simplifier;
        assertNotNull(visitor, "WrapperClassUseSimplifier should be usable as AttributeVisitor");
    }

    // ========================================
    // Single-Parameter Constructor Tests
    // ========================================

    /**
     * Tests the single-parameter constructor with a valid non-null InstructionVisitor.
     * Verifies that the simplifier can be instantiated with an extra instruction visitor.
     */
    @Test
    public void testSingleParamConstructorWithValidParameter() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        WrapperClassUseSimplifier simplifier = new WrapperClassUseSimplifier(extraInstructionVisitor);

        // Assert
        assertNotNull(simplifier, "WrapperClassUseSimplifier should be created successfully");
    }

    /**
     * Tests the single-parameter constructor with null extraInstructionVisitor.
     * Verifies that the simplifier handles null visitor parameter.
     */
    @Test
    public void testSingleParamConstructorWithNullExtraInstructionVisitor() {
        // Arrange & Act
        WrapperClassUseSimplifier simplifier = new WrapperClassUseSimplifier(null);

        // Assert
        assertNotNull(simplifier, "WrapperClassUseSimplifier should be created with null extraInstructionVisitor");
    }

    /**
     * Tests that the single-parameter constructor creates an instance of AttributeVisitor.
     * Verifies that WrapperClassUseSimplifier implements the AttributeVisitor interface.
     */
    @Test
    public void testSingleParamConstructorCreatesInstanceOfAttributeVisitor() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        WrapperClassUseSimplifier simplifier = new WrapperClassUseSimplifier(extraInstructionVisitor);

        // Assert
        assertInstanceOf(AttributeVisitor.class, simplifier,
            "WrapperClassUseSimplifier should implement AttributeVisitor interface");
    }

    /**
     * Tests creating multiple simplifier instances with the same parameter.
     * Verifies that multiple instances can be created using the same parameter.
     */
    @Test
    public void testSingleParamConstructorMultipleInstancesWithSameParameter() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(extraInstructionVisitor);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(extraInstructionVisitor);

        // Assert
        assertNotNull(simplifier1, "First simplifier should be created");
        assertNotNull(simplifier2, "Second simplifier should be created");
        assertNotSame(simplifier1, simplifier2, "Simplifier instances should be different");
    }

    /**
     * Tests creating multiple simplifier instances with different parameters.
     * Verifies that simplifiers can be created independently with different configurations.
     */
    @Test
    public void testSingleParamConstructorMultipleInstancesWithDifferentParameters() {
        // Arrange
        InstructionVisitor extraInstructionVisitor1 = mock(InstructionVisitor.class);
        InstructionVisitor extraInstructionVisitor2 = mock(InstructionVisitor.class);

        // Act
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(extraInstructionVisitor1);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(extraInstructionVisitor2);

        // Assert
        assertNotNull(simplifier1, "First simplifier should be created");
        assertNotNull(simplifier2, "Second simplifier should be created");
        assertNotSame(simplifier1, simplifier2, "Simplifier instances should be different");
    }

    /**
     * Tests creating multiple simplifier instances with shared extraInstructionVisitor.
     * Verifies that simplifiers can share the same visitor instance.
     */
    @Test
    public void testSingleParamConstructorMultipleInstancesWithSharedExtraInstructionVisitor() {
        // Arrange
        InstructionVisitor sharedVisitor = mock(InstructionVisitor.class);

        // Act
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(sharedVisitor);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(sharedVisitor);

        // Assert
        assertNotNull(simplifier1, "First simplifier should be created");
        assertNotNull(simplifier2, "Second simplifier should be created");
        assertNotSame(simplifier1, simplifier2, "Simplifier instances should be different");
    }

    /**
     * Tests that the single-parameter constructor doesn't invoke any methods on the parameters.
     * Verifies that the constructor only stores the parameter without using it.
     */
    @Test
    public void testSingleParamConstructorDoesNotInvokeParameter() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        WrapperClassUseSimplifier simplifier = new WrapperClassUseSimplifier(extraInstructionVisitor);

        // Assert
        assertNotNull(simplifier, "WrapperClassUseSimplifier should be created");
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests that the single-parameter constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testSingleParamConstructorIsEfficient() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);
        long startTime = System.nanoTime();

        // Act
        WrapperClassUseSimplifier simplifier = new WrapperClassUseSimplifier(extraInstructionVisitor);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(simplifier, "WrapperClassUseSimplifier should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests creating a sequence of simplifiers with the single-parameter constructor.
     * Verifies that multiple simplifiers can be created sequentially without issues.
     */
    @Test
    public void testSingleParamConstructorSequentialSimplifierCreation() {
        // Act & Assert - create multiple simplifiers sequentially
        for (int i = 0; i < 10; i++) {
            InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);
            WrapperClassUseSimplifier simplifier = new WrapperClassUseSimplifier(extraInstructionVisitor);
            assertNotNull(simplifier, "Simplifier " + i + " should be created");
        }
    }

    /**
     * Tests that two simplifiers created with null extraInstructionVisitor are independent instances.
     * Verifies that even with null parameters, distinct instances are created.
     */
    @Test
    public void testSingleParamConstructorMultipleInstancesWithNullsAreIndependent() {
        // Act
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(null);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(null);

        // Assert
        assertNotNull(simplifier1, "First simplifier should be created");
        assertNotNull(simplifier2, "Second simplifier should be created");
        assertNotSame(simplifier1, simplifier2, "Simplifier instances should be different even with null parameters");
    }

    /**
     * Tests the single-parameter constructor with various null/non-null combinations.
     * Verifies that both combinations of null/non-null parameters are handled.
     */
    @Test
    public void testSingleParamConstructorWithVariousNullCombinations() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Test case 1: non-null visitor
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(extraInstructionVisitor);
        assertNotNull(simplifier1, "Simplifier with non-null visitor should be created");

        // Test case 2: null visitor
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(null);
        assertNotNull(simplifier2, "Simplifier with null visitor should be created");
    }

    /**
     * Tests that the single-parameter constructor properly initializes the instance for use.
     * Verifies that the created instance is in a valid state.
     */
    @Test
    public void testSingleParamConstructorInitializesInstanceProperly() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        WrapperClassUseSimplifier simplifier = new WrapperClassUseSimplifier(extraInstructionVisitor);

        // Assert
        assertNotNull(simplifier, "WrapperClassUseSimplifier should be created");
        // The simplifier should be usable as an AttributeVisitor
        AttributeVisitor visitor = simplifier;
        assertNotNull(visitor, "WrapperClassUseSimplifier should be usable as AttributeVisitor");
    }

    // ========================================
    // Cross-Constructor Tests
    // ========================================

    /**
     * Tests that the no-arg and single-parameter constructors create independent instances.
     * Verifies that both constructors can be used interchangeably.
     */
    @Test
    public void testBothConstructorsCreateIndependentInstances() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier();
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(extraInstructionVisitor);
        WrapperClassUseSimplifier simplifier3 = new WrapperClassUseSimplifier(null);

        // Assert
        assertNotNull(simplifier1, "No-arg constructor should create instance");
        assertNotNull(simplifier2, "Single-param constructor with visitor should create instance");
        assertNotNull(simplifier3, "Single-param constructor with null visitor should create instance");
        assertNotSame(simplifier1, simplifier2, "Instances should be different");
        assertNotSame(simplifier1, simplifier3, "Instances should be different");
        assertNotSame(simplifier2, simplifier3, "Instances should be different");
    }

    /**
     * Tests that multiple WrapperClassUseSimplifier instances work independently.
     * Verifies that using both constructors doesn't cause interference.
     */
    @Test
    public void testMultipleInstancesFromBothConstructorsWorkIndependently() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier();
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(extraInstructionVisitor);

        // Assert
        assertNotNull(simplifier1, "First simplifier should be created");
        assertNotNull(simplifier2, "Second simplifier should be created");
        assertNotSame(simplifier1, simplifier2, "Simplifier instances should be different");
        assertInstanceOf(AttributeVisitor.class, simplifier1, "First simplifier should implement AttributeVisitor");
        assertInstanceOf(AttributeVisitor.class, simplifier2, "Second simplifier should implement AttributeVisitor");
    }

    /**
     * Tests creating simplifiers with alternating constructors.
     * Verifies that both constructors can be used in sequence without issues.
     */
    @Test
    public void testAlternatingConstructorUsage() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier();
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(extraInstructionVisitor);
        WrapperClassUseSimplifier simplifier3 = new WrapperClassUseSimplifier();
        WrapperClassUseSimplifier simplifier4 = new WrapperClassUseSimplifier(null);

        // Assert
        assertNotNull(simplifier1, "Simplifier 1 should be created");
        assertNotNull(simplifier2, "Simplifier 2 should be created");
        assertNotNull(simplifier3, "Simplifier 3 should be created");
        assertNotNull(simplifier4, "Simplifier 4 should be created");
    }
}

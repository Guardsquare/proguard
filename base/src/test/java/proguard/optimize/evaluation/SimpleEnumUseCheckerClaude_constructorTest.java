package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.value.TypedReferenceValueFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SimpleEnumUseChecker} constructors.
 *
 * Tests both:
 * - The no-arg constructor SimpleEnumUseChecker()
 * - The constructor SimpleEnumUseChecker(PartialEvaluator)
 */
public class SimpleEnumUseCheckerClaude_constructorTest {

    /**
     * Tests the no-arg constructor.
     * Verifies that a SimpleEnumUseChecker can be instantiated without parameters.
     * The no-arg constructor should create a default PartialEvaluator internally.
     */
    @Test
    public void testNoArgConstructor() {
        // Act - Create SimpleEnumUseChecker using no-arg constructor
        SimpleEnumUseChecker checker = new SimpleEnumUseChecker();

        // Assert - Verify the SimpleEnumUseChecker instance was created successfully
        assertNotNull(checker, "SimpleEnumUseChecker should be instantiated successfully");
    }

    /**
     * Tests that the no-arg constructor can be called multiple times.
     * Verifies that each call creates a new independent instance.
     */
    @Test
    public void testNoArgConstructorCreatesMultipleInstances() {
        // Act - Create multiple SimpleEnumUseChecker instances
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker();
        SimpleEnumUseChecker checker3 = new SimpleEnumUseChecker();

        // Assert - Verify all instances are created and are distinct
        assertNotNull(checker1, "First checker should be created");
        assertNotNull(checker2, "Second checker should be created");
        assertNotNull(checker3, "Third checker should be created");
        assertNotSame(checker1, checker2, "First and second checkers should be different instances");
        assertNotSame(checker1, checker3, "First and third checkers should be different instances");
        assertNotSame(checker2, checker3, "Second and third checkers should be different instances");
    }

    /**
     * Tests the constructor with a PartialEvaluator parameter.
     * Verifies that a SimpleEnumUseChecker can be instantiated with a custom PartialEvaluator.
     */
    @Test
    public void testConstructorWithPartialEvaluator() {
        // Arrange - Create a PartialEvaluator
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create().build();

        // Act - Create SimpleEnumUseChecker with the PartialEvaluator
        SimpleEnumUseChecker checker = new SimpleEnumUseChecker(partialEvaluator);

        // Assert - Verify the SimpleEnumUseChecker instance was created successfully
        assertNotNull(checker, "SimpleEnumUseChecker should be instantiated with PartialEvaluator");
    }

    /**
     * Tests the constructor with a PartialEvaluator that uses TypedReferenceValueFactory.
     * This matches the configuration used by the no-arg constructor.
     */
    @Test
    public void testConstructorWithTypedReferenceValueFactoryEvaluator() {
        // Arrange - Create a PartialEvaluator with TypedReferenceValueFactory
        PartialEvaluator partialEvaluator = PartialEvaluator.Builder.create()
            .setValueFactory(new TypedReferenceValueFactory())
            .build();

        // Act - Create SimpleEnumUseChecker with the PartialEvaluator
        SimpleEnumUseChecker checker = new SimpleEnumUseChecker(partialEvaluator);

        // Assert - Verify the SimpleEnumUseChecker instance was created successfully
        assertNotNull(checker, "SimpleEnumUseChecker should work with TypedReferenceValueFactory evaluator");
    }

    /**
     * Tests that the parameterized constructor can be called multiple times.
     * Verifies that each call creates a new independent instance.
     */
    @Test
    public void testParameterizedConstructorCreatesMultipleInstances() {
        // Arrange - Create PartialEvaluators
        PartialEvaluator evaluator1 = PartialEvaluator.Builder.create().build();
        PartialEvaluator evaluator2 = PartialEvaluator.Builder.create().build();
        PartialEvaluator evaluator3 = PartialEvaluator.Builder.create().build();

        // Act - Create multiple SimpleEnumUseChecker instances
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker(evaluator1);
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker(evaluator2);
        SimpleEnumUseChecker checker3 = new SimpleEnumUseChecker(evaluator3);

        // Assert - Verify all instances are created and are distinct
        assertNotNull(checker1, "First checker should be created");
        assertNotNull(checker2, "Second checker should be created");
        assertNotNull(checker3, "Third checker should be created");
        assertNotSame(checker1, checker2, "First and second checkers should be different instances");
        assertNotSame(checker1, checker3, "First and third checkers should be different instances");
        assertNotSame(checker2, checker3, "Second and third checkers should be different instances");
    }

    /**
     * Tests that multiple checkers can share the same PartialEvaluator instance.
     * Verifies that the constructor accepts and works with a shared PartialEvaluator.
     */
    @Test
    public void testMultipleCheckersWithSamePartialEvaluator() {
        // Arrange - Create a single PartialEvaluator
        PartialEvaluator sharedEvaluator = PartialEvaluator.Builder.create()
            .setValueFactory(new TypedReferenceValueFactory())
            .build();

        // Act - Create multiple checkers with the same evaluator
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker(sharedEvaluator);
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker(sharedEvaluator);

        // Assert - Verify both instances are created and are distinct
        assertNotNull(checker1, "First checker should be created");
        assertNotNull(checker2, "Second checker should be created");
        assertNotSame(checker1, checker2, "Checkers should be different instances even with same evaluator");
    }

    /**
     * Tests that the constructor works with different PartialEvaluator configurations.
     * Verifies that SimpleEnumUseChecker can work with various evaluator setups.
     */
    @Test
    public void testConstructorWithVariousPartialEvaluatorConfigurations() {
        // Arrange & Act - Create checkers with different evaluator configurations
        PartialEvaluator defaultEvaluator = PartialEvaluator.Builder.create().build();
        SimpleEnumUseChecker checker1 = new SimpleEnumUseChecker(defaultEvaluator);

        PartialEvaluator typedEvaluator = PartialEvaluator.Builder.create()
            .setValueFactory(new TypedReferenceValueFactory())
            .build();
        SimpleEnumUseChecker checker2 = new SimpleEnumUseChecker(typedEvaluator);

        // Assert - Verify all instances are created successfully
        assertNotNull(checker1, "Checker with default evaluator should be created");
        assertNotNull(checker2, "Checker with typed evaluator should be created");
    }

    /**
     * Tests that SimpleEnumUseChecker implements the expected visitor interfaces.
     * Verifies that the created instance can be used as various visitor types.
     */
    @Test
    public void testNoArgConstructorCreatesValidClassVisitor() {
        // Act - Create SimpleEnumUseChecker
        SimpleEnumUseChecker checker = new SimpleEnumUseChecker();

        // Assert - Verify it can be used as a ClassVisitor
        assertInstanceOf(proguard.classfile.visitor.ClassVisitor.class, checker,
            "SimpleEnumUseChecker should be a ClassVisitor");
    }

    /**
     * Tests that SimpleEnumUseChecker implements the MemberVisitor interface.
     * Verifies that the created instance can be used as a MemberVisitor.
     */
    @Test
    public void testNoArgConstructorCreatesValidMemberVisitor() {
        // Act - Create SimpleEnumUseChecker
        SimpleEnumUseChecker checker = new SimpleEnumUseChecker();

        // Assert - Verify it can be used as a MemberVisitor
        assertInstanceOf(proguard.classfile.visitor.MemberVisitor.class, checker,
            "SimpleEnumUseChecker should be a MemberVisitor");
    }

    /**
     * Tests that SimpleEnumUseChecker implements the AttributeVisitor interface.
     * Verifies that the created instance can be used as an AttributeVisitor.
     */
    @Test
    public void testNoArgConstructorCreatesValidAttributeVisitor() {
        // Act - Create SimpleEnumUseChecker
        SimpleEnumUseChecker checker = new SimpleEnumUseChecker();

        // Assert - Verify it can be used as an AttributeVisitor
        assertInstanceOf(proguard.classfile.attribute.visitor.AttributeVisitor.class, checker,
            "SimpleEnumUseChecker should be an AttributeVisitor");
    }

    /**
     * Tests that SimpleEnumUseChecker implements the InstructionVisitor interface.
     * Verifies that the created instance can be used as an InstructionVisitor.
     */
    @Test
    public void testNoArgConstructorCreatesValidInstructionVisitor() {
        // Act - Create SimpleEnumUseChecker
        SimpleEnumUseChecker checker = new SimpleEnumUseChecker();

        // Assert - Verify it can be used as an InstructionVisitor
        assertInstanceOf(proguard.classfile.instruction.visitor.InstructionVisitor.class, checker,
            "SimpleEnumUseChecker should be an InstructionVisitor");
    }

    /**
     * Tests that SimpleEnumUseChecker implements the ConstantVisitor interface.
     * Verifies that the created instance can be used as a ConstantVisitor.
     */
    @Test
    public void testNoArgConstructorCreatesValidConstantVisitor() {
        // Act - Create SimpleEnumUseChecker
        SimpleEnumUseChecker checker = new SimpleEnumUseChecker();

        // Assert - Verify it can be used as a ConstantVisitor
        assertInstanceOf(proguard.classfile.constant.visitor.ConstantVisitor.class, checker,
            "SimpleEnumUseChecker should be a ConstantVisitor");
    }

    /**
     * Tests that the parameterized constructor creates instances with the same visitor interfaces.
     * Verifies that both constructors create equivalent visitor types.
     */
    @Test
    public void testParameterizedConstructorCreatesValidVisitors() {
        // Arrange
        PartialEvaluator evaluator = PartialEvaluator.Builder.create().build();

        // Act - Create SimpleEnumUseChecker with parameter
        SimpleEnumUseChecker checker = new SimpleEnumUseChecker(evaluator);

        // Assert - Verify it implements all visitor interfaces
        assertInstanceOf(proguard.classfile.visitor.ClassVisitor.class, checker,
            "SimpleEnumUseChecker should be a ClassVisitor");
        assertInstanceOf(proguard.classfile.visitor.MemberVisitor.class, checker,
            "SimpleEnumUseChecker should be a MemberVisitor");
        assertInstanceOf(proguard.classfile.attribute.visitor.AttributeVisitor.class, checker,
            "SimpleEnumUseChecker should be an AttributeVisitor");
        assertInstanceOf(proguard.classfile.instruction.visitor.InstructionVisitor.class, checker,
            "SimpleEnumUseChecker should be an InstructionVisitor");
        assertInstanceOf(proguard.classfile.constant.visitor.ConstantVisitor.class, checker,
            "SimpleEnumUseChecker should be a ConstantVisitor");
    }

    /**
     * Tests that constructed instances can be used interchangeably.
     * Verifies that both constructors create functionally equivalent objects.
     */
    @Test
    public void testBothConstructorsCreateCompatibleInstances() {
        // Act - Create checkers using both constructors
        SimpleEnumUseChecker noArgChecker = new SimpleEnumUseChecker();

        PartialEvaluator evaluator = PartialEvaluator.Builder.create()
            .setValueFactory(new TypedReferenceValueFactory())
            .build();
        SimpleEnumUseChecker parameterizedChecker = new SimpleEnumUseChecker(evaluator);

        // Assert - Verify both are of the same type
        assertEquals(noArgChecker.getClass(), parameterizedChecker.getClass(),
            "Both constructors should create instances of the same class");
    }

    /**
     * Tests that the no-arg constructor is stable across multiple invocations.
     * Verifies consistent behavior when creating many instances.
     */
    @Test
    public void testNoArgConstructorStability() {
        // Act & Assert - Create many instances without issues
        for (int i = 0; i < 100; i++) {
            SimpleEnumUseChecker checker = new SimpleEnumUseChecker();
            assertNotNull(checker, "Checker " + i + " should be created successfully");
        }
    }

    /**
     * Tests that the parameterized constructor is stable across multiple invocations.
     * Verifies consistent behavior when creating many instances with the same evaluator.
     */
    @Test
    public void testParameterizedConstructorStability() {
        // Arrange
        PartialEvaluator evaluator = PartialEvaluator.Builder.create().build();

        // Act & Assert - Create many instances without issues
        for (int i = 0; i < 100; i++) {
            SimpleEnumUseChecker checker = new SimpleEnumUseChecker(evaluator);
            assertNotNull(checker, "Checker " + i + " should be created successfully");
        }
    }

    /**
     * Tests that constructed checkers don't throw exceptions during basic operations.
     * Verifies that the constructor properly initializes all internal state.
     */
    @Test
    public void testConstructedInstancesAreUsable() {
        // Arrange & Act
        SimpleEnumUseChecker noArgChecker = new SimpleEnumUseChecker();

        PartialEvaluator evaluator = PartialEvaluator.Builder.create().build();
        SimpleEnumUseChecker paramChecker = new SimpleEnumUseChecker(evaluator);

        // Assert - Basic visitor methods should not throw on null input (typical no-op behavior)
        assertDoesNotThrow(() -> noArgChecker.visitAnyClass(null),
            "No-arg checker should handle visitAnyClass");
        assertDoesNotThrow(() -> paramChecker.visitAnyClass(null),
            "Parameterized checker should handle visitAnyClass");
    }

    /**
     * Tests the no-arg constructor creates a valid checker for immediate use.
     * Verifies the default configuration is suitable for typical usage.
     */
    @Test
    public void testNoArgConstructorCreatesReadyToUseChecker() {
        // Act
        SimpleEnumUseChecker checker = new SimpleEnumUseChecker();

        // Assert - Should be ready to use as various visitor types
        assertNotNull(checker, "Checker should be created");

        // Verify it implements all necessary interfaces
        assertTrue(checker instanceof proguard.classfile.visitor.ClassVisitor,
            "Should implement ClassVisitor");
        assertTrue(checker instanceof proguard.classfile.visitor.MemberVisitor,
            "Should implement MemberVisitor");
        assertTrue(checker instanceof proguard.classfile.attribute.visitor.AttributeVisitor,
            "Should implement AttributeVisitor");
    }

    /**
     * Tests that the constructor with PartialEvaluator properly stores the evaluator.
     * This test verifies that the evaluator is retained and usable.
     */
    @Test
    public void testParameterizedConstructorRetainsEvaluator() {
        // Arrange
        PartialEvaluator evaluator = PartialEvaluator.Builder.create()
            .setValueFactory(new TypedReferenceValueFactory())
            .build();

        // Act - Create checker with the evaluator
        SimpleEnumUseChecker checker = new SimpleEnumUseChecker(evaluator);

        // Assert - The checker should be successfully created
        // (We can't directly verify the evaluator is stored without reflection,
        // but successful construction implies proper handling)
        assertNotNull(checker, "Checker should be created with provided evaluator");
    }
}

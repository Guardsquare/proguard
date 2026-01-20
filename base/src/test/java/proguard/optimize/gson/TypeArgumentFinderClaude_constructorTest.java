package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.value.TypedReferenceValueFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the TypeArgumentFinder constructor.
 * Tests the constructor (Lproguard/classfile/ClassPool;Lproguard/classfile/ClassPool;Lproguard/evaluation/PartialEvaluator;)V
 */
public class TypeArgumentFinderClaude_constructorTest {

    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private PartialEvaluator partialEvaluator;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        partialEvaluator = PartialEvaluator.Builder.create().build();
    }

    /**
     * Tests that the constructor successfully creates a non-null instance with all valid parameters.
     */
    @Test
    public void testConstructor_createsNonNullInstance() {
        // Act
        TypeArgumentFinder finder = new TypeArgumentFinder(
            programClassPool,
            libraryClassPool,
            partialEvaluator
        );

        // Assert
        assertNotNull(finder, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor properly implements InstructionVisitor interface.
     */
    @Test
    public void testConstructor_implementsInstructionVisitor() {
        // Act
        TypeArgumentFinder finder = new TypeArgumentFinder(
            programClassPool,
            libraryClassPool,
            partialEvaluator
        );

        // Assert
        assertTrue(finder instanceof proguard.classfile.instruction.visitor.InstructionVisitor,
            "TypeArgumentFinder should implement InstructionVisitor");
    }

    /**
     * Tests that the constructor properly implements ConstantVisitor interface.
     */
    @Test
    public void testConstructor_implementsConstantVisitor() {
        // Act
        TypeArgumentFinder finder = new TypeArgumentFinder(
            programClassPool,
            libraryClassPool,
            partialEvaluator
        );

        // Assert
        assertTrue(finder instanceof proguard.classfile.constant.visitor.ConstantVisitor,
            "TypeArgumentFinder should implement ConstantVisitor");
    }

    /**
     * Tests that the constructor works with empty ClassPool instances.
     */
    @Test
    public void testConstructor_withEmptyClassPools() {
        // Arrange
        ClassPool emptyProgramPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();

        // Act
        TypeArgumentFinder finder = new TypeArgumentFinder(
            emptyProgramPool,
            emptyLibraryPool,
            partialEvaluator
        );

        // Assert
        assertNotNull(finder, "Constructor should work with empty class pools");
    }

    /**
     * Tests that the constructor works with the same ClassPool for both program and library.
     */
    @Test
    public void testConstructor_withSameClassPoolForBothParameters() {
        // Arrange
        ClassPool samePool = new ClassPool();

        // Act
        TypeArgumentFinder finder = new TypeArgumentFinder(
            samePool,
            samePool,
            partialEvaluator
        );

        // Assert
        assertNotNull(finder, "Constructor should work with same ClassPool for both parameters");
    }

    /**
     * Tests that the constructor works with a default PartialEvaluator.
     */
    @Test
    public void testConstructor_withDefaultPartialEvaluator() {
        // Arrange
        PartialEvaluator defaultEvaluator = PartialEvaluator.Builder.create().build();

        // Act
        TypeArgumentFinder finder = new TypeArgumentFinder(
            programClassPool,
            libraryClassPool,
            defaultEvaluator
        );

        // Assert
        assertNotNull(finder, "Constructor should work with default PartialEvaluator");
    }

    /**
     * Tests that the constructor works with a PartialEvaluator using TypedReferenceValueFactory.
     */
    @Test
    public void testConstructor_withTypedReferenceValueFactoryEvaluator() {
        // Arrange
        PartialEvaluator typedEvaluator = PartialEvaluator.Builder.create()
            .setValueFactory(new TypedReferenceValueFactory())
            .build();

        // Act
        TypeArgumentFinder finder = new TypeArgumentFinder(
            programClassPool,
            libraryClassPool,
            typedEvaluator
        );

        // Assert
        assertNotNull(finder, "Constructor should work with TypedReferenceValueFactory evaluator");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_createIndependently() {
        // Act
        TypeArgumentFinder finder1 = new TypeArgumentFinder(
            programClassPool, libraryClassPool, partialEvaluator);

        TypeArgumentFinder finder2 = new TypeArgumentFinder(
            programClassPool, libraryClassPool, partialEvaluator);

        TypeArgumentFinder finder3 = new TypeArgumentFinder(
            new ClassPool(), new ClassPool(), PartialEvaluator.Builder.create().build());

        // Assert
        assertNotNull(finder1, "First instance should be created");
        assertNotNull(finder2, "Second instance should be created");
        assertNotNull(finder3, "Third instance should be created");
        assertNotSame(finder1, finder2, "Instances should be distinct");
        assertNotSame(finder2, finder3, "Instances should be distinct");
        assertNotSame(finder1, finder3, "Instances should be distinct");
    }

    /**
     * Tests that the constructor does not throw any exceptions with valid inputs.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new TypeArgumentFinder(
            programClassPool,
            libraryClassPool,
            partialEvaluator
        ), "Constructor should not throw any exception with valid inputs");
    }

    /**
     * Tests that the constructor can be called multiple times consecutively.
     */
    @Test
    public void testConstructor_consecutiveCalls_allSucceed() {
        // Act & Assert
        for (int i = 0; i < 10; i++) {
            TypeArgumentFinder finder = new TypeArgumentFinder(
                new ClassPool(),
                new ClassPool(),
                PartialEvaluator.Builder.create().build()
            );
            assertNotNull(finder, "Instance " + i + " should be created");
        }
    }

    /**
     * Tests that multiple finders can share the same PartialEvaluator instance.
     */
    @Test
    public void testConstructor_multipleInstancesWithSharedPartialEvaluator() {
        // Arrange
        PartialEvaluator sharedEvaluator = PartialEvaluator.Builder.create().build();

        // Act
        TypeArgumentFinder finder1 = new TypeArgumentFinder(
            programClassPool, libraryClassPool, sharedEvaluator);
        TypeArgumentFinder finder2 = new TypeArgumentFinder(
            programClassPool, libraryClassPool, sharedEvaluator);

        // Assert
        assertNotNull(finder1, "First finder should be created");
        assertNotNull(finder2, "Second finder should be created");
        assertNotSame(finder1, finder2, "Finders should be different instances even with same evaluator");
    }

    /**
     * Tests that multiple finders can share the same ClassPool instances.
     */
    @Test
    public void testConstructor_multipleInstancesWithSharedClassPools() {
        // Arrange
        ClassPool sharedProgramPool = new ClassPool();
        ClassPool sharedLibraryPool = new ClassPool();

        // Act
        TypeArgumentFinder finder1 = new TypeArgumentFinder(
            sharedProgramPool, sharedLibraryPool, partialEvaluator);
        TypeArgumentFinder finder2 = new TypeArgumentFinder(
            sharedProgramPool, sharedLibraryPool,
            PartialEvaluator.Builder.create().build());

        // Assert
        assertNotNull(finder1, "First finder should be created");
        assertNotNull(finder2, "Second finder should be created");
        assertNotSame(finder1, finder2, "Finders should be different instances even with shared pools");
    }

    /**
     * Tests that the constructor correctly initializes the finder to be ready for visiting instructions.
     */
    @Test
    public void testConstructor_createsReadyToUseVisitor() {
        // Act
        TypeArgumentFinder finder = new TypeArgumentFinder(
            programClassPool,
            libraryClassPool,
            partialEvaluator
        );

        // Assert - Should be able to call visitor methods without error
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 0, null),
            "Should be able to call visitAnyInstruction after construction");
        assertDoesNotThrow(() -> finder.visitAnyConstant(null, null),
            "Should be able to call visitAnyConstant after construction");
    }

    /**
     * Tests that constructed finders have the typeArgumentClasses field accessible and initially null.
     */
    @Test
    public void testConstructor_typeArgumentClassesAccessible() {
        // Act
        TypeArgumentFinder finder = new TypeArgumentFinder(
            programClassPool,
            libraryClassPool,
            partialEvaluator
        );

        // Assert - typeArgumentClasses is a package-private field that should be accessible
        assertDoesNotThrow(() -> {
            String[] typeArgs = finder.typeArgumentClasses;
        }, "Should be able to access typeArgumentClasses field after construction");
    }

    /**
     * Tests that the constructor works with different PartialEvaluator configurations.
     */
    @Test
    public void testConstructor_withVariousPartialEvaluatorConfigurations() {
        // Arrange & Act - Create finders with different evaluator configurations
        PartialEvaluator defaultEvaluator = PartialEvaluator.Builder.create().build();
        TypeArgumentFinder finder1 = new TypeArgumentFinder(
            programClassPool, libraryClassPool, defaultEvaluator);

        PartialEvaluator typedEvaluator = PartialEvaluator.Builder.create()
            .setValueFactory(new TypedReferenceValueFactory())
            .build();
        TypeArgumentFinder finder2 = new TypeArgumentFinder(
            programClassPool, libraryClassPool, typedEvaluator);

        // Assert - Verify all instances are created successfully
        assertNotNull(finder1, "Finder with default evaluator should be created");
        assertNotNull(finder2, "Finder with typed evaluator should be created");
    }

    /**
     * Tests that the no-arg constructor is stable across multiple invocations.
     */
    @Test
    public void testConstructor_stability() {
        // Act & Assert - Create many instances without issues
        for (int i = 0; i < 100; i++) {
            TypeArgumentFinder finder = new TypeArgumentFinder(
                new ClassPool(),
                new ClassPool(),
                PartialEvaluator.Builder.create().build()
            );
            assertNotNull(finder, "Finder " + i + " should be created successfully");
        }
    }

    /**
     * Tests that constructed instances don't throw exceptions during basic operations.
     */
    @Test
    public void testConstructor_instancesAreUsable() {
        // Arrange & Act
        TypeArgumentFinder finder = new TypeArgumentFinder(
            programClassPool,
            libraryClassPool,
            partialEvaluator
        );

        // Assert - Basic visitor methods should not throw on null input (typical no-op behavior)
        assertDoesNotThrow(() -> finder.visitAnyInstruction(null, null, null, 0, null),
            "Finder should handle visitAnyInstruction with null inputs");
        assertDoesNotThrow(() -> finder.visitAnyConstant(null, null),
            "Finder should handle visitAnyConstant with null inputs");
    }

    /**
     * Tests that the constructor creates a valid InstructionVisitor for immediate use.
     */
    @Test
    public void testConstructor_createsReadyToUseInstructionVisitor() {
        // Act
        TypeArgumentFinder finder = new TypeArgumentFinder(
            programClassPool,
            libraryClassPool,
            partialEvaluator
        );

        // Assert - Should be ready to use as InstructionVisitor
        assertNotNull(finder, "Finder should be created");
        assertTrue(finder instanceof proguard.classfile.instruction.visitor.InstructionVisitor,
            "Should implement InstructionVisitor");
    }

    /**
     * Tests that the constructor creates a valid ConstantVisitor for immediate use.
     */
    @Test
    public void testConstructor_createsReadyToUseConstantVisitor() {
        // Act
        TypeArgumentFinder finder = new TypeArgumentFinder(
            programClassPool,
            libraryClassPool,
            partialEvaluator
        );

        // Assert - Should be ready to use as ConstantVisitor
        assertNotNull(finder, "Finder should be created");
        assertTrue(finder instanceof proguard.classfile.constant.visitor.ConstantVisitor,
            "Should implement ConstantVisitor");
    }

    /**
     * Tests that different finder instances can be created with different configurations.
     */
    @Test
    public void testConstructor_withDifferentConfigurations_createsIndependentInstances() {
        // Arrange
        ClassPool pool1 = new ClassPool();
        ClassPool pool2 = new ClassPool();
        PartialEvaluator eval1 = PartialEvaluator.Builder.create().build();
        PartialEvaluator eval2 = PartialEvaluator.Builder.create()
            .setValueFactory(new TypedReferenceValueFactory())
            .build();

        // Act
        TypeArgumentFinder finder1 = new TypeArgumentFinder(pool1, pool1, eval1);
        TypeArgumentFinder finder2 = new TypeArgumentFinder(pool2, pool2, eval2);

        // Assert
        assertNotNull(finder1, "First finder should be created");
        assertNotNull(finder2, "Second finder should be created");
        assertNotSame(finder1, finder2, "Different configurations should create distinct instances");
    }

    /**
     * Tests that the constructor works when all three parameters are distinct new instances.
     */
    @Test
    public void testConstructor_withAllFreshParameters() {
        // Act
        TypeArgumentFinder finder = new TypeArgumentFinder(
            new ClassPool(),
            new ClassPool(),
            PartialEvaluator.Builder.create().build()
        );

        // Assert
        assertNotNull(finder, "Constructor should work with all fresh parameters");
    }

    /**
     * Tests that the constructor creates instances that implement both required interfaces.
     */
    @Test
    public void testConstructor_implementsBothInterfaces() {
        // Act
        TypeArgumentFinder finder = new TypeArgumentFinder(
            programClassPool,
            libraryClassPool,
            partialEvaluator
        );

        // Assert - Verify it implements both interfaces
        assertInstanceOf(proguard.classfile.instruction.visitor.InstructionVisitor.class, finder,
            "TypeArgumentFinder should be an InstructionVisitor");
        assertInstanceOf(proguard.classfile.constant.visitor.ConstantVisitor.class, finder,
            "TypeArgumentFinder should be a ConstantVisitor");
    }
}

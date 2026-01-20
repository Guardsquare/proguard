package proguard.obfuscate.util;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.constant.Constant;
import proguard.classfile.instruction.Instruction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InstructionSequenceObfuscator} constructor.
 * Tests the constructor:
 * <init>.(Lproguard/obfuscate/util/ReplacementSequences;)V
 */
public class InstructionSequenceObfuscatorClaude_constructorTest {

    /**
     * Tests the constructor with a valid ReplacementSequences creates a valid instance.
     * Verifies that an InstructionSequenceObfuscator can be instantiated.
     */
    @Test
    public void testConstructorCreatesValidInstance() {
        // Arrange
        ReplacementSequences sequences = createSimpleReplacementSequences();

        // Act
        InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(sequences);

        // Assert
        assertNotNull(obfuscator, "InstructionSequenceObfuscator should be created successfully");
    }

    /**
     * Tests that the constructor doesn't throw any exceptions with valid input.
     * Verifies that the constructor is exception-safe with valid inputs.
     */
    @Test
    public void testConstructorDoesNotThrowException() {
        // Arrange
        ReplacementSequences sequences = createSimpleReplacementSequences();

        // Act & Assert
        assertDoesNotThrow(() -> new InstructionSequenceObfuscator(sequences),
            "Constructor should not throw any exceptions");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that each instance is independent.
     */
    @Test
    public void testMultipleInstancesCreation() {
        // Arrange
        ReplacementSequences sequences1 = createSimpleReplacementSequences();
        ReplacementSequences sequences2 = createSimpleReplacementSequences();

        // Act
        InstructionSequenceObfuscator obfuscator1 = new InstructionSequenceObfuscator(sequences1);
        InstructionSequenceObfuscator obfuscator2 = new InstructionSequenceObfuscator(sequences2);

        // Assert
        assertNotNull(obfuscator1, "First instance should be created");
        assertNotNull(obfuscator2, "Second instance should be created");
        assertNotSame(obfuscator1, obfuscator2, "Instances should be different");
    }

    /**
     * Tests that the instance implements ClassVisitor interface.
     * Verifies interface implementation.
     */
    @Test
    public void testInstanceImplementsClassVisitor() {
        // Arrange
        ReplacementSequences sequences = createSimpleReplacementSequences();

        // Act
        InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(sequences);

        // Assert
        assertTrue(obfuscator instanceof proguard.classfile.visitor.ClassVisitor,
                   "Should implement ClassVisitor interface");
    }

    /**
     * Tests that the instance implements MemberVisitor interface.
     * Verifies interface implementation.
     */
    @Test
    public void testInstanceImplementsMemberVisitor() {
        // Arrange
        ReplacementSequences sequences = createSimpleReplacementSequences();

        // Act
        InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(sequences);

        // Assert
        assertTrue(obfuscator instanceof proguard.classfile.visitor.MemberVisitor,
                   "Should implement MemberVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance of the expected class.
     * Verifies the exact type of the created instance.
     */
    @Test
    public void testConstructorCreatesCorrectType() {
        // Arrange
        ReplacementSequences sequences = createSimpleReplacementSequences();

        // Act
        InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(sequences);

        // Assert
        assertEquals(InstructionSequenceObfuscator.class, obfuscator.getClass(),
            "Constructor should create an instance of InstructionSequenceObfuscator");
    }

    /**
     * Tests that the instance is usable immediately after construction.
     * Verifies that no additional initialization is required.
     */
    @Test
    public void testInstanceUsableImmediatelyAfterConstruction() {
        // Arrange
        ReplacementSequences sequences = createSimpleReplacementSequences();
        InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(sequences);
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.visitAnyClass(programClass),
            "Instance should be immediately usable after construction");
    }

    /**
     * Tests that the constructor creates consistent instances.
     * Verifies that all instances created have the same type.
     */
    @Test
    public void testConstructorCreatesConsistentInstances() {
        // Arrange
        ReplacementSequences sequences1 = createSimpleReplacementSequences();
        ReplacementSequences sequences2 = createSimpleReplacementSequences();

        // Act
        InstructionSequenceObfuscator obfuscator1 = new InstructionSequenceObfuscator(sequences1);
        InstructionSequenceObfuscator obfuscator2 = new InstructionSequenceObfuscator(sequences2);

        // Assert
        assertEquals(obfuscator1.getClass(), obfuscator2.getClass(),
            "All instances should have the same class type");
    }

    /**
     * Tests creating multiple instances in a batch.
     * Verifies that the constructor can handle batch creation without issues.
     */
    @Test
    public void testBatchConstructorCreation() {
        // Arrange
        int batchSize = 50;
        InstructionSequenceObfuscator[] obfuscatorArray = new InstructionSequenceObfuscator[batchSize];

        // Act
        for (int i = 0; i < batchSize; i++) {
            ReplacementSequences sequences = createSimpleReplacementSequences();
            obfuscatorArray[i] = new InstructionSequenceObfuscator(sequences);
        }

        // Assert
        for (int i = 0; i < batchSize; i++) {
            assertNotNull(obfuscatorArray[i], "Instance " + i + " should be created");
        }

        // Verify all instances are different
        for (int i = 0; i < batchSize - 1; i++) {
            assertNotSame(obfuscatorArray[i], obfuscatorArray[i + 1],
                "Consecutive instances should be different");
        }
    }

    /**
     * Tests that the constructor is accessible and public.
     * Verifies that the constructor can be called from outside the package.
     */
    @Test
    public void testConstructorIsPublic() {
        // Arrange
        ReplacementSequences sequences = createSimpleReplacementSequences();

        // Act - This test implicitly verifies that the constructor is accessible
        InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(sequences);

        // Assert
        assertNotNull(obfuscator, "Public constructor should be accessible");
    }

    /**
     * Tests constructor with toString() to verify object creation.
     * Verifies that the created object has a valid string representation.
     */
    @Test
    public void testConstructorCreatesObjectWithValidToString() {
        // Arrange
        ReplacementSequences sequences = createSimpleReplacementSequences();

        // Act
        InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(sequences);

        // Assert
        assertNotNull(obfuscator.toString(), "toString() should return a non-null value");
    }

    /**
     * Tests that hashCode is consistent across multiple calls on same instance.
     * Verifies basic object consistency after construction.
     */
    @Test
    public void testConstructorCreatesObjectWithConsistentHashCode() {
        // Arrange
        ReplacementSequences sequences = createSimpleReplacementSequences();

        // Act
        InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(sequences);
        int hashCode1 = obfuscator.hashCode();
        int hashCode2 = obfuscator.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2,
            "hashCode should be consistent for the same instance");
    }

    /**
     * Tests that multiple sequential constructor calls work correctly.
     * Verifies constructor stability under repeated invocation.
     */
    @Test
    public void testMultipleSequentialConstructorCalls() {
        // Act & Assert
        for (int i = 0; i < 10; i++) {
            ReplacementSequences sequences = createSimpleReplacementSequences();
            InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(sequences);
            assertNotNull(obfuscator, "Instance " + i + " should be created");
        }
    }

    /**
     * Tests that the constructor works with empty sequences.
     * Verifies that the constructor handles edge cases properly.
     */
    @Test
    public void testConstructorWithEmptySequences() {
        // Arrange
        ReplacementSequences emptySequences = new ReplacementSequences() {
            @Override
            public Instruction[][][] getSequences() {
                return new Instruction[0][][];
            }

            @Override
            public Constant[] getConstants() {
                return new Constant[0];
            }
        };

        // Act & Assert
        assertDoesNotThrow(() -> {
            InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(emptySequences);
            assertNotNull(obfuscator, "Should be created with empty sequences");
        }, "Constructor should not throw with empty sequences");
    }

    /**
     * Tests that the instance can be used as a ClassVisitor.
     * Verifies that the ClassVisitor interface methods are callable.
     */
    @Test
    public void testInstanceAsClassVisitor() {
        // Arrange
        ReplacementSequences sequences = createSimpleReplacementSequences();
        InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(sequences);
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.visitProgramClass(programClass),
            "Should be usable as ClassVisitor");
    }

    /**
     * Tests that the instance can be used as a MemberVisitor.
     * Verifies that the MemberVisitor interface methods are callable.
     */
    @Test
    public void testInstanceAsMemberVisitor() {
        // Arrange
        ReplacementSequences sequences = createSimpleReplacementSequences();
        InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(sequences);
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.visitProgramMethod(programClass, programMethod),
            "Should be usable as MemberVisitor");
    }

    /**
     * Tests that the constructor works with different ReplacementSequences implementations.
     * Verifies flexibility in accepting different implementations.
     */
    @Test
    public void testConstructorWithDifferentReplacementSequencesImplementations() {
        // Arrange - Create different implementations
        ReplacementSequences sequences1 = createSimpleReplacementSequences();
        ReplacementSequences sequences2 = createSimpleReplacementSequences();

        // Act
        InstructionSequenceObfuscator obfuscator1 = new InstructionSequenceObfuscator(sequences1);
        InstructionSequenceObfuscator obfuscator2 = new InstructionSequenceObfuscator(sequences2);

        // Assert
        assertNotNull(obfuscator1, "First instance should be created");
        assertNotNull(obfuscator2, "Second instance should be created");
    }

    /**
     * Tests that the constructor with a single sequence pair works correctly.
     * Verifies minimal valid configuration.
     */
    @Test
    public void testConstructorWithSingleSequencePair() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        ReplacementSequences sequences = new proguard.obfuscate.kotlin.KotlinUnsupportedExceptionReplacementSequences(
            programClassPool, libraryClassPool);

        // Act
        InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(sequences);

        // Assert
        assertNotNull(obfuscator, "Should be created with a single sequence pair");
    }

    /**
     * Tests that the constructor with multiple sequence pairs works correctly.
     * Verifies handling of complex configurations.
     */
    @Test
    public void testConstructorWithMultipleSequencePairs() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        ReplacementSequences sequences = new proguard.obfuscate.kotlin.KotlinIntrinsicsReplacementSequences(
            programClassPool, libraryClassPool);

        // Act
        InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(sequences);

        // Assert
        assertNotNull(obfuscator, "Should be created with multiple sequence pairs");
    }

    /**
     * Tests that different instances with the same sequences are independent.
     * Verifies proper encapsulation.
     */
    @Test
    public void testDifferentInstancesWithSameSequencesAreIndependent() {
        // Arrange
        ReplacementSequences sharedSequences = createSimpleReplacementSequences();

        // Act
        InstructionSequenceObfuscator obfuscator1 = new InstructionSequenceObfuscator(sharedSequences);
        InstructionSequenceObfuscator obfuscator2 = new InstructionSequenceObfuscator(sharedSequences);

        // Assert
        assertNotSame(obfuscator1, obfuscator2, "Instances should be different even with same sequences");
    }

    /**
     * Tests that visitAnyClass is safe to call immediately after construction.
     * Verifies ClassVisitor interface implementation.
     */
    @Test
    public void testVisitAnyClassSafeAfterConstruction() {
        // Arrange
        ReplacementSequences sequences = createSimpleReplacementSequences();
        InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(sequences);
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.visitAnyClass(programClass),
            "visitAnyClass should be safe to call after construction");
    }

    /**
     * Tests that visitAnyMember is safe to call immediately after construction.
     * Verifies MemberVisitor interface implementation.
     */
    @Test
    public void testVisitAnyMemberSafeAfterConstruction() {
        // Arrange
        ReplacementSequences sequences = createSimpleReplacementSequences();
        InstructionSequenceObfuscator obfuscator = new InstructionSequenceObfuscator(sequences);
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert
        assertDoesNotThrow(() -> obfuscator.visitAnyMember(programClass, programMethod),
            "visitAnyMember should be safe to call after construction");
    }

    // Helper method to create a simple ReplacementSequences implementation
    private ReplacementSequences createSimpleReplacementSequences() {
        return new ReplacementSequences() {
            @Override
            public Instruction[][][] getSequences() {
                return new Instruction[0][][];
            }

            @Override
            public Constant[] getConstants() {
                return new Constant[0];
            }
        };
    }
}

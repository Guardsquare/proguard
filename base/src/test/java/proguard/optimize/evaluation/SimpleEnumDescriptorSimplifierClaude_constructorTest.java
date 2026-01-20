package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.attribute.visitor.LocalVariableInfoVisitor;
import proguard.classfile.attribute.visitor.LocalVariableTypeInfoVisitor;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SimpleEnumDescriptorSimplifier} default constructor.
 * Tests the SimpleEnumDescriptorSimplifier() no-arg constructor.
 */
public class SimpleEnumDescriptorSimplifierClaude_constructorTest {

    /**
     * Tests that the default constructor successfully creates a SimpleEnumDescriptorSimplifier instance.
     * Verifies that the instance can be instantiated without errors.
     */
    @Test
    public void testDefaultConstructor() {
        // Act - Create SimpleEnumDescriptorSimplifier with default constructor
        SimpleEnumDescriptorSimplifier simplifier = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify the SimpleEnumDescriptorSimplifier instance was created successfully
        assertNotNull(simplifier, "SimpleEnumDescriptorSimplifier should be instantiated successfully");
    }

    /**
     * Tests that the created SimpleEnumDescriptorSimplifier is a valid ClassVisitor.
     * Verifies that SimpleEnumDescriptorSimplifier implements the ClassVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidClassVisitor() {
        // Act - Create SimpleEnumDescriptorSimplifier
        SimpleEnumDescriptorSimplifier simplifier = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify it implements ClassVisitor
        assertInstanceOf(ClassVisitor.class, simplifier,
                "SimpleEnumDescriptorSimplifier should implement ClassVisitor");
    }

    /**
     * Tests that the created SimpleEnumDescriptorSimplifier is a valid ConstantVisitor.
     * Verifies that SimpleEnumDescriptorSimplifier implements the ConstantVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidConstantVisitor() {
        // Act - Create SimpleEnumDescriptorSimplifier
        SimpleEnumDescriptorSimplifier simplifier = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify it implements ConstantVisitor
        assertInstanceOf(ConstantVisitor.class, simplifier,
                "SimpleEnumDescriptorSimplifier should implement ConstantVisitor");
    }

    /**
     * Tests that the created SimpleEnumDescriptorSimplifier is a valid MemberVisitor.
     * Verifies that SimpleEnumDescriptorSimplifier implements the MemberVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidMemberVisitor() {
        // Act - Create SimpleEnumDescriptorSimplifier
        SimpleEnumDescriptorSimplifier simplifier = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify it implements MemberVisitor
        assertInstanceOf(MemberVisitor.class, simplifier,
                "SimpleEnumDescriptorSimplifier should implement MemberVisitor");
    }

    /**
     * Tests that the created SimpleEnumDescriptorSimplifier is a valid AttributeVisitor.
     * Verifies that SimpleEnumDescriptorSimplifier implements the AttributeVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidAttributeVisitor() {
        // Act - Create SimpleEnumDescriptorSimplifier
        SimpleEnumDescriptorSimplifier simplifier = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify it implements AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, simplifier,
                "SimpleEnumDescriptorSimplifier should implement AttributeVisitor");
    }

    /**
     * Tests that the created SimpleEnumDescriptorSimplifier is a valid LocalVariableInfoVisitor.
     * Verifies that SimpleEnumDescriptorSimplifier implements the LocalVariableInfoVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidLocalVariableInfoVisitor() {
        // Act - Create SimpleEnumDescriptorSimplifier
        SimpleEnumDescriptorSimplifier simplifier = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify it implements LocalVariableInfoVisitor
        assertInstanceOf(LocalVariableInfoVisitor.class, simplifier,
                "SimpleEnumDescriptorSimplifier should implement LocalVariableInfoVisitor");
    }

    /**
     * Tests that the created SimpleEnumDescriptorSimplifier is a valid LocalVariableTypeInfoVisitor.
     * Verifies that SimpleEnumDescriptorSimplifier implements the LocalVariableTypeInfoVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidLocalVariableTypeInfoVisitor() {
        // Act - Create SimpleEnumDescriptorSimplifier
        SimpleEnumDescriptorSimplifier simplifier = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify it implements LocalVariableTypeInfoVisitor
        assertInstanceOf(LocalVariableTypeInfoVisitor.class, simplifier,
                "SimpleEnumDescriptorSimplifier should implement LocalVariableTypeInfoVisitor");
    }

    /**
     * Tests that multiple SimpleEnumDescriptorSimplifier instances can be created independently.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testMultipleSimpleEnumDescriptorSimplifierInstances() {
        // Act - Create two SimpleEnumDescriptorSimplifier instances
        SimpleEnumDescriptorSimplifier simplifier1 = new SimpleEnumDescriptorSimplifier();
        SimpleEnumDescriptorSimplifier simplifier2 = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify both instances were created and are different
        assertNotNull(simplifier1, "First SimpleEnumDescriptorSimplifier should be created");
        assertNotNull(simplifier2, "Second SimpleEnumDescriptorSimplifier should be created");
        assertNotSame(simplifier1, simplifier2, "SimpleEnumDescriptorSimplifier instances should be different objects");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     * Verifies exception-free construction.
     */
    @Test
    public void testConstructorDoesNotThrowException() {
        // Act & Assert - Verify no exception is thrown
        assertDoesNotThrow(() -> new SimpleEnumDescriptorSimplifier(),
                "Constructor should not throw exception");
    }

    /**
     * Tests that the constructor can be called multiple times in sequence.
     * Verifies stability of the constructor when called repeatedly.
     */
    @Test
    public void testConstructorRepeatedInvocation() {
        // Act & Assert - Create multiple simplifiers in sequence
        for (int i = 0; i < 5; i++) {
            SimpleEnumDescriptorSimplifier simplifier = new SimpleEnumDescriptorSimplifier();
            assertNotNull(simplifier, "SimpleEnumDescriptorSimplifier should be created on iteration " + i);
            assertInstanceOf(ClassVisitor.class, simplifier,
                    "SimpleEnumDescriptorSimplifier should implement ClassVisitor on iteration " + i);
            assertInstanceOf(ConstantVisitor.class, simplifier,
                    "SimpleEnumDescriptorSimplifier should implement ConstantVisitor on iteration " + i);
            assertInstanceOf(MemberVisitor.class, simplifier,
                    "SimpleEnumDescriptorSimplifier should implement MemberVisitor on iteration " + i);
            assertInstanceOf(AttributeVisitor.class, simplifier,
                    "SimpleEnumDescriptorSimplifier should implement AttributeVisitor on iteration " + i);
            assertInstanceOf(LocalVariableInfoVisitor.class, simplifier,
                    "SimpleEnumDescriptorSimplifier should implement LocalVariableInfoVisitor on iteration " + i);
            assertInstanceOf(LocalVariableTypeInfoVisitor.class, simplifier,
                    "SimpleEnumDescriptorSimplifier should implement LocalVariableTypeInfoVisitor on iteration " + i);
        }
    }

    /**
     * Tests that the constructor properly initializes the SimpleEnumDescriptorSimplifier
     * to be used as a ClassVisitor.
     */
    @Test
    public void testConstructorInitializesForClassVisitorOperations() {
        // Act - Create SimpleEnumDescriptorSimplifier
        SimpleEnumDescriptorSimplifier simplifier = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify it can be used as ClassVisitor
        assertInstanceOf(ClassVisitor.class, simplifier,
                "Newly created simplifier should be usable as ClassVisitor");
    }

    /**
     * Tests that the constructor properly initializes the SimpleEnumDescriptorSimplifier
     * to be used as a ConstantVisitor.
     */
    @Test
    public void testConstructorInitializesForConstantVisitorOperations() {
        // Act - Create SimpleEnumDescriptorSimplifier
        SimpleEnumDescriptorSimplifier simplifier = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify it can be used as ConstantVisitor
        assertInstanceOf(ConstantVisitor.class, simplifier,
                "Newly created simplifier should be usable as ConstantVisitor");
    }

    /**
     * Tests that the constructor properly initializes the SimpleEnumDescriptorSimplifier
     * to be used as a MemberVisitor.
     */
    @Test
    public void testConstructorInitializesForMemberVisitorOperations() {
        // Act - Create SimpleEnumDescriptorSimplifier
        SimpleEnumDescriptorSimplifier simplifier = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify it can be used as MemberVisitor
        assertInstanceOf(MemberVisitor.class, simplifier,
                "Newly created simplifier should be usable as MemberVisitor");
    }

    /**
     * Tests that the constructor properly initializes the SimpleEnumDescriptorSimplifier
     * to be used as an AttributeVisitor.
     */
    @Test
    public void testConstructorInitializesForAttributeVisitorOperations() {
        // Act - Create SimpleEnumDescriptorSimplifier
        SimpleEnumDescriptorSimplifier simplifier = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify it can be used as AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, simplifier,
                "Newly created simplifier should be usable as AttributeVisitor");
    }

    /**
     * Tests that the constructor properly initializes the SimpleEnumDescriptorSimplifier
     * to be used as a LocalVariableInfoVisitor.
     */
    @Test
    public void testConstructorInitializesForLocalVariableInfoVisitorOperations() {
        // Act - Create SimpleEnumDescriptorSimplifier
        SimpleEnumDescriptorSimplifier simplifier = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify it can be used as LocalVariableInfoVisitor
        assertInstanceOf(LocalVariableInfoVisitor.class, simplifier,
                "Newly created simplifier should be usable as LocalVariableInfoVisitor");
    }

    /**
     * Tests that the constructor properly initializes the SimpleEnumDescriptorSimplifier
     * to be used as a LocalVariableTypeInfoVisitor.
     */
    @Test
    public void testConstructorInitializesForLocalVariableTypeInfoVisitorOperations() {
        // Act - Create SimpleEnumDescriptorSimplifier
        SimpleEnumDescriptorSimplifier simplifier = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify it can be used as LocalVariableTypeInfoVisitor
        assertInstanceOf(LocalVariableTypeInfoVisitor.class, simplifier,
                "Newly created simplifier should be usable as LocalVariableTypeInfoVisitor");
    }

    /**
     * Tests that the constructor creates instances with all required interfaces.
     * Verifies that the instance implements all visitor interfaces.
     */
    @Test
    public void testConstructorImplementsAllInterfaces() {
        // Act - Create SimpleEnumDescriptorSimplifier
        SimpleEnumDescriptorSimplifier simplifier = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify it implements all interfaces
        assertTrue(simplifier instanceof ClassVisitor,
                "SimpleEnumDescriptorSimplifier should implement ClassVisitor");
        assertTrue(simplifier instanceof ConstantVisitor,
                "SimpleEnumDescriptorSimplifier should implement ConstantVisitor");
        assertTrue(simplifier instanceof MemberVisitor,
                "SimpleEnumDescriptorSimplifier should implement MemberVisitor");
        assertTrue(simplifier instanceof AttributeVisitor,
                "SimpleEnumDescriptorSimplifier should implement AttributeVisitor");
        assertTrue(simplifier instanceof LocalVariableInfoVisitor,
                "SimpleEnumDescriptorSimplifier should implement LocalVariableInfoVisitor");
        assertTrue(simplifier instanceof LocalVariableTypeInfoVisitor,
                "SimpleEnumDescriptorSimplifier should implement LocalVariableTypeInfoVisitor");
    }

    /**
     * Tests that multiple instances created in sequence are all independent.
     * Verifies that each constructor call creates a new, distinct object.
     */
    @Test
    public void testConstructorCreatesIndependentInstances() {
        // Act - Create multiple instances
        SimpleEnumDescriptorSimplifier simplifier1 = new SimpleEnumDescriptorSimplifier();
        SimpleEnumDescriptorSimplifier simplifier2 = new SimpleEnumDescriptorSimplifier();
        SimpleEnumDescriptorSimplifier simplifier3 = new SimpleEnumDescriptorSimplifier();

        // Assert - Verify all instances are different
        assertNotSame(simplifier1, simplifier2, "simplifier1 and simplifier2 should be different");
        assertNotSame(simplifier1, simplifier3, "simplifier1 and simplifier3 should be different");
        assertNotSame(simplifier2, simplifier3, "simplifier2 and simplifier3 should be different");
    }

    /**
     * Tests that the constructor works correctly in a multi-threaded scenario.
     * Verifies that the constructor is thread-safe when called concurrently.
     */
    @Test
    public void testConstructorThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads that will instantiate SimpleEnumDescriptorSimplifier
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        SimpleEnumDescriptorSimplifier[] simplifiers = new SimpleEnumDescriptorSimplifier[threadCount];

        // Act - Create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                simplifiers[index] = new SimpleEnumDescriptorSimplifier();
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - Verify all instances were created successfully
        for (int i = 0; i < threadCount; i++) {
            assertNotNull(simplifiers[i], "Simplifier " + i + " should be created");
            assertInstanceOf(ClassVisitor.class, simplifiers[i],
                    "Simplifier " + i + " should implement ClassVisitor");
            assertInstanceOf(ConstantVisitor.class, simplifiers[i],
                    "Simplifier " + i + " should implement ConstantVisitor");
            assertInstanceOf(MemberVisitor.class, simplifiers[i],
                    "Simplifier " + i + " should implement MemberVisitor");
            assertInstanceOf(AttributeVisitor.class, simplifiers[i],
                    "Simplifier " + i + " should implement AttributeVisitor");
            assertInstanceOf(LocalVariableInfoVisitor.class, simplifiers[i],
                    "Simplifier " + i + " should implement LocalVariableInfoVisitor");
            assertInstanceOf(LocalVariableTypeInfoVisitor.class, simplifiers[i],
                    "Simplifier " + i + " should implement LocalVariableTypeInfoVisitor");
        }

        // Verify all instances are different
        for (int i = 0; i < threadCount; i++) {
            for (int j = i + 1; j < threadCount; j++) {
                assertNotSame(simplifiers[i], simplifiers[j],
                        "Simplifier " + i + " and " + j + " should be different instances");
            }
        }
    }
}

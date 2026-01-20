package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SimpleEnumClassSimplifier} default constructor.
 * Tests the SimpleEnumClassSimplifier() no-arg constructor.
 */
public class SimpleEnumClassSimplifierClaude_constructorTest {

    /**
     * Tests that the default constructor successfully creates a SimpleEnumClassSimplifier instance.
     * Verifies that the instance can be instantiated without errors.
     */
    @Test
    public void testDefaultConstructor() {
        // Act - Create SimpleEnumClassSimplifier with default constructor
        SimpleEnumClassSimplifier simplifier = new SimpleEnumClassSimplifier();

        // Assert - Verify the SimpleEnumClassSimplifier instance was created successfully
        assertNotNull(simplifier, "SimpleEnumClassSimplifier should be instantiated successfully");
    }

    /**
     * Tests that the created SimpleEnumClassSimplifier is a valid ClassVisitor.
     * Verifies that SimpleEnumClassSimplifier implements the ClassVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidClassVisitor() {
        // Act - Create SimpleEnumClassSimplifier
        SimpleEnumClassSimplifier simplifier = new SimpleEnumClassSimplifier();

        // Assert - Verify it implements ClassVisitor
        assertInstanceOf(ClassVisitor.class, simplifier,
                "SimpleEnumClassSimplifier should implement ClassVisitor");
    }

    /**
     * Tests that the created SimpleEnumClassSimplifier is a valid AttributeVisitor.
     * Verifies that SimpleEnumClassSimplifier implements the AttributeVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidAttributeVisitor() {
        // Act - Create SimpleEnumClassSimplifier
        SimpleEnumClassSimplifier simplifier = new SimpleEnumClassSimplifier();

        // Assert - Verify it implements AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, simplifier,
                "SimpleEnumClassSimplifier should implement AttributeVisitor");
    }

    /**
     * Tests that multiple SimpleEnumClassSimplifier instances can be created independently.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testMultipleSimpleEnumClassSimplifierInstances() {
        // Act - Create two SimpleEnumClassSimplifier instances
        SimpleEnumClassSimplifier simplifier1 = new SimpleEnumClassSimplifier();
        SimpleEnumClassSimplifier simplifier2 = new SimpleEnumClassSimplifier();

        // Assert - Verify both instances were created and are different
        assertNotNull(simplifier1, "First SimpleEnumClassSimplifier should be created");
        assertNotNull(simplifier2, "Second SimpleEnumClassSimplifier should be created");
        assertNotSame(simplifier1, simplifier2, "SimpleEnumClassSimplifier instances should be different objects");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     * Verifies exception-free construction.
     */
    @Test
    public void testConstructorDoesNotThrowException() {
        // Act & Assert - Verify no exception is thrown
        assertDoesNotThrow(() -> new SimpleEnumClassSimplifier(),
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
            SimpleEnumClassSimplifier simplifier = new SimpleEnumClassSimplifier();
            assertNotNull(simplifier, "SimpleEnumClassSimplifier should be created on iteration " + i);
            assertInstanceOf(ClassVisitor.class, simplifier,
                    "SimpleEnumClassSimplifier should implement ClassVisitor on iteration " + i);
            assertInstanceOf(AttributeVisitor.class, simplifier,
                    "SimpleEnumClassSimplifier should implement AttributeVisitor on iteration " + i);
        }
    }

    /**
     * Tests that the constructor properly initializes the SimpleEnumClassSimplifier
     * to be used as a ClassVisitor.
     */
    @Test
    public void testConstructorInitializesForClassVisitorOperations() {
        // Act - Create SimpleEnumClassSimplifier
        SimpleEnumClassSimplifier simplifier = new SimpleEnumClassSimplifier();

        // Assert - Verify it can be used as ClassVisitor
        assertInstanceOf(ClassVisitor.class, simplifier,
                "Newly created simplifier should be usable as ClassVisitor");
    }

    /**
     * Tests that the constructor properly initializes the SimpleEnumClassSimplifier
     * to be used as an AttributeVisitor.
     */
    @Test
    public void testConstructorInitializesForAttributeVisitorOperations() {
        // Act - Create SimpleEnumClassSimplifier
        SimpleEnumClassSimplifier simplifier = new SimpleEnumClassSimplifier();

        // Assert - Verify it can be used as AttributeVisitor
        assertInstanceOf(AttributeVisitor.class, simplifier,
                "Newly created simplifier should be usable as AttributeVisitor");
    }

    /**
     * Tests that the constructor creates instances with both required interfaces.
     * Verifies that the instance implements both ClassVisitor and AttributeVisitor.
     */
    @Test
    public void testConstructorImplementsBothInterfaces() {
        // Act - Create SimpleEnumClassSimplifier
        SimpleEnumClassSimplifier simplifier = new SimpleEnumClassSimplifier();

        // Assert - Verify it implements both interfaces
        assertTrue(simplifier instanceof ClassVisitor,
                "SimpleEnumClassSimplifier should implement ClassVisitor");
        assertTrue(simplifier instanceof AttributeVisitor,
                "SimpleEnumClassSimplifier should implement AttributeVisitor");
    }

    /**
     * Tests that multiple instances created in sequence are all independent.
     * Verifies that each constructor call creates a new, distinct object.
     */
    @Test
    public void testConstructorCreatesIndependentInstances() {
        // Act - Create multiple instances
        SimpleEnumClassSimplifier simplifier1 = new SimpleEnumClassSimplifier();
        SimpleEnumClassSimplifier simplifier2 = new SimpleEnumClassSimplifier();
        SimpleEnumClassSimplifier simplifier3 = new SimpleEnumClassSimplifier();

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
        // Arrange - Create multiple threads that will instantiate SimpleEnumClassSimplifier
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        SimpleEnumClassSimplifier[] simplifiers = new SimpleEnumClassSimplifier[threadCount];

        // Act - Create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                simplifiers[index] = new SimpleEnumClassSimplifier();
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
            assertInstanceOf(AttributeVisitor.class, simplifiers[i],
                    "Simplifier " + i + " should implement AttributeVisitor");
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

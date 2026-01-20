package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SimpleEnumArrayPropagator} default constructor.
 * Tests the SimpleEnumArrayPropagator() no-arg constructor.
 */
public class SimpleEnumArrayPropagatorClaude_constructorTest {

    /**
     * Tests that the default constructor successfully creates a SimpleEnumArrayPropagator instance.
     * Verifies that the instance can be instantiated without errors.
     */
    @Test
    public void testDefaultConstructor() {
        // Act - Create SimpleEnumArrayPropagator with default constructor
        SimpleEnumArrayPropagator propagator = new SimpleEnumArrayPropagator();

        // Assert - Verify the SimpleEnumArrayPropagator instance was created successfully
        assertNotNull(propagator, "SimpleEnumArrayPropagator should be instantiated successfully");
    }

    /**
     * Tests that the created SimpleEnumArrayPropagator is a valid ClassVisitor.
     * Verifies that SimpleEnumArrayPropagator implements the ClassVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidClassVisitor() {
        // Act - Create SimpleEnumArrayPropagator
        SimpleEnumArrayPropagator propagator = new SimpleEnumArrayPropagator();

        // Assert - Verify it implements ClassVisitor
        assertInstanceOf(ClassVisitor.class, propagator,
                "SimpleEnumArrayPropagator should implement ClassVisitor");
    }

    /**
     * Tests that the created SimpleEnumArrayPropagator is a valid MemberVisitor.
     * Verifies that SimpleEnumArrayPropagator implements the MemberVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidMemberVisitor() {
        // Act - Create SimpleEnumArrayPropagator
        SimpleEnumArrayPropagator propagator = new SimpleEnumArrayPropagator();

        // Assert - Verify it implements MemberVisitor
        assertInstanceOf(MemberVisitor.class, propagator,
                "SimpleEnumArrayPropagator should implement MemberVisitor");
    }

    /**
     * Tests that multiple SimpleEnumArrayPropagator instances can be created independently.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testMultipleSimpleEnumArrayPropagatorInstances() {
        // Act - Create two SimpleEnumArrayPropagator instances
        SimpleEnumArrayPropagator propagator1 = new SimpleEnumArrayPropagator();
        SimpleEnumArrayPropagator propagator2 = new SimpleEnumArrayPropagator();

        // Assert - Verify both instances were created and are different
        assertNotNull(propagator1, "First SimpleEnumArrayPropagator should be created");
        assertNotNull(propagator2, "Second SimpleEnumArrayPropagator should be created");
        assertNotSame(propagator1, propagator2, "SimpleEnumArrayPropagator instances should be different objects");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     * Verifies exception-free construction.
     */
    @Test
    public void testConstructorDoesNotThrowException() {
        // Act & Assert - Verify no exception is thrown
        assertDoesNotThrow(() -> new SimpleEnumArrayPropagator(),
                "Constructor should not throw exception");
    }

    /**
     * Tests that the constructor can be called multiple times in sequence.
     * Verifies stability of the constructor when called repeatedly.
     */
    @Test
    public void testConstructorRepeatedInvocation() {
        // Act & Assert - Create multiple propagators in sequence
        for (int i = 0; i < 5; i++) {
            SimpleEnumArrayPropagator propagator = new SimpleEnumArrayPropagator();
            assertNotNull(propagator, "SimpleEnumArrayPropagator should be created on iteration " + i);
            assertInstanceOf(ClassVisitor.class, propagator,
                    "SimpleEnumArrayPropagator should implement ClassVisitor on iteration " + i);
            assertInstanceOf(MemberVisitor.class, propagator,
                    "SimpleEnumArrayPropagator should implement MemberVisitor on iteration " + i);
        }
    }

    /**
     * Tests that the constructor properly initializes the SimpleEnumArrayPropagator
     * to be used as a ClassVisitor.
     */
    @Test
    public void testConstructorInitializesForClassVisitorOperations() {
        // Act - Create SimpleEnumArrayPropagator
        SimpleEnumArrayPropagator propagator = new SimpleEnumArrayPropagator();

        // Assert - Verify it can be used as ClassVisitor
        assertInstanceOf(ClassVisitor.class, propagator,
                "Newly created propagator should be usable as ClassVisitor");
    }

    /**
     * Tests that the constructor properly initializes the SimpleEnumArrayPropagator
     * to be used as a MemberVisitor.
     */
    @Test
    public void testConstructorInitializesForMemberVisitorOperations() {
        // Act - Create SimpleEnumArrayPropagator
        SimpleEnumArrayPropagator propagator = new SimpleEnumArrayPropagator();

        // Assert - Verify it can be used as MemberVisitor
        assertInstanceOf(MemberVisitor.class, propagator,
                "Newly created propagator should be usable as MemberVisitor");
    }

    /**
     * Tests that the constructor creates instances with both required interfaces.
     * Verifies that the instance implements both ClassVisitor and MemberVisitor.
     */
    @Test
    public void testConstructorImplementsBothInterfaces() {
        // Act - Create SimpleEnumArrayPropagator
        SimpleEnumArrayPropagator propagator = new SimpleEnumArrayPropagator();

        // Assert - Verify it implements both interfaces
        assertTrue(propagator instanceof ClassVisitor,
                "SimpleEnumArrayPropagator should implement ClassVisitor");
        assertTrue(propagator instanceof MemberVisitor,
                "SimpleEnumArrayPropagator should implement MemberVisitor");
    }

    /**
     * Tests that multiple instances created in sequence are all independent.
     * Verifies that each constructor call creates a new, distinct object.
     */
    @Test
    public void testConstructorCreatesIndependentInstances() {
        // Act - Create multiple instances
        SimpleEnumArrayPropagator propagator1 = new SimpleEnumArrayPropagator();
        SimpleEnumArrayPropagator propagator2 = new SimpleEnumArrayPropagator();
        SimpleEnumArrayPropagator propagator3 = new SimpleEnumArrayPropagator();

        // Assert - Verify all instances are different
        assertNotSame(propagator1, propagator2, "propagator1 and propagator2 should be different");
        assertNotSame(propagator1, propagator3, "propagator1 and propagator3 should be different");
        assertNotSame(propagator2, propagator3, "propagator2 and propagator3 should be different");
    }

    /**
     * Tests that the constructor works correctly in a multi-threaded scenario.
     * Verifies that the constructor is thread-safe when called concurrently.
     */
    @Test
    public void testConstructorThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads that will instantiate SimpleEnumArrayPropagator
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        SimpleEnumArrayPropagator[] propagators = new SimpleEnumArrayPropagator[threadCount];

        // Act - Create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                propagators[index] = new SimpleEnumArrayPropagator();
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - Verify all instances were created successfully
        for (int i = 0; i < threadCount; i++) {
            assertNotNull(propagators[i], "Propagator " + i + " should be created");
            assertInstanceOf(ClassVisitor.class, propagators[i],
                    "Propagator " + i + " should implement ClassVisitor");
            assertInstanceOf(MemberVisitor.class, propagators[i],
                    "Propagator " + i + " should implement MemberVisitor");
        }

        // Verify all instances are different
        for (int i = 0; i < threadCount; i++) {
            for (int j = i + 1; j < threadCount; j++) {
                assertNotSame(propagators[i], propagators[j],
                        "Propagator " + i + " and " + j + " should be different instances");
            }
        }
    }
}

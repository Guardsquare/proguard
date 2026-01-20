package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SimpleEnumClassChecker} default constructor.
 * Tests the SimpleEnumClassChecker() no-arg constructor.
 */
public class SimpleEnumClassCheckerClaude_constructorTest {

    /**
     * Tests that the default constructor successfully creates a SimpleEnumClassChecker instance.
     * Verifies that the instance can be instantiated without errors.
     */
    @Test
    public void testDefaultConstructor() {
        // Act - Create SimpleEnumClassChecker with default constructor
        SimpleEnumClassChecker checker = new SimpleEnumClassChecker();

        // Assert - Verify the SimpleEnumClassChecker instance was created successfully
        assertNotNull(checker, "SimpleEnumClassChecker should be instantiated successfully");
    }

    /**
     * Tests that the created SimpleEnumClassChecker is a valid ClassVisitor.
     * Verifies that SimpleEnumClassChecker implements the ClassVisitor interface.
     */
    @Test
    public void testConstructorCreatesValidClassVisitor() {
        // Act - Create SimpleEnumClassChecker
        SimpleEnumClassChecker checker = new SimpleEnumClassChecker();

        // Assert - Verify it implements ClassVisitor
        assertInstanceOf(ClassVisitor.class, checker,
                "SimpleEnumClassChecker should implement ClassVisitor");
    }

    /**
     * Tests that multiple SimpleEnumClassChecker instances can be created independently.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testMultipleSimpleEnumClassCheckerInstances() {
        // Act - Create two SimpleEnumClassChecker instances
        SimpleEnumClassChecker checker1 = new SimpleEnumClassChecker();
        SimpleEnumClassChecker checker2 = new SimpleEnumClassChecker();

        // Assert - Verify both instances were created and are different
        assertNotNull(checker1, "First SimpleEnumClassChecker should be created");
        assertNotNull(checker2, "Second SimpleEnumClassChecker should be created");
        assertNotSame(checker1, checker2, "SimpleEnumClassChecker instances should be different objects");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     * Verifies exception-free construction.
     */
    @Test
    public void testConstructorDoesNotThrowException() {
        // Act & Assert - Verify no exception is thrown
        assertDoesNotThrow(() -> new SimpleEnumClassChecker(),
                "Constructor should not throw exception");
    }

    /**
     * Tests that the constructor can be called multiple times in sequence.
     * Verifies stability of the constructor when called repeatedly.
     */
    @Test
    public void testConstructorRepeatedInvocation() {
        // Act & Assert - Create multiple checkers in sequence
        for (int i = 0; i < 5; i++) {
            SimpleEnumClassChecker checker = new SimpleEnumClassChecker();
            assertNotNull(checker, "SimpleEnumClassChecker should be created on iteration " + i);
            assertInstanceOf(ClassVisitor.class, checker,
                    "SimpleEnumClassChecker should implement ClassVisitor on iteration " + i);
        }
    }

    /**
     * Tests that the constructor properly initializes the SimpleEnumClassChecker
     * to be used as a ClassVisitor.
     */
    @Test
    public void testConstructorInitializesForClassVisitorOperations() {
        // Act - Create SimpleEnumClassChecker
        SimpleEnumClassChecker checker = new SimpleEnumClassChecker();

        // Assert - Verify it can be used as ClassVisitor
        assertInstanceOf(ClassVisitor.class, checker,
                "Newly created checker should be usable as ClassVisitor");
    }

    /**
     * Tests that multiple instances created in sequence are all independent.
     * Verifies that each constructor call creates a new, distinct object.
     */
    @Test
    public void testConstructorCreatesIndependentInstances() {
        // Act - Create multiple instances
        SimpleEnumClassChecker checker1 = new SimpleEnumClassChecker();
        SimpleEnumClassChecker checker2 = new SimpleEnumClassChecker();
        SimpleEnumClassChecker checker3 = new SimpleEnumClassChecker();

        // Assert - Verify all instances are different
        assertNotSame(checker1, checker2, "checker1 and checker2 should be different");
        assertNotSame(checker1, checker3, "checker1 and checker3 should be different");
        assertNotSame(checker2, checker3, "checker2 and checker3 should be different");
    }

    /**
     * Tests that the constructor works correctly in a multi-threaded scenario.
     * Verifies that the constructor is thread-safe when called concurrently.
     */
    @Test
    public void testConstructorThreadSafety() throws InterruptedException {
        // Arrange - Create multiple threads that will instantiate SimpleEnumClassChecker
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        SimpleEnumClassChecker[] checkers = new SimpleEnumClassChecker[threadCount];

        // Act - Create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                checkers[index] = new SimpleEnumClassChecker();
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - Verify all instances were created successfully
        for (int i = 0; i < threadCount; i++) {
            assertNotNull(checkers[i], "Checker " + i + " should be created");
            assertInstanceOf(ClassVisitor.class, checkers[i],
                    "Checker " + i + " should implement ClassVisitor");
        }

        // Verify all instances are different
        for (int i = 0; i < threadCount; i++) {
            for (int j = i + 1; j < threadCount; j++) {
                assertNotSame(checkers[i], checkers[j],
                        "Checker " + i + " and " + j + " should be different instances");
            }
        }
    }

    /**
     * Tests that the created SimpleEnumClassChecker instance is a ClassVisitor.
     * Verifies the instance can be assigned to a ClassVisitor reference.
     */
    @Test
    public void testConstructorImplementsClassVisitor() {
        // Act - Create SimpleEnumClassChecker
        SimpleEnumClassChecker checker = new SimpleEnumClassChecker();

        // Assert - Verify it is a ClassVisitor
        assertTrue(checker instanceof ClassVisitor,
                "SimpleEnumClassChecker should implement ClassVisitor");
    }

    /**
     * Tests that the constructor can be invoked repeatedly without side effects.
     * Verifies that creating multiple instances doesn't cause issues.
     */
    @Test
    public void testConstructorHasNoSideEffects() {
        // Act - Create multiple checkers
        SimpleEnumClassChecker checker1 = new SimpleEnumClassChecker();
        SimpleEnumClassChecker checker2 = new SimpleEnumClassChecker();
        SimpleEnumClassChecker checker3 = new SimpleEnumClassChecker();

        // Assert - Verify all instances are valid
        assertNotNull(checker1, "First checker should be created");
        assertNotNull(checker2, "Second checker should be created");
        assertNotNull(checker3, "Third checker should be created");
    }

    /**
     * Tests that the constructor produces consistent results across multiple invocations.
     * Verifies that every instance created is a valid ClassVisitor.
     */
    @Test
    public void testConstructorConsistency() {
        // Act & Assert - Verify consistency across multiple invocations
        for (int i = 0; i < 10; i++) {
            SimpleEnumClassChecker checker = new SimpleEnumClassChecker();
            assertNotNull(checker, "Checker should be created on iteration " + i);
            assertTrue(checker instanceof ClassVisitor,
                    "Checker should be a ClassVisitor on iteration " + i);
        }
    }

    /**
     * Tests that the constructor properly initializes internal state.
     * Verifies that a newly created instance can be used as a ClassVisitor.
     */
    @Test
    public void testConstructorInitializesInternalState() {
        // Act - Create SimpleEnumClassChecker
        SimpleEnumClassChecker checker = new SimpleEnumClassChecker();

        // Assert - Verify internal state allows it to be used as ClassVisitor
        assertNotNull(checker, "Checker should be created");
        ClassVisitor visitor = checker;
        assertNotNull(visitor, "Checker should be assignable to ClassVisitor");
    }
}

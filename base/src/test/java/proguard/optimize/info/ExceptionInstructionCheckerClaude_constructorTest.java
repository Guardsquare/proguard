package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ExceptionInstructionChecker} default constructor.
 *
 * The ExceptionInstructionChecker has an implicit default no-arg constructor since no constructor
 * is explicitly defined. This constructor simply creates a new instance with no initialization logic.
 */
public class ExceptionInstructionCheckerClaude_constructorTest {

    /**
     * Tests that the default constructor successfully creates an ExceptionInstructionChecker instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        ExceptionInstructionChecker checker = new ExceptionInstructionChecker();

        // Assert
        assertNotNull(checker, "ExceptionInstructionChecker instance should be created");
    }

    /**
     * Tests that the constructed ExceptionInstructionChecker is an instance of InstructionVisitor.
     */
    @Test
    public void testConstructor_createsInstructionVisitor() {
        // Act
        ExceptionInstructionChecker checker = new ExceptionInstructionChecker();

        // Assert
        assertInstanceOf(InstructionVisitor.class, checker,
                "ExceptionInstructionChecker should be an instance of InstructionVisitor");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        ExceptionInstructionChecker checker1 = new ExceptionInstructionChecker();
        ExceptionInstructionChecker checker2 = new ExceptionInstructionChecker();
        ExceptionInstructionChecker checker3 = new ExceptionInstructionChecker();

        // Assert
        assertNotNull(checker1, "First checker should be created");
        assertNotNull(checker2, "Second checker should be created");
        assertNotNull(checker3, "Third checker should be created");
        assertNotSame(checker1, checker2, "First and second checkers should be different instances");
        assertNotSame(checker2, checker3, "Second and third checkers should be different instances");
        assertNotSame(checker1, checker3, "First and third checkers should be different instances");
    }

    /**
     * Tests that the constructor completes quickly without expensive initialization.
     */
    @Test
    public void testConstructor_completesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - create many instances
        for (int i = 0; i < 1000; i++) {
            new ExceptionInstructionChecker();
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 instances)
        assertTrue(durationMs < 100, "Constructor should execute quickly with minimal overhead");
    }

    /**
     * Tests that the constructor is thread-safe and can be called concurrently.
     */
    @Test
    public void testConstructor_threadSafe() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final int instancesPerThread = 100;
        Thread[] threads = new Thread[threadCount];
        final ExceptionInstructionChecker[][] results = new ExceptionInstructionChecker[threadCount][instancesPerThread];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < instancesPerThread; j++) {
                    results[threadIndex][j] = new ExceptionInstructionChecker();
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify all instances were created successfully
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < instancesPerThread; j++) {
                assertNotNull(results[i][j],
                        "Instance [" + i + "][" + j + "] should be created");
            }
        }
    }

    /**
     * Tests that constructed instances can be used as InstructionVisitor immediately.
     */
    @Test
    public void testConstructor_instanceIsUsableImmediately() {
        // Act
        ExceptionInstructionChecker checker = new ExceptionInstructionChecker();

        // Assert - should be able to call visitor methods without NPE
        assertDoesNotThrow(() -> checker.visitAnyInstruction(null, null, null, 0, null),
                "Newly constructed checker should be usable as an InstructionVisitor");
    }

    /**
     * Tests that the constructor creates instances that are equal by type but not by reference.
     */
    @Test
    public void testConstructor_createsSeparateInstances() {
        // Act
        ExceptionInstructionChecker checker1 = new ExceptionInstructionChecker();
        ExceptionInstructionChecker checker2 = new ExceptionInstructionChecker();

        // Assert
        assertEquals(checker1.getClass(), checker2.getClass(),
                "Both instances should be of the same class");
        assertNotSame(checker1, checker2,
                "Instances should be separate objects in memory");
    }

    /**
     * Tests that the constructor can be called repeatedly in quick succession.
     */
    @Test
    public void testConstructor_rapidSuccession() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                new ExceptionInstructionChecker();
            }
        }, "Constructor should handle rapid successive calls");
    }

    /**
     * Tests that constructed checkers are distinct objects that can be stored in collections.
     */
    @Test
    public void testConstructor_instancesAreCollectable() {
        // Arrange
        java.util.Set<ExceptionInstructionChecker> checkers = new java.util.HashSet<>();

        // Act - create multiple instances and add to collection
        for (int i = 0; i < 10; i++) {
            checkers.add(new ExceptionInstructionChecker());
        }

        // Assert - all instances should be distinct
        assertEquals(10, checkers.size(),
                "All 10 instances should be distinct and stored in the set");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new ExceptionInstructionChecker(),
                "Constructor should not throw any exception");
    }
}

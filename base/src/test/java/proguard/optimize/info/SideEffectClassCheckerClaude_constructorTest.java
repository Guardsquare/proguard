package proguard.optimize.info;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SideEffectClassChecker} default constructor.
 *
 * The SideEffectClassChecker has an implicit default no-arg constructor since no constructor
 * is explicitly defined. This is a utility class with static methods, but the constructor
 * can still be invoked to create instances.
 */
public class SideEffectClassCheckerClaude_constructorTest {

    /**
     * Tests that the default constructor successfully creates a SideEffectClassChecker instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        SideEffectClassChecker checker = new SideEffectClassChecker();

        // Assert
        assertNotNull(checker, "SideEffectClassChecker instance should be created");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        SideEffectClassChecker checker1 = new SideEffectClassChecker();
        SideEffectClassChecker checker2 = new SideEffectClassChecker();
        SideEffectClassChecker checker3 = new SideEffectClassChecker();

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
            new SideEffectClassChecker();
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
        final SideEffectClassChecker[][] results = new SideEffectClassChecker[threadCount][instancesPerThread];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < instancesPerThread; j++) {
                    results[threadIndex][j] = new SideEffectClassChecker();
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
     * Tests that the constructor creates instances that are equal by type but not by reference.
     */
    @Test
    public void testConstructor_createsSeparateInstances() {
        // Act
        SideEffectClassChecker checker1 = new SideEffectClassChecker();
        SideEffectClassChecker checker2 = new SideEffectClassChecker();

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
                new SideEffectClassChecker();
            }
        }, "Constructor should handle rapid successive calls");
    }

    /**
     * Tests that constructed checkers are distinct objects that can be stored in collections.
     */
    @Test
    public void testConstructor_instancesAreCollectable() {
        // Arrange
        java.util.Set<SideEffectClassChecker> checkers = new java.util.HashSet<>();

        // Act - create multiple instances and add to collection
        for (int i = 0; i < 10; i++) {
            checkers.add(new SideEffectClassChecker());
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
        assertDoesNotThrow(() -> new SideEffectClassChecker(),
                "Constructor should not throw any exception");
    }

    /**
     * Tests that constructed instances are of the correct type.
     */
    @Test
    public void testConstructor_createsCorrectType() {
        // Act
        SideEffectClassChecker checker = new SideEffectClassChecker();

        // Assert
        assertInstanceOf(SideEffectClassChecker.class, checker,
                "Instance should be of type SideEffectClassChecker");
    }

    /**
     * Tests that the constructor creates instances with Object as superclass.
     */
    @Test
    public void testConstructor_extendsObject() {
        // Act
        SideEffectClassChecker checker = new SideEffectClassChecker();

        // Assert
        assertInstanceOf(Object.class, checker,
                "SideEffectClassChecker should extend Object");
        assertEquals(Object.class, checker.getClass().getSuperclass(),
                "SideEffectClassChecker should have Object as direct superclass");
    }
}

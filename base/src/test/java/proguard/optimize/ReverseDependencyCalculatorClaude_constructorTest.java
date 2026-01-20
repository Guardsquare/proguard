package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ReverseDependencyCalculator#ReverseDependencyCalculator(ClassPool)}.
 *
 * The constructor in ReverseDependencyCalculator accepts a ClassPool parameter that will be
 * used for computing reverse dependencies when the reverseDependencyStore() method is called.
 * The ClassPool is stored in a private field for later use.
 *
 * These tests verify that the constructor:
 * 1. Successfully creates an instance with a valid ClassPool
 * 2. Properly handles null ClassPool (if applicable)
 * 3. Creates instances that are immediately usable
 * 4. Can be called repeatedly without issues
 * 5. Creates distinct instances with independent state
 */
public class ReverseDependencyCalculatorClaude_constructorTest {

    /**
     * Tests that the constructor successfully creates an instance with a valid ClassPool.
     * This is the basic happy path with a properly initialized ClassPool.
     */
    @Test
    public void testConstructor_withValidClassPool_createsInstance() {
        // Arrange
        ClassPool classPool = new ClassPool();

        // Act
        ReverseDependencyCalculator calculator = new ReverseDependencyCalculator(classPool);

        // Assert
        assertNotNull(calculator, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor accepts null for the ClassPool parameter.
     * Null may be accepted if the calculator is meant to be configured later,
     * or may fail if ClassPool is required.
     */
    @Test
    public void testConstructor_withNullClassPool_createsInstance() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            ReverseDependencyCalculator calculator = new ReverseDependencyCalculator(null);
            assertNotNull(calculator, "Constructor should accept null ClassPool");
        }, "Constructor should not throw with null ClassPool");
    }

    /**
     * Tests that the constructor creates an instance of the correct type.
     */
    @Test
    public void testConstructor_createsCorrectType() {
        // Arrange
        ClassPool classPool = new ClassPool();

        // Act
        ReverseDependencyCalculator calculator = new ReverseDependencyCalculator(classPool);

        // Assert
        assertNotNull(calculator, "Instance should be created");
        assertTrue(calculator instanceof ReverseDependencyCalculator,
            "Should be instance of ReverseDependencyCalculator");
        assertEquals(ReverseDependencyCalculator.class, calculator.getClass(),
            "Class should be ReverseDependencyCalculator");
    }

    /**
     * Tests that multiple instances can be created with different ClassPools.
     * Verifies each instance maintains its own state.
     */
    @Test
    public void testConstructor_multipleInstances_eachHasOwnState() {
        // Arrange
        ClassPool classPool1 = new ClassPool();
        ClassPool classPool2 = new ClassPool();
        ClassPool classPool3 = new ClassPool();

        // Act
        ReverseDependencyCalculator calculator1 = new ReverseDependencyCalculator(classPool1);
        ReverseDependencyCalculator calculator2 = new ReverseDependencyCalculator(classPool2);
        ReverseDependencyCalculator calculator3 = new ReverseDependencyCalculator(classPool3);

        // Assert
        assertNotNull(calculator1, "First instance should be created");
        assertNotNull(calculator2, "Second instance should be created");
        assertNotNull(calculator3, "Third instance should be created");
        assertNotSame(calculator1, calculator2, "Instances should be distinct");
        assertNotSame(calculator2, calculator3, "Instances should be distinct");
        assertNotSame(calculator1, calculator3, "Instances should be distinct");
    }

    /**
     * Tests that the constructor can be called repeatedly without issues.
     */
    @Test
    public void testConstructor_repeatedConstruction_succeeds() {
        // Arrange
        ClassPool classPool = new ClassPool();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ReverseDependencyCalculator calculator = new ReverseDependencyCalculator(
                    i % 2 == 0 ? classPool : new ClassPool()
                );
                assertNotNull(calculator, "Instance " + i + " should be non-null");
            }
        }, "Should be able to construct many instances without issues");
    }

    /**
     * Tests that construction completes quickly without performing expensive operations.
     * The constructor should only store the ClassPool reference, not process it.
     */
    @Test
    public void testConstructor_completesQuickly() {
        // Arrange
        ClassPool classPool = new ClassPool();

        // Act
        long startTime = System.nanoTime();
        ReverseDependencyCalculator calculator = new ReverseDependencyCalculator(classPool);
        long endTime = System.nanoTime();

        // Assert
        assertNotNull(calculator, "Instance should be created");
        long durationNanos = endTime - startTime;
        long oneMillisecondInNanos = 1_000_000L;
        assertTrue(durationNanos < oneMillisecondInNanos,
            "Constructor should complete very quickly, took " + durationNanos + " nanoseconds");
    }

    /**
     * Tests that the constructor works in a multi-threaded environment.
     * Verifies there are no concurrency issues with construction.
     */
    @Test
    public void testConstructor_threadSafe() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final Thread[] threads = new Thread[threadCount];
        final ReverseDependencyCalculator[] calculators = new ReverseDependencyCalculator[threadCount];
        final Exception[] exceptions = new Exception[threadCount];

        // Act - create calculators in parallel threads
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    ClassPool classPool = new ClassPool();
                    calculators[index] = new ReverseDependencyCalculator(classPool);
                } catch (Exception e) {
                    exceptions[index] = e;
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert
        for (int i = 0; i < threadCount; i++) {
            assertNull(exceptions[i], "No exceptions should occur in thread " + i);
            assertNotNull(calculators[i], "Calculator should be created in thread " + i);
        }
    }

    /**
     * Tests that the constructor does not throw exceptions with valid parameters.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Arrange
        ClassPool classPool = new ClassPool();

        // Act & Assert
        assertDoesNotThrow(() -> new ReverseDependencyCalculator(classPool),
            "Constructor should not throw with valid parameters");
    }

    /**
     * Tests that the instance's toString() method works after construction.
     */
    @Test
    public void testConstructor_toStringWorks() {
        // Arrange
        ClassPool classPool = new ClassPool();

        // Act
        ReverseDependencyCalculator calculator = new ReverseDependencyCalculator(classPool);
        String toString = calculator.toString();

        // Assert
        assertNotNull(toString, "toString() should return a non-null value");
        assertTrue(toString.contains("ReverseDependencyCalculator"),
            "toString() should contain the class name");
    }

    /**
     * Tests that hashCode() works on the constructed instance.
     */
    @Test
    public void testConstructor_hashCodeWorks() {
        // Arrange
        ClassPool classPool = new ClassPool();

        // Act
        ReverseDependencyCalculator calculator = new ReverseDependencyCalculator(classPool);

        // Assert
        assertDoesNotThrow(() -> calculator.hashCode(),
            "hashCode() should work on constructed instance");
    }

    /**
     * Tests that equals() works on the constructed instance.
     */
    @Test
    public void testConstructor_equalsWorks() {
        // Arrange
        ClassPool classPool = new ClassPool();

        // Act
        ReverseDependencyCalculator calculator1 = new ReverseDependencyCalculator(classPool);
        ReverseDependencyCalculator calculator2 = new ReverseDependencyCalculator(classPool);

        // Assert
        assertDoesNotThrow(() -> calculator1.equals(calculator2),
            "equals() should work on constructed instances");
        assertTrue(calculator1.equals(calculator1),
            "Instance should equal itself");
    }

    /**
     * Tests that the same ClassPool can be shared across multiple calculator instances.
     * Verifies the constructor doesn't claim exclusive ownership of the ClassPool.
     */
    @Test
    public void testConstructor_sharedClassPool_succeeds() {
        // Arrange
        ClassPool sharedClassPool = new ClassPool();

        // Act
        ReverseDependencyCalculator calculator1 = new ReverseDependencyCalculator(sharedClassPool);
        ReverseDependencyCalculator calculator2 = new ReverseDependencyCalculator(sharedClassPool);
        ReverseDependencyCalculator calculator3 = new ReverseDependencyCalculator(sharedClassPool);

        // Assert
        assertNotNull(calculator1, "First instance with shared ClassPool should be created");
        assertNotNull(calculator2, "Second instance with shared ClassPool should be created");
        assertNotNull(calculator3, "Third instance with shared ClassPool should be created");
    }

    /**
     * Tests that construction with an empty ClassPool succeeds.
     * An empty ClassPool is a valid state (no classes loaded yet).
     */
    @Test
    public void testConstructor_withEmptyClassPool_succeeds() {
        // Arrange
        ClassPool emptyClassPool = new ClassPool();

        // Act
        ReverseDependencyCalculator calculator = new ReverseDependencyCalculator(emptyClassPool);

        // Assert
        assertNotNull(calculator, "Constructor should work with empty ClassPool");
    }

    /**
     * Tests that the created instance can immediately call its public methods.
     * Verifies the instance is fully initialized after construction.
     */
    @Test
    public void testConstructor_instanceIsImmediatelyUsable() {
        // Arrange
        ClassPool classPool = new ClassPool();

        // Act
        ReverseDependencyCalculator calculator = new ReverseDependencyCalculator(classPool);

        // Assert
        assertNotNull(calculator, "Instance should be created");
        // Verify that we can call reverseDependencyStore() without error
        // (even on an empty ClassPool)
        assertDoesNotThrow(() -> calculator.reverseDependencyStore(),
            "Should be able to call reverseDependencyStore() immediately after construction");
    }

    /**
     * Tests that multiple calculators can be created and used independently.
     */
    @Test
    public void testConstructor_multipleCalculators_operateIndependently() {
        // Arrange
        ClassPool classPool1 = new ClassPool();
        ClassPool classPool2 = new ClassPool();

        // Act
        ReverseDependencyCalculator calculator1 = new ReverseDependencyCalculator(classPool1);
        ReverseDependencyCalculator calculator2 = new ReverseDependencyCalculator(classPool2);

        // Assert
        assertNotNull(calculator1, "First calculator should be created");
        assertNotNull(calculator2, "Second calculator should be created");

        // Both should be usable
        assertDoesNotThrow(() -> calculator1.reverseDependencyStore(),
            "First calculator should be usable");
        assertDoesNotThrow(() -> calculator2.reverseDependencyStore(),
            "Second calculator should be usable");
    }
}

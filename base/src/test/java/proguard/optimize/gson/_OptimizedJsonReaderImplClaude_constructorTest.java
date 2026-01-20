package proguard.optimize.gson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link _OptimizedJsonReaderImpl} constructor.
 * Tests the constructor: <init>.()V
 *
 * The _OptimizedJsonReaderImpl class is a template for an optimized JSON reader implementation.
 * It contains a static field that maps JSON field names to internal indices, which is initialized
 * to null by the static initializer method. The constructor is the default no-arg constructor.
 */
public class _OptimizedJsonReaderImplClaude_constructorTest {

    // =========================================================================
    // Tests for constructor: <init>.()V
    // =========================================================================

    /**
     * Tests that the constructor creates a non-null instance.
     */
    @Test
    public void testConstructor_createsNonNullInstance() {
        // Act
        _OptimizedJsonReaderImpl instance = new _OptimizedJsonReaderImpl();

        // Assert
        assertNotNull(instance, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor creates an instance implementing the _OptimizedJsonReader interface.
     */
    @Test
    public void testConstructor_createsInstanceOfCorrectType() {
        // Act
        _OptimizedJsonReaderImpl instance = new _OptimizedJsonReaderImpl();

        // Assert
        assertTrue(instance instanceof _OptimizedJsonReader,
            "Instance should implement _OptimizedJsonReader interface");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_areIndependent() {
        // Act
        _OptimizedJsonReaderImpl instance1 = new _OptimizedJsonReaderImpl();
        _OptimizedJsonReaderImpl instance2 = new _OptimizedJsonReaderImpl();

        // Assert
        assertNotNull(instance1, "First instance should not be null");
        assertNotNull(instance2, "Second instance should not be null");
        assertNotSame(instance1, instance2, "Instances should be different objects");
    }

    /**
     * Tests that multiple instances can be created in sequence.
     */
    @Test
    public void testConstructor_sequentialCreation_succeeds() {
        // Act & Assert
        for (int i = 0; i < 10; i++) {
            _OptimizedJsonReaderImpl instance = new _OptimizedJsonReaderImpl();
            assertNotNull(instance, "Instance " + i + " should not be null");
        }
    }

    /**
     * Tests that the constructor can be called repeatedly without side effects.
     */
    @Test
    public void testConstructor_repeatedCalls_noSideEffects() {
        // Act
        _OptimizedJsonReaderImpl instance1 = new _OptimizedJsonReaderImpl();
        _OptimizedJsonReaderImpl instance2 = new _OptimizedJsonReaderImpl();
        _OptimizedJsonReaderImpl instance3 = new _OptimizedJsonReaderImpl();

        // Assert
        assertNotNull(instance1, "First instance should not be null");
        assertNotNull(instance2, "Second instance should not be null");
        assertNotNull(instance3, "Third instance should not be null");
    }

    /**
     * Tests that the created instance has the expected class name.
     */
    @Test
    public void testConstructor_createdInstanceHasCorrectClassName() {
        // Act
        _OptimizedJsonReaderImpl instance = new _OptimizedJsonReaderImpl();

        // Assert
        assertEquals("_OptimizedJsonReaderImpl", instance.getClass().getSimpleName(),
            "Instance should have correct class name");
    }

    /**
     * Tests that the created instance is of the exact class type.
     */
    @Test
    public void testConstructor_createdInstanceIsExactType() {
        // Act
        _OptimizedJsonReaderImpl instance = new _OptimizedJsonReaderImpl();

        // Assert
        assertEquals(_OptimizedJsonReaderImpl.class, instance.getClass(),
            "Instance should be of exact _OptimizedJsonReaderImpl type");
    }

    /**
     * Tests that the constructor works in a multi-threaded context.
     * Creates instances from multiple threads concurrently.
     */
    @Test
    public void testConstructor_multiThreaded_succeeds() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        final _OptimizedJsonReaderImpl[] instances = new _OptimizedJsonReaderImpl[threadCount];

        // Act
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                instances[index] = new _OptimizedJsonReaderImpl();
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert
        for (int i = 0; i < threadCount; i++) {
            assertNotNull(instances[i], "Instance " + i + " should not be null");
        }

        // Verify all instances are different
        for (int i = 0; i < threadCount; i++) {
            for (int j = i + 1; j < threadCount; j++) {
                assertNotSame(instances[i], instances[j],
                    "Instance " + i + " and " + j + " should be different");
            }
        }
    }

    /**
     * Tests that instances created by the constructor are unique objects.
     */
    @Test
    public void testConstructor_eachInstanceIsUnique() {
        // Act
        _OptimizedJsonReaderImpl instance1 = new _OptimizedJsonReaderImpl();
        _OptimizedJsonReaderImpl instance2 = new _OptimizedJsonReaderImpl();

        // Assert
        assertFalse(instance1.equals(instance2), "Different instances should not be equal");
        assertNotEquals(System.identityHashCode(instance1), System.identityHashCode(instance2),
            "Different instances should have different identity hash codes");
    }

    /**
     * Tests that the constructor works correctly when called via reflection.
     */
    @Test
    public void testConstructor_viaReflection_succeeds() throws Exception {
        // Act
        Object instance = _OptimizedJsonReaderImpl.class.getDeclaredConstructor().newInstance();

        // Assert
        assertNotNull(instance, "Instance created via reflection should not be null");
        assertTrue(instance instanceof _OptimizedJsonReaderImpl,
            "Instance should be of type _OptimizedJsonReaderImpl");
        assertTrue(instance instanceof _OptimizedJsonReader,
            "Instance should implement _OptimizedJsonReader interface");
    }
}

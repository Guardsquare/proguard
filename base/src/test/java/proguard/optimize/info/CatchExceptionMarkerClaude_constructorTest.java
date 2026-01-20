package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link CatchExceptionMarker} default constructor.
 *
 * The CatchExceptionMarker has an implicit default no-arg constructor since no constructor
 * is explicitly defined. This constructor simply creates a new instance with no initialization logic.
 */
public class CatchExceptionMarkerClaude_constructorTest {

    /**
     * Tests that the default constructor successfully creates a CatchExceptionMarker instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        CatchExceptionMarker marker = new CatchExceptionMarker();

        // Assert
        assertNotNull(marker, "CatchExceptionMarker instance should be created");
    }

    /**
     * Tests that the constructed CatchExceptionMarker is an instance of AttributeVisitor.
     */
    @Test
    public void testConstructor_createsAttributeVisitor() {
        // Act
        CatchExceptionMarker marker = new CatchExceptionMarker();

        // Assert
        assertInstanceOf(AttributeVisitor.class, marker,
                "CatchExceptionMarker should be an instance of AttributeVisitor");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        CatchExceptionMarker marker1 = new CatchExceptionMarker();
        CatchExceptionMarker marker2 = new CatchExceptionMarker();
        CatchExceptionMarker marker3 = new CatchExceptionMarker();

        // Assert
        assertNotNull(marker1, "First marker should be created");
        assertNotNull(marker2, "Second marker should be created");
        assertNotNull(marker3, "Third marker should be created");
        assertNotSame(marker1, marker2, "First and second markers should be different instances");
        assertNotSame(marker2, marker3, "Second and third markers should be different instances");
        assertNotSame(marker1, marker3, "First and third markers should be different instances");
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
            new CatchExceptionMarker();
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
        final CatchExceptionMarker[][] results = new CatchExceptionMarker[threadCount][instancesPerThread];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < instancesPerThread; j++) {
                    results[threadIndex][j] = new CatchExceptionMarker();
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
     * Tests that constructed instances can be used as AttributeVisitor immediately.
     */
    @Test
    public void testConstructor_instanceIsUsableImmediately() {
        // Act
        CatchExceptionMarker marker = new CatchExceptionMarker();

        // Assert - should be able to call visitor methods without NPE
        assertDoesNotThrow(() -> marker.visitAnyAttribute(null, null),
                "Newly constructed marker should be usable as an AttributeVisitor");
    }

    /**
     * Tests that the constructor creates instances that are equal by type but not by reference.
     */
    @Test
    public void testConstructor_createsSeparateInstances() {
        // Act
        CatchExceptionMarker marker1 = new CatchExceptionMarker();
        CatchExceptionMarker marker2 = new CatchExceptionMarker();

        // Assert
        assertEquals(marker1.getClass(), marker2.getClass(),
                "Both instances should be of the same class");
        assertNotSame(marker1, marker2,
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
                new CatchExceptionMarker();
            }
        }, "Constructor should handle rapid successive calls");
    }

    /**
     * Tests that constructed markers are distinct objects that can be stored in collections.
     */
    @Test
    public void testConstructor_instancesAreCollectable() {
        // Arrange
        java.util.Set<CatchExceptionMarker> markers = new java.util.HashSet<>();

        // Act - create multiple instances and add to collection
        for (int i = 0; i < 10; i++) {
            markers.add(new CatchExceptionMarker());
        }

        // Assert - all instances should be distinct
        assertEquals(10, markers.size(),
                "All 10 instances should be distinct and stored in the set");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new CatchExceptionMarker(),
                "Constructor should not throw any exception");
    }
}

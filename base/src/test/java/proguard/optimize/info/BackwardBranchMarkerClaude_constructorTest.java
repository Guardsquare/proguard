package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link BackwardBranchMarker} default constructor.
 *
 * The BackwardBranchMarker has an implicit default no-arg constructor since no constructor
 * is explicitly defined. This constructor simply creates a new instance with no initialization logic.
 */
public class BackwardBranchMarkerClaude_constructorTest {

    /**
     * Tests that the default constructor successfully creates a BackwardBranchMarker instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        BackwardBranchMarker marker = new BackwardBranchMarker();

        // Assert
        assertNotNull(marker, "BackwardBranchMarker instance should be created");
    }

    /**
     * Tests that the constructed BackwardBranchMarker is an instance of InstructionVisitor.
     */
    @Test
    public void testConstructor_createsInstructionVisitor() {
        // Act
        BackwardBranchMarker marker = new BackwardBranchMarker();

        // Assert
        assertInstanceOf(InstructionVisitor.class, marker,
                "BackwardBranchMarker should be an instance of InstructionVisitor");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        BackwardBranchMarker marker1 = new BackwardBranchMarker();
        BackwardBranchMarker marker2 = new BackwardBranchMarker();
        BackwardBranchMarker marker3 = new BackwardBranchMarker();

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
            new BackwardBranchMarker();
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
        final BackwardBranchMarker[][] results = new BackwardBranchMarker[threadCount][instancesPerThread];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < instancesPerThread; j++) {
                    results[threadIndex][j] = new BackwardBranchMarker();
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
        BackwardBranchMarker marker = new BackwardBranchMarker();

        // Assert - should be able to call visitor methods without NPE
        assertDoesNotThrow(() -> marker.visitAnyInstruction(null, null, null, 0, null),
                "Newly constructed marker should be usable as an InstructionVisitor");
    }

    /**
     * Tests that the constructor creates instances that are equal by type but not by reference.
     */
    @Test
    public void testConstructor_createsSeparateInstances() {
        // Act
        BackwardBranchMarker marker1 = new BackwardBranchMarker();
        BackwardBranchMarker marker2 = new BackwardBranchMarker();

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
                new BackwardBranchMarker();
            }
        }, "Constructor should handle rapid successive calls");
    }

    /**
     * Tests that constructed markers are distinct objects that can be stored in collections.
     */
    @Test
    public void testConstructor_instancesAreCollectable() {
        // Arrange
        java.util.Set<BackwardBranchMarker> markers = new java.util.HashSet<>();

        // Act - create multiple instances and add to collection
        for (int i = 0; i < 10; i++) {
            markers.add(new BackwardBranchMarker());
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
        assertDoesNotThrow(() -> new BackwardBranchMarker(),
                "Constructor should not throw any exception");
    }
}

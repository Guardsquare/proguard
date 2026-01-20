package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link FinalFieldAssignmentMarker} default constructor.
 *
 * The FinalFieldAssignmentMarker has an implicit default no-arg constructor since no constructor
 * is explicitly defined. This constructor simply creates a new instance with the referencedMethod
 * field initialized to null.
 */
public class FinalFieldAssignmentMarkerClaude_constructorTest {

    /**
     * Tests that the default constructor successfully creates a FinalFieldAssignmentMarker instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        FinalFieldAssignmentMarker marker = new FinalFieldAssignmentMarker();

        // Assert
        assertNotNull(marker, "FinalFieldAssignmentMarker instance should be created");
    }

    /**
     * Tests that the constructed FinalFieldAssignmentMarker is an instance of InstructionVisitor.
     */
    @Test
    public void testConstructor_createsInstructionVisitor() {
        // Act
        FinalFieldAssignmentMarker marker = new FinalFieldAssignmentMarker();

        // Assert
        assertInstanceOf(InstructionVisitor.class, marker,
                "FinalFieldAssignmentMarker should be an instance of InstructionVisitor");
    }

    /**
     * Tests that the constructed FinalFieldAssignmentMarker is an instance of ConstantVisitor.
     */
    @Test
    public void testConstructor_createsConstantVisitor() {
        // Act
        FinalFieldAssignmentMarker marker = new FinalFieldAssignmentMarker();

        // Assert
        assertInstanceOf(ConstantVisitor.class, marker,
                "FinalFieldAssignmentMarker should be an instance of ConstantVisitor");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        FinalFieldAssignmentMarker marker1 = new FinalFieldAssignmentMarker();
        FinalFieldAssignmentMarker marker2 = new FinalFieldAssignmentMarker();
        FinalFieldAssignmentMarker marker3 = new FinalFieldAssignmentMarker();

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
            new FinalFieldAssignmentMarker();
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
        final FinalFieldAssignmentMarker[][] results = new FinalFieldAssignmentMarker[threadCount][instancesPerThread];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < instancesPerThread; j++) {
                    results[threadIndex][j] = new FinalFieldAssignmentMarker();
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
    public void testConstructor_instanceIsUsableAsInstructionVisitorImmediately() {
        // Act
        FinalFieldAssignmentMarker marker = new FinalFieldAssignmentMarker();

        // Assert - should be able to call visitor methods without NPE
        assertDoesNotThrow(() -> marker.visitAnyInstruction(null, null, null, 0, null),
                "Newly constructed marker should be usable as an InstructionVisitor");
    }

    /**
     * Tests that constructed instances can be used as ConstantVisitor immediately.
     */
    @Test
    public void testConstructor_instanceIsUsableAsConstantVisitorImmediately() {
        // Act
        FinalFieldAssignmentMarker marker = new FinalFieldAssignmentMarker();

        // Assert - should be able to call visitor methods without NPE
        assertDoesNotThrow(() -> marker.visitAnyConstant(null, null),
                "Newly constructed marker should be usable as a ConstantVisitor");
    }

    /**
     * Tests that the constructor creates instances that are equal by type but not by reference.
     */
    @Test
    public void testConstructor_createsSeparateInstances() {
        // Act
        FinalFieldAssignmentMarker marker1 = new FinalFieldAssignmentMarker();
        FinalFieldAssignmentMarker marker2 = new FinalFieldAssignmentMarker();

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
                new FinalFieldAssignmentMarker();
            }
        }, "Constructor should handle rapid successive calls");
    }

    /**
     * Tests that constructed markers are distinct objects that can be stored in collections.
     */
    @Test
    public void testConstructor_instancesAreCollectable() {
        // Arrange
        java.util.Set<FinalFieldAssignmentMarker> markers = new java.util.HashSet<>();

        // Act - create multiple instances and add to collection
        for (int i = 0; i < 10; i++) {
            markers.add(new FinalFieldAssignmentMarker());
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
        assertDoesNotThrow(() -> new FinalFieldAssignmentMarker(),
                "Constructor should not throw any exception");
    }
}
